package com.yf.captcha.sms;

/**
 * @author : YiFei
 * @since : 2025/6/13 9:24
 */
public interface ISmsService {

    /**
     * 使用手机号发送验证码
     *
     * @param phone 需要送达的手机号
     */
    void sendPhoneCode(String phone);

    /**
     * 校验手机号验证码
     *
     * @param phone     手机号
     * @param phoneCode 验证码
     * @return 是否校验成功
     */
    boolean checkPhoneCode(String phone, String phoneCode);

}
