package com.yf.websocket.handler.chat.strategy.impl;

import com.yf.model.websocket.dto.ChatRoomMessageDto;
import com.yf.model.websocket.enums.SocketChannelEnum;
import com.yf.websocket.base.BaseWebSocketHandler;
import com.yf.websocket.handler.chat.strategy.ChatHandelMsgStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * 欢迎消息处理
 *
 * @author : YiFei
 * @since : 2024/6/14 22:37
 */
@Component
@RequiredArgsConstructor
public class ChatWelcomeHandelMsg implements ChatHandelMsgStrategy {

    @Override
    public SocketChannelEnum getChannelEnum() {
        return SocketChannelEnum.SYSTEM;
    }

    @Override
    public void handelMessage(BaseWebSocketHandler baseWebSocketHandler, WebSocketSession session, ChatRoomMessageDto chatRoomMessage) {
        // 广播消息
        baseWebSocketHandler.broadcastMessage(session, chatRoomMessage);
    }
}
