package com.yf.service.impl;

import com.yf.logic.annotation.DynamicLogicSwitch;
import com.yf.logic.model.enums.LogicOperator;
import com.yf.new_logic.rule.NumberRule;
import com.yf.new_logic.rule.TestUserRule;
import com.yf.new_logic.service.DSSNewLogic;
import com.yf.service.DynamicLogicSwitchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 动态开关切换-服务实现类
 *
 * @author : YiFei
 * @since : 2024/11/28 20:29
 */
@Service
@RequiredArgsConstructor
public class DynamicLogicSwitchServiceImpl implements DynamicLogicSwitchService {

    @DynamicLogicSwitch(value = DSSNewLogic.class, rules = NumberRule.class)
    @Override
    public String executeLogic(Integer number) {
        return "old logic : " + number;
    }

    @DynamicLogicSwitch(value = DSSNewLogic.class, rules = NumberRule.class, executeTogether = true)
    @Override
    public String togetherExecuteLogic(Integer number) {
        return (number % 2 == 0 ? "together execute logic : " : "old logic : ") + number;
    }

    @DynamicLogicSwitch(value = DSSNewLogic.class,
            rules = {NumberRule.class, TestUserRule.class},
            operator = LogicOperator.AND)
    @Override
    public String rulesExecuteLogic(Integer number, Long userId) {
        return "old logic && number % 2 != 0 OR userId != 999L";
    }

}
