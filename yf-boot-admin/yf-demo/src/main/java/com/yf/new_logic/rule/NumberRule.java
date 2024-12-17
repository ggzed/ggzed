package com.yf.new_logic.rule;

import com.yf.logic.rule.LogicSwitchRule;

/**
 * 根据Number决定调用新旧函数
 *
 * @author : YiFei
 * @since : 2024/11/28 20:46
 */
public class NumberRule implements LogicSwitchRule {
    @Override
    public boolean shouldApplyNewLogic(Object[] args) {
        Integer number = (Integer) args[0];
        return number % 2 == 0;
    }
}
