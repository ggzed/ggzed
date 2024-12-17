package com.yf.websocket.handler.chat.strategy.impl;

import com.yf.model.websocket.dto.ChatRoomMessageDto;
import com.yf.model.websocket.enums.SocketChannelEnum;
import com.yf.websocket.base.BaseWebSocketHandler;
import com.yf.websocket.handler.chat.strategy.ChatHandelMsgStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * 聊天室退出消息处理
 *
 * @author YiFei
 * @since 2024/5/26 0:24
 */
@Component
@RequiredArgsConstructor
public class ChatExitHandelMsg implements ChatHandelMsgStrategy {
    @Override
    public SocketChannelEnum getChannelEnum() {
        return SocketChannelEnum.EXIT;
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

    @Override
    public void handelMessage(BaseWebSocketHandler baseWebSocketHandler, WebSocketSession session, ChatRoomMessageDto chatRoomMessage) {
        // 广播消息
        baseWebSocketHandler.broadcastMessage(session, chatRoomMessage);
    }
}
