package com.yf.websocket.handler.chat.strategy;

import com.yf.constants.ChatRoomConstant;
import com.yf.model.websocket.dto.ChatRoomMessageDto;
import com.yf.model.websocket.enums.ChatRoomDefaultUserEnum;
import com.yf.model.websocket.enums.MessageProviderEnum;
import com.yf.model.websocket.enums.SocketChannelEnum;
import com.yf.websocket.base.BaseWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;

import java.util.function.Function;

/**
 * 聊天处理用户消息策略
 *
 * @author YiFei
 * @since 2024/5/24 16:57
 */
public interface ChatHandelMsgStrategy {
    /**
     * 获取频道类型
     *
     * @return 处理的频道
     */
    SocketChannelEnum getChannelEnum();

    /**
     * 处理消息的前置操作
     *
     * @return 是否需要继续处理
     */
    default boolean beforeHandelMessage(BaseWebSocketHandler baseWebSocketHandler, WebSocketSession session, ChatRoomMessageDto chatRoomMessage) {
        return true;
    }

    /**
     * 处理消息
     */
    void handelMessage(BaseWebSocketHandler baseWebSocketHandler, WebSocketSession session, ChatRoomMessageDto chatRoomMessage);

    /**
     * 处理消息的后置操作
     */
    default void afterHandelMessage(ChatRoomMessageDto chatRoomMessage) {
    }

    /**
     * 权限消息发送
     */
    default boolean checker(BaseWebSocketHandler baseWebSocketHandler, ChatRoomMessageDto chatRoomMessage, Function<Long, Boolean> permissionCheck) {
        // 1. 获取用户Id
        Long userId = this.getUserIdByMessage(chatRoomMessage);
        // 2. 根据传入 Supplier 判断是否拥有权限
        Boolean isPermission = permissionCheck.apply(userId);
        if (Boolean.FALSE.equals(isPermission)) {
            ChatRoomMessageDto chatRoomMessageDto = ChatRoomMessageDto.builder()
                    .senderId(ChatRoomDefaultUserEnum.SYSTEM.getValue())            // 系统发送
                    .receiverId(userId)                                             // 当前请求用户接收
                    .channel(SocketChannelEnum.PERMISSION)                          // 走权限频道
                    .serviceProvider(baseWebSocketHandler.getServiceProvider())     // 服务提供者
                    .messageProvider(MessageProviderEnum.SYSTEM)                    // 系统发送
                    .content(ChatRoomConstant.PERMISSION_DENIED_MESSAGE)            // 拦截内容
                    .build();
            baseWebSocketHandler.sendPrivateMessage(chatRoomMessageDto);
        }
        return isPermission;
    }

    /**
     * 限流消息发送
     */
    default boolean rateLimiter(BaseWebSocketHandler baseWebSocketHandler, ChatRoomMessageDto chatRoomMessage, Function<Long, Boolean> rateLimiterCheck) {
        // 1. 获取用户Id
        Long userId = this.getUserIdByMessage(chatRoomMessage);
        // 2. 根据传入 Supplier 判断是否拥有权限
        Boolean isLimiter = rateLimiterCheck.apply(userId);
        if (Boolean.TRUE.equals(isLimiter)) {
            ChatRoomMessageDto chatRoomMessageDto = ChatRoomMessageDto.builder()
                    .senderId(ChatRoomDefaultUserEnum.SYSTEM.getValue())            // 系统发送
                    .receiverId(userId)                                             // 当前请求用户接收
                    .channel(SocketChannelEnum.RATE_LIMITER)                        // 走限流频道
                    .serviceProvider(baseWebSocketHandler.getServiceProvider())     // 服务提供者
                    .messageProvider(MessageProviderEnum.SYSTEM)                    // 系统发送
                    .content(ChatRoomConstant.RATE_LIMITER_MESSAGE)                 // 拦截内容
                    .build();
            baseWebSocketHandler.sendPrivateMessage(chatRoomMessageDto);
        }
        return isLimiter;
    }

    /**
     * 根据消息体获取用户ID
     *
     * @param chatRoomMessageDto 消息体
     * @return 发送者用户ID
     */
    default Long getUserIdByMessage(ChatRoomMessageDto chatRoomMessageDto) {
        return chatRoomMessageDto.getSenderId();
    }

}
