package com.yf.controller.open;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 心跳
 *
 * @author : YiFei
 * @since : 2025/5/14 22:16
 */
@Tag(name = "心跳")
@RestController
@RequestMapping("public")
@RequiredArgsConstructor
public class HeartbeatController {

    @Operation(summary = "心跳")
    @GetMapping("heartbeat")
    public String heartbeat() {
        return "ok";
    }

}
