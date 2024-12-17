package com.yf.model.websocket.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yf.base.IBaseEnum;
import lombok.Getter;

/**
 * 服务提供者枚举类
 *
 * @author YiFei
 * @since 2024/5/23 23:46
 */
@Getter
public enum ServiceProviderEnum implements IBaseEnum<Integer> {
    /**
     * 聊天室服务 ( value : 数据库插入值 , label : 缓存存储值 )
     */
    CHAT_ROOM(0, "CHAT:ROOM:"),
    /**
     * 数据大屏服务 ( value : 数据库插入值 , label : 缓存存储值 )
     */
    DATA_DASHBOARD(1, "DATA:DASHBOARD:"),
    /**
     * 人工客服服务 ( value : 数据库插入值 , label : 缓存存储值 )
     */
    MANUAL_SUPPORT(2, "MANUAL:SUPPORT:");

    @EnumValue //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    @JsonValue //  表示对枚举序列化时返回此字段
    private final Integer value;

    private final String label;

    ServiceProviderEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
