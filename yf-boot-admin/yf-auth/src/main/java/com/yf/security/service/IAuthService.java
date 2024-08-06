package com.yf.security.service;

import com.yf.model.dto.LoginResult;
import com.yf.model.enums.LoginTypeEnum;
import com.yf.model.form.LoginForm;
import com.yf.model.form.RefreshTokenForm;

/**
 * security服务类
 *
 * @author YiFei
 * @since 2024/4/16 16:32
 */
public interface IAuthService {
    /**
     * 本地登陆
     *
     * @param loginForm 登陆表单
     * @param type      登陆类型
     * @return LoginResult
     */
    LoginResult login(LoginForm loginForm, LoginTypeEnum type);

    /**
     * 刷新token
     *
     * @param refreshTokenForm 刷新token表单
     * @return LoginResult
     */
    LoginResult refreshToken(RefreshTokenForm refreshTokenForm);

    /**
     * 退出登陆
     *
     * @return 是否退出登陆成功
     */
    boolean logout();
}
