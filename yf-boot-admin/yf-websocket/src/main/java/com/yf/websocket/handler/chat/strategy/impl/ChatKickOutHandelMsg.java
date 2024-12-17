package com.yf.websocket.handler.chat.strategy.impl;

import com.yf.model.websocket.dto.ChatRoomMessageDto;
import com.yf.model.websocket.enums.SocketChannelEnum;
import com.yf.websocket.base.BaseWebSocketHandler;
import com.yf.websocket.handler.chat.strategy.ChatHandelMsgStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * 踢出消息处理
 *
 * @author YiFei
 * @since 2024/5/24 21:23
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatKickOutHandelMsg implements ChatHandelMsgStrategy {

    private final RedissonClient redissonClient;

    @Override
    public SocketChannelEnum getChannelEnum() {
        return SocketChannelEnum.KICK_OUT;
    }

    @Override
    public boolean beforeHandelMessage(BaseWebSocketHandler baseWebSocketHandler, WebSocketSession session, ChatRoomMessageDto chatRoomMessage) {
        if (session == null) {
            // 踢出用户没有 session 为正常情况
            return true;
        } else {
            // 此处表示恶意访问
            // 方案一 : 恶意访问限流
            // 方案二 : 恶意访问则直接下线 ( 当前系统执行 )
            baseWebSocketHandler.close(session);
            return false;
        }
    }

    /**
     * 异常情况为编码问题
     * 正常情况 : 接收到消息证明一定是有一个服务中没有该用户，而转发到当前服务（ 则另外一个线程正在阻塞队列中获取对象 ）
     * 异常情况 : 有一个服务无法获取获取到 WebSocketHandler 所以采用发布订阅方式踢出用户 （ 但是未消费这个消息 ）
     */
    @Override
    public void handelMessage(BaseWebSocketHandler baseWebSocketHandler, WebSocketSession session, ChatRoomMessageDto chatRoomMessage) {
        // 1. 获取 receiverId ，踢下线 receiverId
        Long receiverId = chatRoomMessage.getReceiverId();
        try {
            baseWebSocketHandler.kickSocketSession(receiverId);
            // 2. 获取队列信息，添加一个队列消息
            RBlockingQueue<Object> blockingQueue = redissonClient.getBlockingQueue(baseWebSocketHandler.getServiceProvider().getLabel()
                    + BaseWebSocketHandler.QUEUE + receiverId
            );
            if (blockingQueue.isEmpty()) {
                blockingQueue.put(new Object());
            } else {
                log.warn("blockingQueue 中含有未消费元素 , 如果出现异常情况，请检查发送下线消息编码是否正确");
            }
        } catch (Exception e) {
            log.error(" ChatKickOutHandelMsg.handelMessage 执行 kickSocketSession 错误", e);
            // 2. 异常则关闭当前 session
            baseWebSocketHandler.close(session);
        }
    }
}
