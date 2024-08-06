package com.yf.model.enums;

import cn.hutool.core.util.DesensitizedUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

/**
 * 数据脱敏枚举
 *
 * @author YiFei
 * @since 2024/3/6 13:02
 */
@AllArgsConstructor
@Getter
public enum DesensitizationTypeEnum {

    /**
     * 自定义
     */
    CUSTOMIZE_RULE(str-> str),

    /**
     *  中文名
     */
     CHINESE_NAME(str->DesensitizedUtil.chineseName(String.valueOf(str))),
    /**
     * 身份证号
     */
     ID_CARD(str->DesensitizedUtil.idCardNum(String.valueOf(str), 1, 2)),

    /**
     *  座机号
     */
     FIXED_PHONE(str->DesensitizedUtil.fixedPhone(String.valueOf(str))),

    /**
     * 手机号
     */
     MOBILE_PHONE(str->DesensitizedUtil.fixedPhone(String.valueOf(str))),
    /**
     * 地址
     */
     ADDRESS(str-> DesensitizedUtil.address(String.valueOf(str),8)),

    /**
     * 电子邮件
     */
     EMAIL(str->DesensitizedUtil.email(String.valueOf(str))),

    /**
     * 密码
     */
     PASSWORD(str->DesensitizedUtil.password(String.valueOf(str))),
    /**
     * 中国大陆车牌，包含普通车辆、新能源车辆
     */
     CAR_LICENSE(str->DesensitizedUtil.carLicense(String.valueOf(str))),

    /**
     * 银行卡
     */
    BANK_CARD(str->DesensitizedUtil.bankCard(String.valueOf(str)));


    private final Function<String,String> serialize;

}
