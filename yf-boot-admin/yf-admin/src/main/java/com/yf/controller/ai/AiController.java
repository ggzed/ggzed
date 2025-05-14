package com.yf.controller.ai;

import com.yf.service.IAiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final IAiService aiService;

}
