package com.yf.logic.rule.impl;

import com.yf.logic.rule.LogicSwitchRule;
import com.yf.security.utils.SecurityUtil;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * 默认规则 : 默认加上注解后直接执行新逻辑
 *
 * @author : YiFei
 * @since : 2024/11/27 18:06
 */
@Component
public class DefaultLogicSwitchRule implements LogicSwitchRule {

    /**
     * 是否应用新逻辑 （ 未登录用户 & 00:00 ~ 04:00 执行新逻辑 ）
     *
     * @param args 方法参数
     * @return 是否应用新逻辑
     */
    @Override
    public boolean shouldApplyNewLogic(Object[] args) {
        // 判断是否为未登录用户
        Long userId = SecurityUtil.getUserId();
        if (userId == null) {
            return true;
        }
        // 获取当前时间
        LocalTime now = LocalTime.now();

        // 判断是否处于凌晨 00:00 到 04:00 之间
        return now.isAfter(LocalTime.MIDNIGHT) && now.isBefore(LocalTime.of(4, 0));
    }

}
