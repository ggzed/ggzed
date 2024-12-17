package com.yf.mail.service;

/**
 * 发送邮箱服务
 *
 * @author yiFei
 * @since 2024/3/8 12:56
 */
public interface IEmailService {

    /**
     * 使用邮箱发送验证码
     *
     * @param email 需要送达的邮箱
     */
    void sendEmailCode(String email);

    /**
     * 校验邮箱验证码
     *
     * @param email     邮箱
     * @param emailCode 验证码
     * @return 是否校验成功
     */
    boolean checkEmailCode(String email, String emailCode);
}
