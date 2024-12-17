package com.yf.websocket.handler.chat.strategy.impl;

import com.yf.constants.ChatRoomConstant;
import com.yf.constants.ChatRoomPermission;
import com.yf.model.websocket.dto.ChatRoomMessageDto;
import com.yf.model.websocket.enums.SocketChannelEnum;
import com.yf.rate_limiting.utils.RateLimiterUtil;
import com.yf.security.permission.PermissionChecker;
import com.yf.websocket.base.BaseWebSocketHandler;
import com.yf.websocket.handler.chat.strategy.ChatHandelMsgStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.TimeUnit;

/**
 * 聊天室私有消息
 *
 * @author YiFei
 * @since 2024/5/24 20:14
 */
@Component
@RequiredArgsConstructor
public class ChatPrivateHandelMsg implements ChatHandelMsgStrategy {

    private final PermissionChecker permissionChecker;
    private final RateLimiterUtil rateLimiterUtil;

    @Override
    public SocketChannelEnum getChannelEnum() {
        return SocketChannelEnum.PRIVATE;
    }

    @Override
    public boolean beforeHandelMessage(BaseWebSocketHandler baseWebSocketHandler, WebSocketSession session, ChatRoomMessageDto chatRoomMessage) {
        // 1. 如果是系统发送的消息，不拦截
        if (session == null) {
            return true;
        }
        // 2. 权限校验
        boolean checker = this.checker(
                baseWebSocketHandler,
                chatRoomMessage,
                (userId) -> permissionChecker.checker(userId, ChatRoomPermission.PRIVATE));
        if (!checker) {
            return false;
        }
        // 3. 限流校验
        boolean rateLimiter = this.rateLimiter(
                baseWebSocketHandler,
                chatRoomMessage, (userId) -> {
                    // 短时间高频限流规则
                    // 5 分钟内最多发送 20 条消息：防止短时间内的消息轰炸。
                    RateLimiterUtil.RateLimitRule shortTermLimit1 = RateLimiterUtil.RateLimitRule.builder()
                            .limit(20)
                            .timeDuration(5)
                            .timeUnit(TimeUnit.MINUTES)
                            .build();
                    // 1 小时内最多发送 100 条消息：防止中等时段内的高频消息。
                    RateLimiterUtil.RateLimitRule shortTermLimit2 = RateLimiterUtil.RateLimitRule.builder()
                            .limit(100)
                            .timeDuration(1)
                            .timeUnit(TimeUnit.HOURS)
                            .build();

                    // 长期限流规则
                    // 24 小时内最多发送 1000 条消息：防止一天内的持续高频消息。
                    RateLimiterUtil.RateLimitRule longTermLimit = RateLimiterUtil.RateLimitRule.builder()
                            .limit(1000)
                            .timeDuration(24)
                            .timeUnit(TimeUnit.HOURS)
                            .build();
                    // 应用限流规则
                    return rateLimiterUtil.rateLimiterGroup(ChatRoomConstant.CHAT_ROOM +
                            BaseWebSocketHandler.RATE +
                            userId, shortTermLimit1, shortTermLimit2, longTermLimit);
                }
        );
        return !rateLimiter;
    }

    @Override
    public void handelMessage(BaseWebSocketHandler baseWebSocketHandler, WebSocketSession session, ChatRoomMessageDto chatRoomMessage) {
        // 消息发送给指定用户
        baseWebSocketHandler.sendPrivateMessage(chatRoomMessage);
    }
}
