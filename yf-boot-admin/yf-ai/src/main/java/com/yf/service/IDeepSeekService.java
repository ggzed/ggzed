package com.yf.service;

import reactor.core.publisher.Flux;

/**
 * AiService
 *
 * @author : YiFei
 * @since : 2024/6/7 9:30
 */
public interface IDeepSeekService {

    /**
     * 调用 DeepSeek 模型
     *
     * @param prompt prompt
     * @return 流式响应
     */
    Flux<String> callStream(String prompt);
}
