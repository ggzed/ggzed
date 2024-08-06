package com.yf.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.yf.base.IBaseEnum;
import lombok.Getter;

/**
 * 频道枚举 ( 1. 目前明确系统不会新增频道所以未涉及数据库表 , 2. 每个频道对应一个权限信息 )
 *
 * @author YiFei
 * @since 2024/5/24 8:33
 */
@Getter
public enum SocketChannelEnum implements IBaseEnum<Integer> {

    SYSTEM(0, "系统频道"),
    PUBLIC(1, "公共频道"),
    PRIVATE(2, "私有频道"),
    AI(3, "AI频道"),
    /**
     * 特殊说明 : 只有在本地没有该socket的时候才会走下线频道 , 本地有则直接进入退出频道
     */
    KICK_OUT(4, "踢出频道"),
    WELCOME(5, "欢迎频道"),
    EXIT(6, "退出频道"),
    PERMISSION(7, "权限频道"),
    RATE_LIMITER(8, "限流频道"),
    HEARTBEAT(9, "心跳频道");

    @EnumValue //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    private final Integer value;

    private final String label;

    SocketChannelEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
