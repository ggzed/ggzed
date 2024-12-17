package com.yf.model.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yf.base.IBaseEnum;
import lombok.Getter;

/**
 * 正常/禁用 枚举类
 *
 * @author : YiFei
 * @since : 2023/9/23 21:58
 */
@Getter
public enum EnableStatusEnum implements IBaseEnum<Integer> {

    DIS_ENABLE(0, "禁用"),
    ENABLE(1, "正常");

    @EnumValue //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    @JsonValue //  表示对枚举序列化时返回此字段
    private final Integer value;

    private final String label;

    EnableStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
