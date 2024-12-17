package com.yf.rate_limiting.annotation;

import java.lang.annotation.*;

/**
 * 限流
 *
 * @author: yiFei
 * @since: 2022/8/10 17:36
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RateLimiters {
    /**
     * 多个限流规则
     */
    RateLimiter[] rateLimiters();
}

