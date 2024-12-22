package com.yf.captcha.graphic.model.enums;

/**
 * 验证码类型
 *
 * @author yiFei
 * @since 2024/3/6 21:19
 */
public enum CaptchaType {

    /**
     * 算术验证码类型，用于生成包含算术运算的验证码。
     */
    ARITHMETIC,

    /**
     * 中文验证码类型，用于生成包含中文字符的验证码。
     */
    CHINESE,

    /**
     * 中文GIF验证码类型，用于生成中文字符的动态GIF验证码。
     */
    CHINESE_GIF,

    /**
     * GIF验证码类型，用于生成动态GIF格式的验证码。
     */
    GIF,

    /**
     * 自定义特殊验证码类型，用于生成特殊类型的验证码，具体形式由业务自定义。
     */
    SPEC
}
