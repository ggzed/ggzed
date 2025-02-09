package com.yf.rate_limiting.annotation;

import com.yf.rate_limiting.model.enums.DuplicateTypeEnum;

import java.lang.annotation.*;

/**
 * 请求信息日志
 *
 * @author : YiFei
 * @since : 2023/10/2 17:31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PreventDuplicateSubmit {

    DuplicateTypeEnum type() default DuplicateTypeEnum.IP;

    /**
     * 是否为全局
     * 默认 : ip:classSimpleName:methodName
     * 全局 : ip
     */
    boolean global() default false;


    /**
     * 防重提交锁过期时间(秒)
     * <p>
     * 默认1秒内不允许重复提交
     */
    long expire() default 1;

}
