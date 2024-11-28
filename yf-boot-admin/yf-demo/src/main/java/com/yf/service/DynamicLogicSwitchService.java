package com.yf.service;

/**
 * 动态开关切换-服务类
 *
 * @author : YiFei
 * @since : 2024/11/28 20:28
 */
public interface DynamicLogicSwitchService {
    /**
     * 根据 number 决定执行逻辑
     */
    String executeLogic(Integer number);

    /**
     * 根据 number 决定是否同时执行逻辑
     */
    String togetherExecuteLogic(Integer number);

    /**
     * 根据 number 和 userId 决定执行逻辑
     */
    String rulesExecuteLogic(Integer number, Long userId);
}
