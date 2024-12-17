package com.yf.auth.security.service;

import com.yf.auth.model.enums.LoginTypeEnum;
import com.yf.auth.model.form.RefreshTokenForm;
import com.yf.model.system.dto.LoginResult;
import com.yf.model.system.form.LoginForm;

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
