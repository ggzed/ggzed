package com.yf.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yf.base.IBaseEnum;
import lombok.Getter;

/**
 * 记录日志操作类型
 *
 * @author YiFei
 * @since 2024/4/15 14:40
 */
@Getter
public enum BusinessTypeEnum implements IBaseEnum<Integer> {

    OTHER(0, "其他"),
    INSERT(1, "新增"),
    DELETE(2, "删除"),
    UPDATE(3, "修改"),
    SEARCH(4, "查询"),
    EXPORT(5, "导出"),
    IMPORT(6, "导入"),
    UPLOAD(7, "上传文件"),
    LOGIN(8, "登录"),
    LOGOUT(9, "登出"),
    KICK_OUT(10, "踢出"),
    CHAT_ROOM(11, "聊天室"),
    TEST(999, "测试");


    @EnumValue //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    @JsonValue //  表示对枚举序列化时返回此字段
    private final Integer value;

    private final String label;

    BusinessTypeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
