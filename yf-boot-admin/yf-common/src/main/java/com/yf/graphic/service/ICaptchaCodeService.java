package com.yf.graphic.service;


import com.yf.graphic.model.dto.CaptchaResult;
import com.yf.graphic.model.form.CaptchaCodeForm;

/**
 * 用户名验证码登陆
 *
 * @author yiFei
 * @since 2024/3/7 13:18
 */
public interface ICaptchaCodeService {
    /**
     * 生成验证码
     *
     * @return 验证码展示Vo
     */
    CaptchaResult generateVerifyCode();

    /**
     * @param captchaCodeForm 校验验证码
     */
    boolean checkVerifyCode(CaptchaCodeForm captchaCodeForm);
}
