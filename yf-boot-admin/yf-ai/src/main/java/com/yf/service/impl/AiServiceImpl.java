package com.yf.service.impl;

import com.yf.constants.ChatRequestConstant;
import com.yf.exception.ServiceException;
import com.yf.model.enums.MessageRoleEnum;
import com.yf.model.query.AiMessageQuery;
import com.yf.result.ResultCode;
import com.yf.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * AiService
 *
 * @author : YiFei
 * @since : 2024/6/7 9:30
 */
@Service("aiService")
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final OllamaChatModel ollamaChatModel;

    /**
     * ollama 回复
     *
     * @param messageQuery 提问消息
     * @return 返回消息
     */
    @Override
    public Flux<AssistantMessage> ollamaStream(AiMessageQuery messageQuery) {
        // 1. 聚合消息
        List<AbstractMessage> messages = this.aggregateMessages(messageQuery.getMessages());
        // 2. 构建 Prompt
        Prompt prompt = new Prompt(List.of(messages.toArray(new Message[0])),
                OllamaOptions.create()
                        .withModel(messageQuery.getModel())
                        .withTemperature(messageQuery.getTemperature())
                        .withTopP(messageQuery.getTopP())
                        .withRepeatPenalty(messageQuery.getRepeatPenalty())
        );
        // 3. 回复问题
        return ollamaChatModel.stream(prompt).map(chatResponse -> chatResponse.getResult().getOutput());
    }

    /**
     * ollama 整合消息
     *
     * @param messageQuery 提问消息
     * @return 整合后的消息
     */
    @Override
    public Flux<AssistantMessage> ollamaConsolidateMessage(AiMessageQuery messageQuery) {
        // 1. 聚合消息
        List<AbstractMessage> messages = this.aggregateMessages(messageQuery.getMessages());
        // 2. 添加系统消息
        SystemMessage systemMessage = new SystemMessage(ChatRequestConstant.SYSTEM_SIMPLIFICATION);
        messages.add(systemMessage);
        // 3. 构建 Prompt
        Prompt prompt = new Prompt(List.of(messages.toArray(new Message[0])),
                OllamaOptions.create()
                        .withModel(messageQuery.getModel())
                        .withTemperature(messageQuery.getTemperature())
                        .withTopP(messageQuery.getTopP())
                        .withRepeatPenalty(messageQuery.getRepeatPenalty())
        );
        // 4. 回复问题
        return ollamaChatModel.stream(prompt).map(chatResponse -> chatResponse.getResult().getOutput());
    }


    /**
     * 聚合消息
     *
     * @param messages 具体消息
     * @return 聚合后的消息
     */
    private List<AbstractMessage> aggregateMessages(List<AiMessageQuery.ActualMessage> messages) {
        // 1. 获取消息对象判断
        String userValue = MessageRoleEnum.USER.getValue();           // 用户消息
        String systemValue = MessageRoleEnum.SYSTEM.getValue();       // 系统消息
        String assistantValue = MessageRoleEnum.ASSISTANT.getValue(); // Ai助手回复消息
        // 2. 聚合消息
        return messages.stream()
                .map(item -> {
                    String role = item.getRole();
                    String content = item.getContent();
                    // 根据对应角色构建对应消息
                    if (userValue.equals(role)) {
                        return new UserMessage(content);
                    } else if (assistantValue.equals(role)) {
                        return new AssistantMessage(content);
                    } else if (systemValue.equals(role)) {
                        return new SystemMessage(content);
                    } else {
                        throw new ServiceException(ResultCode.AI_NOT_FOUND_ROLE);
                    }
                }).toList();
    }

}
