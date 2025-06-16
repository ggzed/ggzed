package com.yf.chat.memory;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Ai Message Table Chat Memory
 *
 * @author : YiFei
 * @since : 2025/6/9 23:17
 */
@Component
public class AiMessageChatMemory implements ChatMemory {


    @Override
    public void add(String conversationId, List<Message> messages) {
        
    }

    @Override
    public List<Message> get(String conversationId) {
        return null;
    }

    @Override
    public void clear(String conversationId) {

    }
}
