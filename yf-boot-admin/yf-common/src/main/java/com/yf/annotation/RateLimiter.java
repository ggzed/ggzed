package com.yf.annotation;

import com.yf.model.enums.LimitTypeEnum;
import com.yf.model.result.ResultCode;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RateLimiter {
    /**
     * 限流类型 ( 默认全局 )
     */
    LimitTypeEnum limitTypeEnum() default LimitTypeEnum.GLOBAL;

    /**
     * 对应限流规则
     */
    RateRule[] rateRules();

    /**
     * 在限流后，确定是否将请求加入黑名单 ( 用户 和 ip 都将进入黑名单 )
     */
    boolean addToBlacklist() default false;

    /**
     * 如果配置了 RateLimiters 中 message，以 RateLimiters 为准
     */
    ResultCode message() default ResultCode.REQUEST_RATE_LIMIT;
}
