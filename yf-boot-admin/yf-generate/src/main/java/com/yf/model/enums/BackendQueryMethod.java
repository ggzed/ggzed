package com.yf.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 后端查询类型
 *
 * @author : YiFei
 * @since : 2024/6/25 13:01
 */
@Getter
@AllArgsConstructor
public enum BackendQueryMethod {

    EQUALS("eq"), // 等于
    NOT_EQUALS("ne"), // 不等于
    GREATER_THAN("gt"), // 大于
    LESS_THAN("lt"), // 小于
    RANGE("range"); // 范围

    private final String method;
}