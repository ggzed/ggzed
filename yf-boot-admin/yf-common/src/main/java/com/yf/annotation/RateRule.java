package com.yf.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RateRule {

    /**
     * 限流次数
     */
    long limit() default 10;

    /**
     * 限流时间
     */
    long timeDuration() default 60;

    /**
     * 限流时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

}
