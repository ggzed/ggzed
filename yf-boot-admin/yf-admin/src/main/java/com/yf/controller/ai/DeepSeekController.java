package com.yf.controller.ai;

import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
import com.yf.service.IDeepSeekService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * ai 控制器
 *
 * @author : YiFei
 * @since : 2024/6/6 17:34
 */
@Tag(name = "DeepSeek AI 控制器")
@RestController
@RequestMapping("ai/deep_seek")
@RequiredArgsConstructor
public class DeepSeekController {

    private final IDeepSeekService deepSeekService;

    @Operation(summary = "DeepSeek AI 流式请求")
    @PreventDuplicateSubmit
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(String prompt) {
        return deepSeekService.callStream(prompt);
    }

}
