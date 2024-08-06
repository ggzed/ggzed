package com.yf.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yf.base.IBaseEnum;
import lombok.Getter;

/**
 * 聊天室默认用户
 *
 * @author YiFei
 * @since 2024/5/24 15:06
 */
@Getter
public enum ChatRoomDefaultUserEnum implements IBaseEnum<Long> {

    SYSTEM(0L, "系统"),
    AI_ASSISTANT(1L, "AI助手"),
    PUBLIC(2L, "群消息");

    @EnumValue //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    @JsonValue //  表示对枚举序列化时返回此字段
    private final Long value;

    private final String label;

    ChatRoomDefaultUserEnum(Long value, String label) {
        this.value = value;
        this.label = label;
    }
}
