package com.yf.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 前端组件类型
 *
 * @author : YiFei
 * @since : 2024/6/25 13:01
 */
@Getter
@AllArgsConstructor
public enum FrontendComponentType {

    TEXT("text"), // 文本框
    TEXTAREA("textarea"), // 文本域
    SELECT("select"), // 下拉框
    CHECKBOX("checkbox"), // 复选框
    RADIO("radio"), // 单选框
    IMAGE("image"), // 图像
    DATE("date"); // 日期控件

    private final String type;
}
