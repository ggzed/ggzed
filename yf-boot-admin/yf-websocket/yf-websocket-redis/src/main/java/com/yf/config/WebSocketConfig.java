package com.yf.config;

import com.yf.websocket.handler.ChatRoomHandler;
import com.yf.websocket.interceptor.ChatRoomAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * webSocket配置
 *
 * @author yiFei
 * @since 2024/3/11 15:45
 */
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatRoomHandler chatRoomHandler;
    private final ChatRoomAuthInterceptor chatRoomAuthInterceptor;

    /**
     * 注册 socket 服务
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatRoomHandler, "/chat-room")
                .setAllowedOrigins("*")
                .addInterceptors(chatRoomAuthInterceptor);
    }

}