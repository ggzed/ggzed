package com.yf.captcha.graphic.service.impl;

import cn.hutool.core.lang.UUID;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import com.yf.captcha.graphic.model.dto.CaptchaResult;
import com.yf.captcha.graphic.model.form.CaptchaCodeForm;
import com.yf.captcha.graphic.service.ICaptchaCodeService;
import com.yf.configuration.CaptchaConfiguration;
import com.yf.constants.RedisKeyConstants;
import com.yf.exception.ServiceException;
import com.yf.result.ResultCode;
import com.yf.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务层
 *
 * @author yiFei
 * @since 2024/3/7 13:31
 */
@Service
@RequiredArgsConstructor
public class CaptchaCodeServiceImpl implements ICaptchaCodeService {

    private final CaptchaConfiguration captchaConfiguration;
    private final RedisUtil redisUtil;

    /**
     * 生成验证码，并返回 VerifyCodeVo 对象
     *
     * @return VerifyCodeVo 包含生成的验证码信息
     */
    @Override
    public CaptchaResult generateVerifyCode() {
        // 1. 解析配置生成验证码对象
        Captcha captcha = parseCaptcha();

        // 2. 获取验证码结果文本
        String text = captcha.text();

        // 3. 将验证码转换为Base64格式
        String base64 = captcha.toBase64();

        // 4. 生成 redisKey 存储 redis
        String uuid = UUID.fastUUID().toString();
        redisUtil.setCacheObject(RedisKeyConstants.CAPTCHA_CODE_CACHE_PREFIX + uuid, text.toUpperCase(), captchaConfiguration.getTimeout(), TimeUnit.SECONDS);

        // 5. 创建 CaptchaResult 对象，包含生成的验证码信息
        return CaptchaResult.builder()
                .verifyCodeKey(uuid)        // 验证key
                .captchaImgBase64(base64)   // 验证码  base64
                .build();
    }

    /**
     * @param captchaCodeForm 校验验证码
     */
    @Override
    public boolean checkVerifyCode(CaptchaCodeForm captchaCodeForm) {
        // 1. 获取 redis 中验证码
        String code = redisUtil.getCacheObject(RedisKeyConstants.CAPTCHA_CODE_CACHE_PREFIX + captchaCodeForm.verifyCodeKey);
        if (!StringUtils.hasText(code)) {
            throw new ServiceException(ResultCode.AUTH_CODE_EXPIRED);
        }
        // 2. 对于部分验证码进行大小写转换
        switch (captchaConfiguration.getType()) {
            case ARITHMETIC:
            case GIF:
            case SPEC:
                code = code.toUpperCase();
                captchaCodeForm.setVerifyCode(captchaCodeForm.getVerifyCode().toUpperCase());
        }

        if (!captchaCodeForm.getVerifyCode().equals(code)) {
            throw new ServiceException(ResultCode.AUTH_CODE_ERROR);
        }

        return true;
    }

    /**
     * 解析验证码类型
     */
    private Captcha parseCaptcha() {
        int width = captchaConfiguration.getWidth();
        int height = captchaConfiguration.getHeight();
        int length = captchaConfiguration.getLength();

        // 创建字体对象
        Font font = new Font(captchaConfiguration.getFontName(), captchaConfiguration.getFontStyle(), captchaConfiguration.getFontSize());

        Captcha captcha;
        switch (captchaConfiguration.getType()) {
            case ARITHMETIC -> captcha = new ArithmeticCaptcha(width, height, length, font);
            case CHINESE -> captcha = new ChineseCaptcha(width, height, length, font);
            case CHINESE_GIF -> captcha = new ChineseGifCaptcha(width, height, length, font);
            case GIF -> captcha = new GifCaptcha(width, height, length, font);
            case SPEC -> captcha = new SpecCaptcha(width, height, length, font);
            // 默认为图形验证码 （预防调用者全部配置错误）
            default -> captcha = new GifCaptcha(width, height, length, font);
        }

        return captcha;
    }
}
