package com.yf.websocket.subscriber;

import com.yf.model.dto.ChatRoomMessageDto;
import com.yf.websocket.handler.ChatRoomHandler;
import com.yf.websocket.handler.chat.context.ChatHandelMsgContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.listener.MessageListener;
import org.springframework.stereotype.Component;

/**
 * 聊天室消息订阅
 *
 * @author YiFei
 * @since 2024/5/23 20:23
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRoomSubscriber implements MessageListener<ChatRoomMessageDto> {

    private final ChatRoomHandler chatRoomHandler;
    private final ChatHandelMsgContext chatHandelMsgContext;

    /**
     * 处理消息
     *
     * @param chatRoomMessageDto 需要转发的消息体
     */
    @Override
    public void onMessage(CharSequence charSequence, ChatRoomMessageDto chatRoomMessageDto) {
        log.info("聊天室消息订阅 : 接收到消息 SenderId : {} , ReceiverId : {} , Channel : {}",
                chatRoomMessageDto.getSenderId(),
                chatRoomMessageDto.getReceiverId(),
                chatRoomMessageDto.getChannel().getLabel());
        /*
        * 正常情况 : 这里永远获取不到发送者的 session
        * 异常情况 : 本机给本机 Subscriber 发送消息，则能获取到 session （ 例如: 心跳踢出用户 ）
        * */
        chatHandelMsgContext.handleMessage(chatRoomHandler, null, chatRoomMessageDto);
    }
}
