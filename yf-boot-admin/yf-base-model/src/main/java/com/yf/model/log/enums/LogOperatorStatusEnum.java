package com.yf.model.log.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yf.base.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作状态
 *
 * @author : YiFei
 * @since : 2023/11/1 22:57
 */
@Getter
@AllArgsConstructor
public enum LogOperatorStatusEnum implements IBaseEnum<Integer> {

    SUCCESS(1, "正常"),
    ERROR(0, "异常");


    @EnumValue //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    @JsonValue //  表示对枚举序列化时返回此字段
    private final Integer value;

    private final String label;

}
