package com.yf.service.impl.new_logic.rule;

import com.yf.logic.rule.LogicSwitchRule;

/**
 * 根据用户决定逻辑是否执行
 *
 * @author : YiFei
 * @since : 2024/11/28 21:18
 */
public class TestUserRule implements LogicSwitchRule {

    @Override
    public boolean shouldApplyNewLogic(Object[] args) {
        // 假设 UserId 为 999 的用户是测试用户
        Long userId = (Long) args[1];
        return userId == 999L;
    }

}
