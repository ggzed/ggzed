package com.yf.log.annotation;

import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.model.log.enums.OperatorTypeEnum;

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
public @interface OperationLog {

    /**
     * 模块名
     */
    String title() default "";

    /**
     * 功能
     */
    BusinessTypeEnum businessType() default BusinessTypeEnum.OTHER;

    /**
     * 操作人类别
     */
    OperatorTypeEnum operatorType() default OperatorTypeEnum.MANAGE;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    boolean isSaveResponseData() default true;

    /**
     * 排除指定的请求参数
     */
    String[] excludeParamNames() default {};

}
