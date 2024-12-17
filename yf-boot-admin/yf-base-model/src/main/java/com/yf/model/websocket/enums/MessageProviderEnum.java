package com.yf.model.websocket.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yf.base.IBaseEnum;
import lombok.Getter;

/**
 * 消息提供者枚举
 *
 * @author YiFei
 * @since 2024/5/23 23:48
 */
@Getter
public enum MessageProviderEnum implements IBaseEnum<Integer> {

    USER(0, "用户"),
    SYSTEM(1, "系统"),
    AI_ASSISTANT(2, "AI助手");

    @EnumValue //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    @JsonValue //  表示对枚举序列化时返回此字段
    private final Integer value;

    private final String label;

    MessageProviderEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
