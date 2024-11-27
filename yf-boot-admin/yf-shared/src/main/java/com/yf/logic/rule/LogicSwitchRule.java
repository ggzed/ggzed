package com.yf.logic.rule;

/**
 * 规则接口
 *
 * @author : YiFei
 * @since : 2024/11/25 22:45
 */
public interface LogicSwitchRule {

    /**
     * 是否应用新逻辑
     *
     * @param args 方法参数
     * @return 是否应用新逻辑
     */
    boolean shouldApplyNewLogic(Object[] args);

}
