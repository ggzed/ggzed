package com.yf.controller.demo.dynamic_logic_switch;

import com.yf.result.Result;
import com.yf.service.DynamicLogicSwitchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 动态开关切换-控制器
 *
 * @author : YiFei
 * @since : 2024/11/28 20:26
 */
@Tag(name = "动态开关切换")
@RestController
@RequestMapping("dynamic-logic-switch")
@RequiredArgsConstructor
public class DynamicLogicSwitchController {

    private final DynamicLogicSwitchService dynamicLogicSwitchService;

    @GetMapping("/{number}/execute-logic")
    public Result<String> executeLogic(@PathVariable("number") Integer number) {
        return Result.success(dynamicLogicSwitchService.executeLogic(number));
    }

    @GetMapping("/{number}/together-execute-logic")
    public Result<String> togetherExecuteLogic(@PathVariable("number") Integer number) {
        return Result.success(dynamicLogicSwitchService.togetherExecuteLogic(number));
    }

    @GetMapping("/{number}/{userId}/rules-execute-logic")
    public Result<String> rulesExecuteLogic(@PathVariable("number") Integer number, @PathVariable("userId") Long userId) {
        return Result.success(dynamicLogicSwitchService.rulesExecuteLogic(number, userId));
    }


}
