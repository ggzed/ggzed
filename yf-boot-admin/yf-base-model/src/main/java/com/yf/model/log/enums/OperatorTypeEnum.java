package com.yf.model.log.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yf.base.IBaseEnum;
import lombok.Getter;

/**
 * 日志中操作人类别
 *
 * @author : YiFei
 * @since : 2023/10/2 17:31
 */
@Getter
public enum OperatorTypeEnum implements IBaseEnum<Integer> {

    OTHER(0, "其他客户端"),
    MANAGE(1, "后台"),
    WEB_H5(2, "H5"),
    MOBILE(3, "手机端");


    @EnumValue //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    @JsonValue //  表示对枚举序列化时返回此字段
    private final Integer value;

    private final String label;

    OperatorTypeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
