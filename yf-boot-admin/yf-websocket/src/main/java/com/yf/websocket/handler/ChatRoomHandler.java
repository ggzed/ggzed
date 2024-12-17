package com.yf.websocket.handler;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.yf.configuration.SystemConfiguration;
import com.yf.constants.ChatRoomConstant;
import com.yf.constants.WebSocketConstant;
import com.yf.mapper.log.OperateLogMapper;
import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.model.log.enums.OperatorTypeEnum;
import com.yf.model.websocket.dto.ChatRoomMessageDto;
import com.yf.model.websocket.dto.UserConnectInfo;
import com.yf.model.websocket.enums.ChatRoomDefaultUserEnum;
import com.yf.model.websocket.enums.MessageProviderEnum;
import com.yf.model.websocket.enums.ServiceProviderEnum;
import com.yf.model.websocket.enums.SocketChannelEnum;
import com.yf.security.model.dto.SysUserDetails;
import com.yf.service.ISocketMessageService;
import com.yf.utils.RedisUtil;
import com.yf.websocket.base.BaseWebSocketHandler;
import com.yf.websocket.handler.chat.context.ChatHandelMsgContext;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 聊天室处理
 *
 * @author YiFei
 * @since 2024/5/23 20:18
 */
@Slf4j
@Component
public class ChatRoomHandler extends BaseWebSocketHandler {

    private final Map<Long, WebSocketSession> chatRoomMap = new ConcurrentHashMap<>();
    private final ChatHandelMsgContext handelMsgContext;

    private final SensitiveWordBs sensitiveWordBs;

    @Autowired
    public ChatRoomHandler(RedisUtil redisUtil, RedissonClient redissonClient, SystemConfiguration systemConfiguration, ISocketMessageService socketMessageService, OperateLogMapper operateLogMapper, ChatHandelMsgContext handelMsgContext, SensitiveWordBs sensitiveWordBs, PlatformTransactionManager transactionManager, ObjectMapper objectMapper) {
        super(redisUtil, redissonClient, systemConfiguration, socketMessageService, operateLogMapper, transactionManager, objectMapper);
        // 权限校验器
        this.handelMsgContext = handelMsgContext;
        this.sensitiveWordBs = sensitiveWordBs;
    }

    /**
     * 存储连接用户的 map
     */
    @Override
    public Map<Long, WebSocketSession> getWebSocketSessionMap() {
        return chatRoomMap;
    }

    /**
     * 服务类型为聊天室
     */
    @Override
    public ServiceProviderEnum getServiceProvider() {
        return ServiceProviderEnum.CHAT_ROOM;
    }

    /**
     * 业务操作为聊天室
     */
    @Override
    public BusinessTypeEnum getBusinessTypeEnum() {
        return BusinessTypeEnum.CHAT_ROOM;
    }

    /**
     * 聊天室服务于管理端
     */
    @Override
    public OperatorTypeEnum getOperatorTypeEnum() {
        return OperatorTypeEnum.MANAGE;
    }

    /**
     * 判断是否为非系统用户（系统用户的ID为0L）
     *
     * @param userId 用户ID
     * @return 是否为非系统用户
     */
    @Override
    public boolean isNonSystemUser(Long userId) {
        for (ChatRoomDefaultUserEnum value : ChatRoomDefaultUserEnum.values()) {
            // 1. 判断是否为系统用户
            if (value.getValue().equals(userId)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 连接成功后执行
     *
     * @param session 连接的 session
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // 1. 获取授权的用户信息
        SysUserDetails userDetails = (SysUserDetails) session.getAttributes().get(WebSocketConstant.SOCKET_SESSION_USER_ID);
        Long userId = userDetails.getUserId();
        // 2. 判断缓存中是否含有改用户
        UserConnectInfo userConnectInfo = this.getCacheUserConnectInfo(userId);
        if (userConnectInfo != null) {
            // 2.1 踢出改用户
            this.kickSocketSession(userId);
        }
        // 3. 添加登录当前用户
        this.addUserInSocket(userDetails, session);
        // 4. 此处不推送目前已经登录的用户信息 ，由用户请求其他服务获取

        // 5. 输出系统欢迎登录的消息
        this.sendWelcomeMessage(session, userId, userDetails.getUsername());
        // 6. 给发送一条心跳消息 : 防止有用户逃逸 （比如连接了不发送心跳消息）
        this.sendHeartbeatMessage(session);
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) {
        // 允许自己给自己发送信息
        if (message instanceof TextMessage textMessage) {
            // 1. 解析发送数据
            ChatRoomMessageDto chatRoomMessage = JSON.parseObject(textMessage.getPayload(), ChatRoomMessageDto.class);
            if (chatRoomMessage == null) {
                this.close(session);
                return;
            }
            String content = chatRoomMessage.getContent();
            // 2. 非心跳消息则过滤字段 : 防止用户恶意消息
            if (StringUtils.hasText(content)) {
                if (content.length() >= 2000) {
                    return;
                }
                String replace = sensitiveWordBs.replace(content);
                chatRoomMessage.setContent(replace);
            }
            // 3. 填充默认信息 : 防止用户恶意篡改
            chatRoomMessage.setSenderId(this.getUserIdBySession(session));
            chatRoomMessage.setServiceProvider(ServiceProviderEnum.CHAT_ROOM);
            chatRoomMessage.setMessageProvider(MessageProviderEnum.USER);
            // 4. 根据频道选择发送方式
            handelMsgContext.handleMessage(this, session, chatRoomMessage);
        }
    }


    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) {
        log.error("服务器可能意外中端", exception);
    }

    /**
     * session.close() 触发 ， 正常断开触发
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus status) {
        // 顺序不能变 ， 否则断开连接的当前 session ( 或者 senderId 设置为当前用户id )
        SysUserDetails userDetails = (SysUserDetails) session.getAttributes().get(WebSocketConstant.SOCKET_SESSION_USER_ID);
        Long userId = userDetails.getUserId();
        // 1. 删除当前用户的 session
        this.disconnectionSocket(session, status);
        // 2. 发送退出消息
        this.sendExitMessage(session, userId);
    }

    /**
     * 发送一条心跳消息到本地（ 初始化心跳检测 ）
     *
     * @param session 连接对象
     */
    private void sendHeartbeatMessage(WebSocketSession session) {
        // 1. 构建心跳消息
        ChatRoomMessageDto heartbeatMessage = ChatRoomMessageDto.builder().channel(SocketChannelEnum.HEARTBEAT).build();
        // 2. 发送心跳消息
        handelMsgContext.handleMessage(this, session, heartbeatMessage);
    }

    /**
     * 输出退出消息
     *
     * @param session 当前连接对象
     * @param userId  用户Id
     */
    public void sendExitMessage(WebSocketSession session, Long userId) {
        String exitMessage = ChatRoomConstant.EXIT_MESSAGE.formatted(userId);
        ChatRoomMessageDto chatRoomMessageDto = ChatRoomMessageDto.builder()
                .senderId(ChatRoomDefaultUserEnum.PUBLIC.getValue())            // userId
                .serviceProvider(this.getServiceProvider())                     // 消息为聊天室提供
                .messageProvider(MessageProviderEnum.SYSTEM)                    // 消息由系统发送
                .channel(SocketChannelEnum.EXIT)                                // 频道为退出频道
                .content(exitMessage)                                           // 退出聊天室消息
                .build();
        // 广播消息
        this.broadcastMessage(session, chatRoomMessageDto);
    }

    /**
     * 输出欢迎消息
     *
     * @param session  当前连接对象
     * @param userId   当前用户ID
     * @param username 用户名
     */
    public void sendWelcomeMessage(WebSocketSession session, Long userId, String username) {
        String welcome = ChatRoomConstant.WELCOME.formatted(userId);
        ChatRoomMessageDto chatRoomMessageDto = ChatRoomMessageDto.builder()
                .senderId(ChatRoomDefaultUserEnum.PUBLIC.getValue())    // 构建发送者
                .serviceProvider(this.getServiceProvider())             // 消息为聊天室提供
                .messageProvider(MessageProviderEnum.SYSTEM)            // 消息由系统发送
                .channel(SocketChannelEnum.WELCOME)                     // 频道为欢迎频道
                .content(welcome)
                .build();
        this.broadcastMessage(session, chatRoomMessageDto);
    }
}
