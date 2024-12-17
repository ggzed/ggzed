package com.yf.websocket.handler.chat.strategy.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yf.configuration.SystemConfiguration;
import com.yf.constants.ChatRoomConstant;
import com.yf.model.websocket.dto.ChatRoomMessageDto;
import com.yf.model.websocket.dto.UserConnectInfo;
import com.yf.model.websocket.enums.ChatRoomDefaultUserEnum;
import com.yf.model.websocket.enums.MessageProviderEnum;
import com.yf.model.websocket.enums.ServiceProviderEnum;
import com.yf.model.websocket.enums.SocketChannelEnum;
import com.yf.ratelimit.SimpleRateLimiter;
import com.yf.utils.RedisUtil;
import com.yf.websocket.base.BaseWebSocketHandler;
import com.yf.websocket.handler.chat.strategy.ChatHandelMsgStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 聊天室心跳处理
 *
 * @author YiFei
 * @since 2024/5/24 21:26
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatHeartbeatHandelMsg implements ChatHandelMsgStrategy {
    private final SimpleRateLimiter heartbeatRateLimiter = new SimpleRateLimiter(6, TimeUnit.MINUTES.toMillis(1));
    private final Map<Long, Long> lastHeartbeatTimes = new ConcurrentHashMap<>();

    private final RedissonClient redissonClient;
    private final SystemConfiguration systemConfiguration;
    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;

    @Override
    public SocketChannelEnum getChannelEnum() {
        return SocketChannelEnum.HEARTBEAT;
    }


    /**
     * 每30秒检查一次,是否断开连接
     * 可以使用系统自定义的定时任务执行器 ， 在 @PostConstruct 启动
     */
    @Scheduled(fixedRate = 30000)
    public void checkHeartbeats() {
        log.debug("websocket:chat-room 心跳检测");
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<Long, Long> entry : lastHeartbeatTimes.entrySet()) {
            // 1. 检测时间
            Long userId = entry.getKey();
            long lastHeartbeatTime = entry.getValue();
            // 超过1分钟未收到心跳，认为客户端断开连接
            if (currentTime - lastHeartbeatTime > TimeUnit.MINUTES.toMillis(1)) {
                // 断开用户连接
                handleUserDisconnection(userId);
                // 清除本地缓存
                heartbeatRateLimiter.cleanUp(userId.toString());
                lastHeartbeatTimes.remove(userId);
            }
        }
    }

    @Override
    public void handelMessage(BaseWebSocketHandler baseWebSocketHandler, WebSocketSession session, ChatRoomMessageDto chatRoomMessage) {
        Long userId = baseWebSocketHandler.getUserIdBySession(session);
        // 1. 限流
        if (!heartbeatRateLimiter.tryAcquire(userId.toString())) {
            // 心跳超过限流 , 下线改用户
            baseWebSocketHandler.close(session);
            return;
        }
        // 2. 发送心跳消息 : 可以对心跳消息进行加密解密 （ 取决于您对系统安全的要求 ）
        try {
            // 2.1 构建心跳消息
            ChatRoomMessageDto heartbeatMessage = ChatRoomMessageDto.builder()
                    .channel(SocketChannelEnum.HEARTBEAT)
                    .build();
            // 2.2 发送心跳消息
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(heartbeatMessage)));
        } catch (Exception e) {
            // 发送异常不更新心跳信息 : 相当于等待重连 超时则断开连接
            return;
        }
        // 3. 更新心跳信息
        this.updateHeartbeat(userId);
    }

    /**
     * 更新心跳时间
     *
     * @param userId 用户Id
     */
    private void updateHeartbeat(Long userId) {
        lastHeartbeatTimes.put(userId, System.currentTimeMillis());
    }

    /**
     * 用户断开连接
     *
     * @param userId 需要断开连接的用户
     */
    private void handleUserDisconnection(Long userId) {
        try {
            UserConnectInfo userConnectInfo = redisUtil.getCacheObject(ServiceProviderEnum.CHAT_ROOM.getLabel() + userId);
            if (userConnectInfo == null) {
                return;
            }
            String kickOutMessage = ChatRoomConstant.KICK_OUT_MESSAGE.formatted(userId);
            // 2. 构建下线消息
            ChatRoomMessageDto kickMessage = ChatRoomMessageDto.builder()
                    .senderId(ChatRoomDefaultUserEnum.PUBLIC.getValue())    // 身份为系统
                    .serviceProvider(ServiceProviderEnum.CHAT_ROOM)         // 消息为聊天室提供
                    .messageProvider(MessageProviderEnum.SYSTEM)            // 消息由系统发送
                    .channel(SocketChannelEnum.KICK_OUT)                    // 频道为踢出频道
                    .content(kickOutMessage)                                // 踢出消息
                    .build();

            // 3. 发布消息到主题
            RTopic topic = redissonClient.getTopic(
                    ServiceProviderEnum.CHAT_ROOM.getLabel() + systemConfiguration.getMachineName());
            topic.publish(kickMessage);

            // 4. 阻塞队列等待操作完毕
            RBlockingQueue<Object> blockingQueue = redissonClient.getBlockingQueue(
                    ServiceProviderEnum.CHAT_ROOM.getLabel() +
                            BaseWebSocketHandler.QUEUE + userId);

            Object poll = blockingQueue.poll(BaseWebSocketHandler.QUEUE_TIME_OUT, TimeUnit.SECONDS);
            if (poll == null) {
                log.warn("心跳机制踢出用户失败 ，可能用户已经被删除，用户ID: {}", userId);
            } else {
                blockingQueue.delete();
            }
        } catch (InterruptedException e) {
            log.error("心跳机制踢出用户异常，线程被中断", e);
            Thread.currentThread().interrupt(); // 恢复中断状态
        } catch (Exception e) {
            log.error("心跳机制踢出用户时发生错误", e);
        }
    }
}
