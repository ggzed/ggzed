package com.yf.websocket.handler.chat.strategy.impl;

import com.yf.constants.ChatRoomPermission;
import com.yf.model.dto.ChatRoomMessageDto;
import com.yf.model.enums.SocketChannelEnum;
import com.yf.security.permission.PermissionChecker;
import com.yf.websocket.base.BaseWebSocketHandler;
import com.yf.websocket.handler.chat.strategy.ChatHandelMsgStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * 系统消息处理
 *
 * @author YiFei
 * @since 2024/5/24 17:01
 */
@Component
@RequiredArgsConstructor
public class ChatSystemHandelMsg implements ChatHandelMsgStrategy {

    private final PermissionChecker permissionChecker;

    @Override
    public SocketChannelEnum getChannelEnum() {
        return SocketChannelEnum.SYSTEM;
    }

    /**
     * 处理消息的前置操作
     *
     * @return 是否需要继续处理
     */
    @Override
    public boolean beforeHandelMessage(BaseWebSocketHandler baseWebSocketHandler, WebSocketSession session, ChatRoomMessageDto chatRoomMessage) {
        // 2. 权限校验
        return this.checker(
                baseWebSocketHandler,
                chatRoomMessage,
                (userId) -> permissionChecker.checker(userId, ChatRoomPermission.SYSTEM));
    }

    @Override
    public void handelMessage(BaseWebSocketHandler baseWebSocketHandler, WebSocketSession session, ChatRoomMessageDto chatRoomMessage) {
        // 广播消息
        baseWebSocketHandler.broadcastMessage(session, chatRoomMessage);
    }
}
