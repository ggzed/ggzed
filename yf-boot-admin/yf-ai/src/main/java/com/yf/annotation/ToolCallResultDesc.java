package com.yf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ToolCallResultDesc
 *
 * @author : YiFei
 * @since : 2025/5/20 0:08
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ToolCallResultDesc {
    String value();
}
