package com.yf.new_logic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * DynamicLogicSwitchServiceNewLogic
 *
 * @author : YiFei
 * @since : 2024/11/28 20:36
 */
@Component
@RequiredArgsConstructor
public class DSSNewLogic {

    public String executeLogic(Integer number) {
        return "new logic : " + number;
    }

    public String togetherExecuteLogic(Integer number) {
        return "new logic : " + number;
    }

    public String rulesExecuteLogic(Integer number, Long userId) {
        return "new logic && number % 2 == 0 && userId == 999L";
    }
}
