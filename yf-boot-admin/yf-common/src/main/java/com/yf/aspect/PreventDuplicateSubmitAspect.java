package com.yf.aspect;

import com.yf.annotation.PreventDuplicateSubmit;
import com.yf.constants.RedisKeyConstants;
import com.yf.exception.ServiceException;
import com.yf.model.enums.DuplicateTypeEnum;
import com.yf.model.result.ResultCode;
import com.yf.utils.IpUtil;
import com.yf.utils.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * PreventDuplicateSubmit 切面
 *
 * @author YiFei
 * @since 2024/5/10 18:05
 */
@Aspect
@Order(0)
@Component
@Slf4j
@RequiredArgsConstructor
public class PreventDuplicateSubmitAspect {

    private final RedissonClient redissonClient;

    @Around(value = "@annotation(preventDuplicateSubmit)")
    public Object preventDuplicateSubmit(ProceedingJoinPoint joinPoint, PreventDuplicateSubmit preventDuplicateSubmit) throws Throwable {
        String prefixKey = generateResubmitLockKey(joinPoint, preventDuplicateSubmit);
        RLock lock = redissonClient.getLock(prefixKey);
        if (!lock.tryLock(0, preventDuplicateSubmit.expire(), TimeUnit.SECONDS)) {
            if (preventDuplicateSubmit.type().equals(DuplicateTypeEnum.ARGS)) {
                throw new ServiceException(ResultCode.REQUEST_OTHER_OPERATION);
            } else {
                throw new ServiceException(ResultCode.REQUEST_MORE_ERROR);
            }
        }
        return joinPoint.proceed();
    }

    /**
     * 生成防重复提交的键
     *
     * @param joinPoint              切入点，表示当前拦截的方法
     * @param preventDuplicateSubmit 防重复提交注解
     * @return 防重复提交的 Redis 键
     */
    private String generateResubmitLockKey(ProceedingJoinPoint joinPoint, PreventDuplicateSubmit preventDuplicateSubmit) {
        String prefix = "";
        Object[] args = joinPoint.getArgs();
        // 获取防重复提交的类型
        DuplicateTypeEnum type = preventDuplicateSubmit.type();

        // 根据类型生成键的前缀
        switch (type) {
            case USER_ID -> {
                // 用户ID模式
                Long userId = SecurityUtil.getUserId();
                if (userId == null) {
                    log.error("PreventDuplicateSubmitAspect.generateResubmitLockKey -> USER_ID 未解析到用户Id");
                } else {
                    prefix = userId.toString();
                }
            }
            case USER_NAME -> {
                // 用户名模式
                String username = SecurityUtil.getUsername();
                if (username == null) {
                    log.error("PreventDuplicateSubmitAspect.generateResubmitLockKey -> USER_NAME 未解析出用户名");
                } else {
                    prefix = username;
                }
            }
            case ARGS ->
                // 参数模式 : TODO 后续修改为 Spring 表达式语言（SP EL）
                // 注意：使用参数生成键时，应防止生成过大的键
                    prefix = Arrays.deepToString(args);
            default -> {
                // 默认模式为IP模式
                HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
                prefix = IpUtil.getIpAddr(request);
            }
        }

        // 如果不是全局限制，进一步添加类名和方法名作为前缀
        if (!preventDuplicateSubmit.global()) {
            // 获取目标方法所属的类的名称
            String className = joinPoint.getTarget().getClass().getSimpleName();
            // 获取方法的名称
            String methodName = joinPoint.getSignature().getName();
            // 组合前缀
            prefix = prefix + ":" + className + ":" + methodName;
        }

        // 返回完整的 Redis 键
        return RedisKeyConstants.PREVENT_DUPLICATE_SUBMIT_PREFIX + prefix;
    }


}
