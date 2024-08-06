package com.yf.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yf.base.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 菜单类型枚举
 *
 * @author: YiFei
 * @since: 2023/9/18 11:31
 */
@Getter
@AllArgsConstructor
public enum MenuTypeEnum implements IBaseEnum<Integer> {

    NULL(0, null),
    CATALOG(1, "目录"),
    MENU(2, "菜单"),
    EXT_LINK(3, "外链"),
    BUTTON(4, "按钮");


    @EnumValue //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    @JsonValue //  表示对枚举序列化时返回此字段
    private final Integer value;

    private final String label;

}
