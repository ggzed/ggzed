package com.yf.constants;

/**
 * 聊天室中常量
 *
 * @author YiFei
 * @since 2024/5/23 18:24
 */
public interface ChatRoomConstant {
    /**
     * 聊天室前缀
     */
    String CHAT_ROOM = "CHAT:ROOM:";
    /**
     * 欢迎消息模版
     */
    String WELCOME = "欢迎用户 %s 进入聊天室";
    /**
     * 消息未发送错误模版
     */
    String MESSAGE_NO_SEND = "%s 发送消息找不到对应对象 %s , 可能对象 %s 未登录";
    /**
     * 限流拦截消息
     */
    String RATE_LIMITER_MESSAGE = "您已经被限流";
    /**
     * 权限拦截信息
     */
    String PERMISSION_DENIED_MESSAGE = "您没有权限执行此操作";

    /**
     * 踢出聊天室信息
     */
    String KICK_OUT_MESSAGE = " %s 已被踢出聊天室";

    /**
     * 退出聊天室信息
     */
    String EXIT_MESSAGE = "%s 退出聊天室";

    /**
     * 心跳消息
     */
    String HEARTBEAT_MESSAGE = "HEARTBEAT";
}
