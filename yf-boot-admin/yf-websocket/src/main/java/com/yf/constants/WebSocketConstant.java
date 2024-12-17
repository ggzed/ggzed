package com.yf.constants;

/**
 * websocket常量
 *
 * @author YiFei
 * @since 2024/5/23 20:29
 */
public interface WebSocketConstant {

    /**
     * WebSocketSession 存储用户 Spring_Security 上下文信息
     */
    String SOCKET_SESSION_USER_ID = "USER_INFO";
    /**
     * WebSocketSession 存储 连接信息 信息
     */
    String SOCKET_SESSION_CONNECT_INFO = "CONNECT_INFO";
}
