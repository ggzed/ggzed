package com.yf.websocket.handler.chat.context;

import com.yf.constants.WebSocketConstant;
import com.yf.model.dto.ChatRoomMessageDto;
import com.yf.model.dto.SysUserDetails;
import com.yf.model.enums.SocketChannelEnum;
import com.yf.websocket.base.BaseWebSocketHandler;
import com.yf.websocket.handler.chat.strategy.ChatHandelMsgStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 策略上下文
 *
 * @author YiFei
 * @since 2024/5/24 16:59
 */
@Component
public class ChatHandelMsgContext {

    private final Map<SocketChannelEnum, ChatHandelMsgStrategy> chatHandelMsgStrategyMap = new EnumMap<>(SocketChannelEnum.class);

    @Autowired
    public ChatHandelMsgContext(List<ChatHandelMsgStrategy> chatHandelMsgStrategies) {
        chatHandelMsgStrategies.forEach(strategy -> chatHandelMsgStrategyMap.put(strategy.getChannelEnum(), strategy));
    }

    /**
     * 处理消息
     *
     * @param baseWebSocketHandler 当前处理的服务
     * @param session              连接会话信息
     * @param chatRoomMessage      消息内容
     */
    public void handleMessage(BaseWebSocketHandler baseWebSocketHandler, WebSocketSession session, ChatRoomMessageDto chatRoomMessage) {
        ChatHandelMsgStrategy chatHandelMsgStrategy = chatHandelMsgStrategyMap.get(chatRoomMessage.getChannel());
        if (chatHandelMsgStrategy != null) {
            // 获取到 chatHandelMsgStrategy 执行对应方法
            if (chatHandelMsgStrategy.beforeHandelMessage(baseWebSocketHandler, session, chatRoomMessage)) {
                chatHandelMsgStrategy.handelMessage(baseWebSocketHandler, session, chatRoomMessage);
                chatHandelMsgStrategy.afterHandelMessage(chatRoomMessage);
            }
        } else {
            // 获取不到改消息表示恶意请求,直接踢出
            Long userId = ((SysUserDetails) session.getAttributes()
                    .get(WebSocketConstant.SOCKET_SESSION_USER_ID)).getUserId();
            baseWebSocketHandler.kickSocketSession(userId);
        }
    }

}
