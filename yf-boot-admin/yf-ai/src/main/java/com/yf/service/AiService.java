package com.yf.service;

import com.yf.model.query.AiMessageQuery;
import org.springframework.ai.chat.messages.AssistantMessage;
import reactor.core.publisher.Flux;

/**
 * AiService
 *
 * @author : YiFei
 * @since : 2024/6/7 9:30
 */
public interface AiService {
    /**
     * ollama 回复
     *
     * @param messageQuery 提问消息
     * @return 返回消息
     */
    Flux<AssistantMessage> ollamaStream(AiMessageQuery messageQuery);

    /**
     * ollama 整合消息
     *
     * @param messageQuery 提问消息
     * @return 整合后的消息
     */
    Flux<AssistantMessage> ollamaConsolidateMessage(AiMessageQuery messageQuery);
}
