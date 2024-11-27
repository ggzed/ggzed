package com.yf.logic.annotation;

import com.yf.logic.model.enums.LogicOperator;
import com.yf.logic.rule.LogicSwitchRule;
import com.yf.logic.rule.impl.DefaultLogicSwitchRule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态切换新旧逻辑注解
 *
 * @author : YiFei
 * @since : 2024/11/25 23:01
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicLogicSwitch {

    // 新逻辑处理类 @DynamicLogicSwitch( NewLogic.class )
    Class<?> value();

    // 指定规则类
    Class<? extends LogicSwitchRule>[] rules() default DefaultLogicSwitchRule.class; // 支持多个规则类

    // 规则逻辑运算符 (AND / OR)
    LogicOperator operator() default LogicOperator.AND;

    // 是否在满足规则后新旧逻辑一起执行 ( 注 : 为确保正确性, 如果有返回结果, 返回旧逻辑结果)
    boolean executeTogether() default false;
    
}
