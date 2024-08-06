package com.yf.websocket.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yf.configuration.SystemConfiguration;
import com.yf.constants.ChatRoomConstant;
import com.yf.constants.RedisKeyConstants;
import com.yf.constants.WebSocketConstant;
import com.yf.exception.ServiceException;
import com.yf.model.dto.ChatRoomMessageDto;
import com.yf.model.dto.SessionConnectInfo;
import com.yf.model.dto.SysUserDetails;
import com.yf.model.dto.UserConnectInfo;
import com.yf.model.entity.OperateLog;
import com.yf.model.entity.SocketMessage;
import com.yf.model.enums.*;
import com.yf.model.result.ResultCode;
import com.yf.model.vo.SocketMessageVO;
import com.yf.model.vo.UserInfoVO;
import com.yf.service.IOperateLogService;
import com.yf.service.ISocketMessageService;
import com.yf.utils.RedisUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 基础的websocket操作
 * 脏词过滤修改 aho-corasick（未维护） 转 sensitive-word （维护中）
 *
 * @author YiFei
 * @since 2024/5/23 18:04
 */
@Slf4j
@Component
public abstract class BaseWebSocketHandler extends AbstractWebSocketHandler {
    public static final String ALL = "ALL";         // socket 中全部用户存储 key 后缀
    public static final String TOPIC = "TOPIC:";    // 主题
    public static final String QUEUE = "QUEUE:";    // 队列前缀
    public static final String RATE = "RATE:";      // 限流前缀
    public static final int QUEUE_TIME_OUT = 5;     // 队列超时时间
    private static final String TRANSACTION_MANAGER_NAME = "WEBSOCKET";  //事物名
    private final RedisUtil redisUtil;
    private final RedissonClient redissonClient;
    private final SystemConfiguration systemConfiguration;
    private final ISocketMessageService socketMessageService;
    private final IOperateLogService operateLogService;
    private final PlatformTransactionManager transactionManager;
    private final ObjectMapper objectMapper;

    protected BaseWebSocketHandler(RedisUtil redisUtil, RedissonClient redissonClient, SystemConfiguration systemConfiguration, ISocketMessageService socketMessageService, IOperateLogService operateLogService, PlatformTransactionManager transactionManager, ObjectMapper objectMapper) {
        this.redisUtil = redisUtil;
        this.redissonClient = redissonClient;
        this.systemConfiguration = systemConfiguration;
        this.socketMessageService = socketMessageService;
        this.operateLogService = operateLogService;
        this.transactionManager = transactionManager;
        this.objectMapper = objectMapper;
    }

    /**
     * 从子类中获取 websocket集合
     *
     * @return Map<用户Id, socket链接>
     */
    public abstract Map<Long, WebSocketSession> getWebSocketSessionMap();

    /**
     * 获取服务类型
     *
     * @return 服务提供者 (0:chat_room【CHAT:ROOM:】, 1:data_dashboard【DATA:DASHBOARD:】...)
     */
    public abstract ServiceProviderEnum getServiceProvider();

    /**
     * 业务名类型获取 (例如 : CHAT_ROOM => 聊天室)
     *
     * @return （value : 业务值 ， label : 业务名）
     */
    public abstract BusinessTypeEnum getBusinessTypeEnum();

    /**
     * 服务于那端
     *
     * @return MANAGE 管理端 , H5 H5页面 ...
     */
    public abstract OperatorTypeEnum getOperatorTypeEnum();

    /**
     * 获取用户信息
     */
    public UserConnectInfo getCacheUserConnectInfo(Long userId) {
        return redisUtil.getCacheObject(this.getServiceProvider().getLabel() + userId);
    }

    /**
     * 添加用户到 socket 服务中
     */
    protected void addUserInSocket(SysUserDetails user, WebSocketSession session) {
        Long userId = user.getUserId();
        String username = user.getUsername();
        LocalDateTime now = LocalDateTime.now();
        Map<Long, WebSocketSession> webSocketSessionMap = getWebSocketSessionMap();

        // 1. 存储用户连接信息到本地
        webSocketSessionMap.put(userId, session);

        // 2. 存储用户连接信息到 redis
        storeUserInfoInRedis(userId, username, now);
    }

    /**
     * 存储用户连接信息到 redis
     *
     * @param userId   用户Id
     * @param username 用户名
     * @param now      当前时间
     */
    private void storeUserInfoInRedis(Long userId, String username, LocalDateTime now) {
        // 1. 缓存中获取当前连接用户信息 : 等于空表示用户更改了个人信息，但是没有写入缓存 ( 目前为了效率，采用全部存储在 redis )
        UserInfoVO userInfoVO = redisUtil.getCacheObject(RedisKeyConstants.SYSTEM_ME_CACHE_PREFIX + userId);
        String avatar = "";
        String nickname = "";
        if (userInfoVO != null) {
            avatar = userInfoVO.getAvatar();
            nickname = userInfoVO.getNickname();
        }
        // 2. 构建用户连接信息
        UserConnectInfo userConnectInfo = UserConnectInfo.builder().userId(userId)                                         // 连接用户ID
                .avatar(avatar)                                         // 用户头像
                .nickname(nickname)                                     // 用户昵称
                .username(username)                                     // 连接的用户名
                .machineName(systemConfiguration.getMachineName())      // 机器信息
                .connectTime(now)                                       // 连接时间
                .build();
        // 3. 当前用户添加到所有用户的集合中
        String redisKeyAll = this.getServiceProvider().getLabel() + ALL;
        redisUtil.addCacheZSetValue(redisKeyAll, userId, now.toEpochSecond(ZoneOffset.UTC));
        // 4. 添加当前用户连接信息到 redis 中
        String redisKeyUser = this.getServiceProvider().getLabel() + userId;
        redisUtil.setCacheObject(redisKeyUser, userConnectInfo);
    }

    /**
     * 群发消息
     *
     * @param chatRoomMessageDto 消息体
     */
    public void broadcastMessage(WebSocketSession session, ChatRoomMessageDto chatRoomMessageDto) {
        // 1. 针对频道设置发送者
        Map<Long, WebSocketSession> webSocketSessionMap = this.getWebSocketSessionMap();
        Long senderId = chatRoomMessageDto.getSenderId();
        // 2. 遍历所有用户发送消息
        for (Map.Entry<Long, WebSocketSession> entry : webSocketSessionMap.entrySet()) {
            Long receiverId = entry.getKey();
            // 设置接收者
            chatRoomMessageDto.setReceiverId(receiverId);
            // 3. 排除自己 : 在排除自己以后才添加到 socketMessageList 可以保证自己数据不会被添加到数据库
            if (!receiverId.equals(senderId)) {
                // 4. 发送消息给用户
                sendMessageToUser(chatRoomMessageDto, entry.getValue());
            }
        }
        // perf 🚀 :手动事务
        TransactionStatus status = this.getTransactionStatus();

        try {
            /*
             * 5. 发布消息，记录日志
             * 理清思路，最多四种规则 :
             * （1）有 session && 是系统默认用户  => 发布任务，不记录日志 （代表是公共的系统消息）
             * （2）有 session && 不是默认用户 => 发布任务，记录日志 （代表是公共消息的用户消息）
             * （3）没有 session && 是默认用户 => 不发布任务，不记录日志 （代表是其他服务系统发送的消息）
             * （4）没有 session && 不是默认用户 => 不存在这种情况
             * */
            if (session != null) {
                if (isNonSystemUser(senderId)) {
                    // 5.1 TODO 存储发送信息集合 ( 发送给所有用户 , 包括自己 )
                    log.info("发送改消息给所有人 , ( 发送给所有用户 , 包括自己 )");
                    // 5.2 TODO 记录日志
                    log.info("记录日志");
                }
                // 5.3 发布任务
                this.publishBroadcastMessage(chatRoomMessageDto);
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            log.error("broadcastMessage", e);
            transactionManager.rollback(status);
            throw e;
        }
    }

    /**
     * 发布群消息任务到 redis
     *
     * @param chatRoomMessageDto 消息体
     */
    private void publishBroadcastMessage(ChatRoomMessageDto chatRoomMessageDto) {
        Set<String> allMachine = redisUtil.getCacheSet(RedisKeyConstants.SYSTEM_MACHINE);
        String currentMachineName = systemConfiguration.getMachineName();
        // 1. 变量所有机器
        for (String machine : allMachine) {
            // 2. 排除当前机器
            if (!machine.equals(currentMachineName)) {
                RTopic topic = redissonClient.getTopic(this.getServiceProvider().getLabel() + machine);
                topic.publish(chatRoomMessageDto);
                log.info("发送消息到机器号 {} 上", machine);
            }
        }
    }

    /**
     * 发送消息给用户
     *
     * @param chatRoomMessageDto 消息体
     * @param session            连接信息
     */
    private void sendMessageToUser(ChatRoomMessageDto chatRoomMessageDto, WebSocketSession session) {
        Long receiverId = chatRoomMessageDto.getReceiverId();
        Long senderId = chatRoomMessageDto.getSenderId();
        try {
            // 1. 构建发送消息
            String sendMessage = createSendMessage(chatRoomMessageDto);
            // 2. 发送消息
            session.sendMessage(new TextMessage(sendMessage));
        } catch (IOException e) {
            log.error("发送消息错误 broadcastMessage , 发送者: {} ,接收者 : {} , 发送时用户退出", senderId, receiverId, e);
        }
    }


    /**
     * 私有消息
     *
     * @param chatRoomMessageDto 发送消息对象
     */
    public void sendPrivateMessage(ChatRoomMessageDto chatRoomMessageDto) {
        Long senderId = chatRoomMessageDto.getSenderId();
        Long receiverId = chatRoomMessageDto.getReceiverId();
        // 1. 获取接收方的 session
        WebSocketSession webSocketSession = getWebSocketSessionMap().get(receiverId);
        if (webSocketSession != null) {
            // perf 🚀 :手动事务
            TransactionStatus status = this.getTransactionStatus();
            try {
                // 2. SocketMessage 构建
                SocketMessage.SocketMessageBuilder builder = createSocketMessageBuilder(chatRoomMessageDto);
                // 3. 本地存在接收方则直接发送
                sendMessageDirectly(chatRoomMessageDto, webSocketSession, builder);
                // 4. 存储消息
                this.socketMessageService.save(builder.build());
                // 5. 不是默认用户 => 记录日志
                if (isNonSystemUser(senderId)) {
                    keepOperationLog(webSocketSession, builder.build());
                }
                transactionManager.commit(status);
            } catch (Exception e) {
                log.error("sendPrivateMessage", e);
                transactionManager.rollback(status);
                throw e;
            }
        } else {
            // 6. 本地不存在则发送消息到 redis
            publishMessageToRedis(chatRoomMessageDto, receiverId);
        }
    }

    /**
     * 通过 chatRoomMessageDto 创建 SocketMessage.SocketMessageBuilder 以便存储
     *
     * @param chatRoomMessageDto 消息体
     * @return SocketMessage.SocketMessageBuilder 构造器
     */
    private SocketMessage.SocketMessageBuilder createSocketMessageBuilder(ChatRoomMessageDto chatRoomMessageDto) {
        return SocketMessage.builder().receiverId(chatRoomMessageDto.getReceiverId())                         // 接收者ID
                .senderId(chatRoomMessageDto.getSenderId())                             // 发送者ID
                .channel(chatRoomMessageDto.getChannel())                               // 频道
                .messageProvider(chatRoomMessageDto.getMessageProvider())               // 消息提供者
                .serviceProvider(this.getServiceProvider())                             // 服务提供者
                .isSend(YesNoEnum.YES.getValue())                                       // 是否发送 ( 默认发送 )
                .content(chatRoomMessageDto.getContent());                              // 发送消息
    }

    /**
     * 点对点消息发送
     *
     * @param chatRoomMessageDto 消息体
     * @param webSocketSession   连接信息
     * @param builder            SocketMessage.SocketMessageBuilder 构造器
     */
    private void sendMessageDirectly(ChatRoomMessageDto chatRoomMessageDto, WebSocketSession webSocketSession, SocketMessage.SocketMessageBuilder builder) {
        try {
            // 1. 构建发送消息对象
            String sendMessage = createSendMessage(chatRoomMessageDto);
            // 2. 发送消息
            webSocketSession.sendMessage(new TextMessage(sendMessage));
        } catch (IOException e) {
            log.error("发送消息错误 sendPrivateMessage , 发送者: {} ,接收者 : {}", chatRoomMessageDto.getSenderId(), chatRoomMessageDto.getReceiverId(), e);
            builder.isSend(YesNoEnum.NO.getValue()).errorMsg(StringUtils.substring(e.getMessage(), 0, 1000));
        }
    }

    /**
     * 发布消息到 redis
     *
     * @param chatRoomMessageDto 消息体
     * @param receiverId         接收者ID
     */
    private void publishMessageToRedis(ChatRoomMessageDto chatRoomMessageDto, Long receiverId) {
        // 1. 获取缓存中接收者 ID
        UserConnectInfo cacheUserConnectInfo = this.getCacheUserConnectInfo(receiverId);
        if (cacheUserConnectInfo != null) {
            // 2. 发布到 Redis
            RTopic topic = redissonClient.getTopic(this.getServiceProvider().getLabel() + cacheUserConnectInfo.getMachineName());
            topic.publish(chatRoomMessageDto);
        } else {
            log.error("本地不存在该用户-缓存用户不存在 , 发送者: {} ,接收者 : {}", chatRoomMessageDto.getSenderId(), receiverId);
        }
    }

    /**
     * 断开连接调用
     *
     * @param session 连接信息
     * @param status  目前不处理状态
     */
    public void disconnectionSocket(WebSocketSession session, CloseStatus status) {
        SysUserDetails userDetails = (SysUserDetails) session.getAttributes().get(WebSocketConstant.SOCKET_SESSION_USER_ID);
        Long userId = userDetails.getUserId();
        // 1. 删除本地 session 信息
        this.getWebSocketSessionMap().remove(userId);
        // 2. 从所有用户中删除用户ID （ zset中删除 ）
        this.deleteUserConnectionInfo(userId);
        // 4. 记录退出日志
        keepOperationLog(session, SocketMessage.builder()
                .senderId(userId)
                .messageProvider(MessageProviderEnum.SYSTEM)            // 系统消息
                .serviceProvider(this.getServiceProvider())             // 服务提供
                .content(ChatRoomConstant.EXIT_MESSAGE)                 // 退出消息
                .build());
    }

    /**
     * 根据用户名删除缓存中用户连接信息
     *
     * @param userId 用户ID
     */
    private void deleteUserConnectionInfo(Long userId) {
        redisUtil.removeZSetMember(this.getServiceProvider().getLabel() + ALL, userId);
        // 3. 删除用户连接信息
        redisUtil.deleteObject(this.getServiceProvider().getLabel() + userId);
    }

    /**
     * 踢下线操作
     *
     * @param userId 用户ID
     */
    public void kickSocketSession(Long userId) {
        // 1. 获取本地是否存在该用户
        WebSocketSession webSocketSession = getWebSocketSessionMap().get(userId);
        // 2. 本地存在直接下线 , 本地不存在则发布到 redis
        if (webSocketSession == null) {
            publishKickMessageToRedis(userId);
        } else {
            this.close(webSocketSession);
        }
    }

    /**
     * 发送踢出消息到 redis
     *
     * @param userId 用户Id
     */
    @SneakyThrows
    private void publishKickMessageToRedis(Long userId) {
        // 1. 缓存中获取对象
        UserConnectInfo userConnectInfo = this.getCacheUserConnectInfo(userId);

        if (userConnectInfo == null) {
            return;
        }
        /*
         * 2. 机器号相同删除缓存信息
         * 情况复现 : 系统突然宕机 , 大量用户本地不存在 ，但是缓存中存在
         *
         * 举例 : 用户A原本在机器一上面登录、此时机器一宕机，本地缓存不存在用户A，Redis缓存中存在用户A，
         * 这时候 用户A重连 需要踢出原本的用户A，判断本地中没有用户A，获取缓存中用户A上线的机器信息(机器一)
         * 然后发布任务到机器一上踢出用户，此时机器一就陷入了阻塞队列无限等待远程机器上的用户A被踢出
         * */
        if (systemConfiguration.getMachineName().equals(userConnectInfo.getMachineName())) {
            this.deleteUserConnectionInfo(userConnectInfo.getUserId());
            return;
        }
        // 3. 构建需要发送的消息
        ChatRoomMessageDto kickMessage = ChatRoomMessageDto.builder().senderId(0L)                                        // 身份为系统
                .serviceProvider(this.getServiceProvider())          // 填充消息服务
                .receiverId(userId)                                  // 接收的用户
                .messageProvider(MessageProviderEnum.SYSTEM)         // 消息由系统发送
                .channel(SocketChannelEnum.KICK_OUT)         // 频道为下线频道
                .content(ChatRoomConstant.KICK_OUT_MESSAGE)          // 踢出消息
                .build();
        // 4. 获取主题发送踢出消息
        RTopic topic = redissonClient.getTopic(this.getServiceProvider().getLabel() + userConnectInfo.getMachineName());
        topic.publish(kickMessage);
        // 5. 阻塞队列等待踢出消息处理完成
        RBlockingQueue<Object> blockingQueue = redissonClient.getBlockingQueue(this.getServiceProvider().getLabel() + QUEUE + userId);
        // 注: InterruptedException 线程中断错误
        Object executed = blockingQueue.poll(QUEUE_TIME_OUT, TimeUnit.SECONDS);
        // 6. 处理成功清空队列 ， 处理失败断开连接
        if (executed == null) {
            log.error("socket拒绝连接 , 踢出用户 {} 失败 , 拒绝当前连接", userId);
            throw new ServiceException(ResultCode.SOCKET_REJECT_CONNECT);
        } else {
            blockingQueue.delete();
        }
    }

    /**
     * 构建一个事物
     *
     * @return TransactionStatus
     */
    private TransactionStatus getTransactionStatus() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName(TRANSACTION_MANAGER_NAME + getServiceProvider().getLabel());
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 新发起一个事务
        return transactionManager.getTransaction(def);
    }


    /**
     * 通过 ChatRoomMessageDto 构建发送对象
     *
     * @param chatRoomMessageDto 接收到的消息
     * @return 发送字符串
     */
    @SneakyThrows
    private String createSendMessage(ChatRoomMessageDto chatRoomMessageDto) {
        // 2. 构建返回消息
        SocketMessageVO socketMessageVO = SocketMessageVO.builder()
                .channel(chatRoomMessageDto.getChannel())               // 返回频道
                .senderId(chatRoomMessageDto.getSenderId())             // 返回发送者
                .receiverId(chatRoomMessageDto.getReceiverId())         // 接受者
                .content(chatRoomMessageDto.getContent())               // 返回内容
                .build();
        return objectMapper.writeValueAsString(socketMessageVO);
    }

    /**
     * 记录日志
     *
     * @param session 需要记录的 session 对象
     */
    @SneakyThrows
    protected void keepOperationLog(WebSocketSession session, SocketMessage message) {
        OperateLog.OperateLogBuilder builder = OperateLog.builder().title(this.getBusinessTypeEnum().getLabel())   // 日志标题
                .businessType(this.getBusinessTypeEnum())       // 日志业务类型
                .operatorType(this.getOperatorTypeEnum());      // 日志操作端
        if (session == null) {
            // 1. session == null 构建错误信息
            builder.errorMsg(message.getErrorMsg());            // 错误信息
        } else {
            // 2. session != null 获取连接信息
            Map<String, Object> attributes = session.getAttributes();
            SessionConnectInfo sessionConnectInfo = (SessionConnectInfo) attributes.get(WebSocketConstant.SOCKET_SESSION_CONNECT_INFO);
            // 3. 构建日志对象
            builder.operatorIp(sessionConnectInfo.getOperatorIp())                 // IP
                    .operatorOs(sessionConnectInfo.getOperatorOs())                 // 操作系统
                    .operatorBrowser(sessionConnectInfo.getOperatorBrowser())       // 操作浏览器
                    .operatorLocation(sessionConnectInfo.getOperatorLocation())     // 操作地址
                    .jsonResult(StringUtils.substring(objectMapper.writeValueAsString(message), 0, 2000));
        }
        // 4. 存储日志数据
        this.operateLogService.save(builder.build());
    }

    /**
     * 根据 session 获取用户ID
     *
     * @param session 连接信息
     * @return 用户ID
     */
    public Long getUserIdBySession(WebSocketSession session) {
        return ((SysUserDetails) session.getAttributes().get(WebSocketConstant.SOCKET_SESSION_USER_ID)).getUserId();
    }

    /**
     * 判断是否为非系统用户（系统用户的ID为0L）
     *
     * @param userId 用户ID
     * @return 是否为非系统用户
     */
    public boolean isNonSystemUser(Long userId) {
        return userId != 0L;
    }

    /**
     * 关闭连接
     *
     * @param session 连接的session
     */
    public void close(WebSocketSession session) {
        try {
            if (session != null && session.isOpen()) {
                session.close();
            }
        } catch (IOException ex) {
            log.error("Session {} , LocalAddress : {} , RemoteAddress : {} 关闭错误", session.getId(), session.getLocalAddress(), session.getRemoteAddress(), ex);
            throw new RuntimeException(ex);
        }
    }
}


