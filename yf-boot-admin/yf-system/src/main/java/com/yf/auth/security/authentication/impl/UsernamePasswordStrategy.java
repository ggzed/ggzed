package com.yf.auth.security.authentication.impl;

import com.yf.auth.model.enums.LoginTypeEnum;
import com.yf.auth.security.authentication.ILoginProcessStrategy;
import com.yf.captcha.graphic.model.form.CaptchaCodeForm;
import com.yf.captcha.graphic.service.ICaptchaCodeService;
import com.yf.exception.ServiceException;
import com.yf.model.common.dto.UserAuthInfo;
import com.yf.model.system.form.LoginForm;
import com.yf.result.ResultCode;
import com.yf.security.model.dto.SysUserDetails;
import com.yf.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 用户密码登录
 *
 * @author YiFei
 * @since 2024/3/6 18:56
 */
@Component
@RequiredArgsConstructor
public class UsernamePasswordStrategy implements ILoginProcessStrategy {

    private final ICaptchaCodeService captchaCodeService;
    private final ISysUserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 支持 USERNAME_PASSWORD
     */
    @Override
    public LoginTypeEnum getLoginTypeSupport() {
        return LoginTypeEnum.USERNAME_PASSWORD;
    }

    /**
     * 校验登录参数
     *
     * @param principal 主体
     */
    @Override
    public boolean validateParameters(LoginForm principal) {
        String username = principal.getUsername();
        String password = principal.getPassword();
        String verifyCode = principal.getVerifyCode();
        String verifyCodeKey = principal.getVerifyCodeKey();
        // 1. 校验用户名
        if (username == null || username.length() < 4 || username.length() > 24) {
            return false;
        }
        // 2. 校验密码
        if (password == null || password.length() < 8 || password.length() > 16) {
            return false;
        }
        // 3. 校验验证码
        if (verifyCode == null || verifyCodeKey == null) {
            return false;
        }
        // 4. 校验缓存验证码
        if (!captchaCodeService.checkVerifyCode(CaptchaCodeForm.builder()
                .verifyCode(verifyCode)         // code
                .verifyCodeKey(verifyCodeKey)   // code key
                .build())) {
            throw new ServiceException(ResultCode.AUTH_CODE_ERROR);
        }
        return true;
    }

    /**
     * 获取用户信息
     */
    @Override
    public UserDetails getUserDetailsByPrincipal(LoginForm principal) {
        // 1. 查询权限信息
        UserAuthInfo userAuthInfo = userService.getUserAuthInfo(principal);
        // 2. 构建 SysUserDetails 信息
        return new SysUserDetails(userAuthInfo);
    }

    /**
     * 后置校验用户信息 : 校验密码是否正确
     */
    @Override
    public boolean validatePostParameters(LoginForm principal, UserDetails userDetails) {
        // 校验密码是否正确
        return passwordEncoder.matches(principal.getPassword(), userDetails.getPassword());
    }

}


