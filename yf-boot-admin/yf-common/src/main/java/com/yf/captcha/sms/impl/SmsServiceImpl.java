package com.yf.captcha.sms.impl;

import com.yf.captcha.sms.ISmsService;
import com.yf.exception.ServiceException;
import com.yf.result.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 短信服务具体实现
 *
 * @author : YiFei
 * @since : 2025/6/13 9:24
 */
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements ISmsService {

    /**
     * 使用手机号发送验证码
     *
     * @param phone 需要送达的手机号
     */
    public void sendPhoneCode(String phone) {
        // 请参考 EmailServiceImpl 中的实现逻辑

        throw new ServiceException(ResultCode.PHONE_CODE_NOT_SUPPORTED);
    }

    /**
     * 校验手机号验证码
     *
     * @param phone     手机号
     * @param phoneCode 验证码
     * @return 是否校验成功
     */
    public boolean checkPhoneCode(String phone, String phoneCode) {
        return false; // 这里需要实现具体的校验逻辑
    }
}
