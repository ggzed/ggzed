package com.yf.logic.aspect;

import com.yf.logic.annotation.DynamicLogicSwitch;
import com.yf.logic.model.enums.LogicOperator;
import com.yf.logic.rule.LogicSwitchRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态切换新旧逻辑注解切面
 *
 * @author : YiFei
 * @since : 2024/11/25 23:15
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DynamicLogicSwitchAspect {

    private final ApplicationContext applicationContext;

    /**
     * 切面逻辑：拦截方法上的 @DynamicLogicSwitch 注解
     *
     * @param joinPoint          切点
     * @param dynamicLogicSwitch dynamicLogicSwitch 注解
     */
    @Around("@annotation(dynamicLogicSwitch)")
    public Object switchLogic(ProceedingJoinPoint joinPoint, DynamicLogicSwitch dynamicLogicSwitch) throws Throwable {
        // 提取公共变量
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        long startTime = System.currentTimeMillis();

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage beforeHeapMemory = memoryMXBean.getHeapMemoryUsage();

        log.info("Starting logic switch for method: {} in class: {}", methodName, className);

        boolean shouldSwitch = false;
        try {
            // 评估规则逻辑
            shouldSwitch = evaluateRules(dynamicLogicSwitch, joinPoint.getArgs());
            log.info("Logic switch decision for method {}: {}. Execute together: {}",
                    methodName, shouldSwitch, dynamicLogicSwitch.executeTogether());

            if (shouldSwitch) {
                if (dynamicLogicSwitch.executeTogether()) {
                    // 新旧逻辑一起执行
                    log.info("Executing dual logic for method: {}", methodName);

                    // 执行新逻辑
                    try {
                        executeNewLogic(dynamicLogicSwitch.value(), joinPoint);
                    } catch (Throwable e) {
                        log.error("Error executing new logic for method: {}", methodName, e);
                    }

                    // 执行旧逻辑并返回结果
                    return joinPoint.proceed();
                } else {
                    // 仅执行新逻辑
                    log.info("Executing new logic only for method: {}", methodName);
                    return executeNewLogic(dynamicLogicSwitch.value(), joinPoint);
                }
            } else {
                // 执行旧逻辑
                log.info("Skipping new logic. Executing original logic for method: {}", methodName);
                return joinPoint.proceed();
            }
        } finally {
            // 统计性能
            long endTime = System.currentTimeMillis();
            MemoryUsage afterHeapMemory = memoryMXBean.getHeapMemoryUsage();

            log.info("Completed logic for method: {}. Execution type: {}. Time: {} ms. Memory: {} KB.",
                    methodName,
                    dynamicLogicSwitch.executeTogether() ?
                            "Dual execution" : (shouldSwitch ?
                            "New logic" : "Original logic"),
                    (endTime - startTime),
                    (afterHeapMemory.getUsed() - beforeHeapMemory.getUsed()) / 1024);
        }
    }

    /**
     * 切面逻辑：拦截类上的 @DynamicLogicSwitch 注解
     *
     * @param joinPoint          切点
     * @param dynamicLogicSwitch dynamicLogicSwitch 注解
     */
    @Around("@within(dynamicLogicSwitch)")
    public Object switchClassLogic(ProceedingJoinPoint joinPoint, DynamicLogicSwitch dynamicLogicSwitch) throws Throwable {
        // 检查目标类是否为普通类
        Class<?> targetClass = joinPoint.getTarget().getClass();
        if (!isOrdinaryClass(targetClass)) {
            throw new IllegalArgumentException("Target class must be an ordinary class: " + targetClass.getName());
        }

        return switchLogic(joinPoint, dynamicLogicSwitch);
    }

    /**
     * 校验是否为普通类
     */
    public boolean isOrdinaryClass(Class<?> clazz) {
        return !clazz.isInterface() && !clazz.isEnum() && !clazz.isAnnotation() && !clazz.isPrimitive() && !clazz.isArray();
    }

    /**
     * 评估规则逻辑
     */
    private boolean evaluateRules(DynamicLogicSwitch logicSwitch, Object[] args) {
        Class<? extends LogicSwitchRule>[] ruleClasses = logicSwitch.rules();
        List<Boolean> ruleResults = new ArrayList<>();

        for (Class<? extends LogicSwitchRule> ruleClass : ruleClasses) {
            try {
                LogicSwitchRule rule = getHandlerInstance(ruleClass);
                boolean result = rule.shouldApplyNewLogic(args);
                ruleResults.add(result);
            } catch (Exception e) {
                log.error("Failed to evaluate rule: {}", ruleClass.getName(), e);
                ruleResults.add(false); // 默认规则失败时为 false
            }
        }

        // 根据逻辑运算符 (AND / OR) 合并规则结果
        LogicOperator operator = logicSwitch.operator();
        return (operator == LogicOperator.AND) ?
                ruleResults.stream().allMatch(Boolean::booleanValue) :
                ruleResults.stream().anyMatch(Boolean::booleanValue);
    }

    /**
     * 执行新逻辑
     */
    private Object executeNewLogic(Class<?> newLogicHandlerClass, ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Executing new logic using handler: {}", newLogicHandlerClass.getName());

        Object newLogicHandler = getHandlerInstance(newLogicHandlerClass);
        Method method = findMatchingMethod(newLogicHandler, joinPoint);

        return method.invoke(newLogicHandler, joinPoint.getArgs());
    }

    /**
     * 获取新逻辑处理器实例
     */
    private <T> T getHandlerInstance(Class<T> handlerClass) throws Exception {
        try {
            return applicationContext.getBean(handlerClass);
        } catch (NoSuchBeanDefinitionException e) {
            return handlerClass.getDeclaredConstructor().newInstance();
        }
    }

    /**
     * 匹配目标方法
     */
    private Method findMatchingMethod(Object target, ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        String methodName = joinPoint.getSignature().getName();
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        return target.getClass().getMethod(methodName, parameterTypes);
    }

}
