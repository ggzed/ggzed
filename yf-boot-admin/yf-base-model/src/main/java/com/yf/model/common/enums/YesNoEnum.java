package com.yf.model.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yf.base.IBaseEnum;
import lombok.Getter;

/**
 * 是 / 否 枚举类
 *
 * @author YiFei
 * @since 2024/5/23 22:49
 */
@Getter
public enum YesNoEnum implements IBaseEnum<Integer> {

    NO(0, "否"),
    YES(1, "是");

    @EnumValue //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    @JsonValue //  表示对枚举序列化时返回此字段
    private final Integer value;

    private final String label;

    YesNoEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
