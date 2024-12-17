package com.yf.controller.ai;


import com.yf.model.query.AiMessageQuery;
import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
import com.yf.rate_limiting.annotation.RateLimiter;
import com.yf.rate_limiting.annotation.RateLimiters;
import com.yf.rate_limiting.annotation.RateRule;
import com.yf.rate_limiting.model.enums.LimitTypeEnum;
import com.yf.service.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;

/**
 * ai 控制器
 *
 * @author : YiFei
 * @since : 2024/6/6 17:34
 */
@Tag(name = "ai 控制器")
@RestController
@RequestMapping("ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    /**
     * 每10秒访问一次 ， 每天 60 次
     */
    @Operation(summary = "ollama")
    @PreventDuplicateSubmit(expire = 10)
    @RateLimiters(rateLimiters = {
            @RateLimiter(limitTypeEnum = LimitTypeEnum.USER_ID,
                    rateRules = {@RateRule(
                            limit = 60,
                            timeDuration = 1,
                            timeUnit = TimeUnit.DAYS)})
    })
    @PostMapping(value = "/ollama/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AssistantMessage> stream(@RequestBody @Validated AiMessageQuery messageQuery) {
        return aiService.ollamaStream(messageQuery);
    }

    /**
     * 每60秒访问一次 ， 每天 10 次
     */
    @Operation(summary = "ollama 聚合消息")
    @RateLimiters(rateLimiters = {
            @RateLimiter(limitTypeEnum = LimitTypeEnum.USER_ID,
                    rateRules = {@RateRule(
                            limit = 60,
                            timeDuration = 1,
                            timeUnit = TimeUnit.DAYS)})
    })
    @PreventDuplicateSubmit(expire = 60)
    @PostMapping(value = "/ollama/consolidate/message", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AssistantMessage> ollamaConsolidateMessage(@RequestBody @Validated AiMessageQuery messageQuery) {
        return aiService.ollamaConsolidateMessage(messageQuery);
    }


}
