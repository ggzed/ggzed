package com.yf.auth.security.service.impl;

import com.yf.auth.model.enums.LoginTypeEnum;
import com.yf.auth.model.form.RefreshTokenForm;
import com.yf.auth.security.service.IAuthService;
import com.yf.auth.utils.JwtUtil;
import com.yf.constants.RedisKeyConstants;
import com.yf.exception.ServiceException;
import com.yf.model.system.dto.LoginResult;
import com.yf.model.system.form.LoginForm;
import com.yf.result.ResultCode;
import com.yf.security.utils.SecurityUtil;
import com.yf.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * security服务类
 *
 * @author YiFei
 * @since 2024/4/16 16:36
 */
@Service("authService")
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * 本地登陆 + 第三方登录
     *
     * @param loginForm 登陆表单
     * @param type      登陆类型
     * @return LoginResult
     */
    @Override
    public LoginResult login(LoginForm loginForm, LoginTypeEnum type) {
        // 参数说明 : principal 主体 ，credentials 凭据
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginForm, type);
        // 1. 获取到 UserDetails 对象
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 2. 生成 Jwt Token;
        return jwtUtil.getLoginResult(authenticate);
    }

    /**
     * 刷新token
     *
     * @param refreshTokenForm 刷新token表单
     * @return LoginResult
     */
    @Override
    public LoginResult refreshToken(RefreshTokenForm refreshTokenForm) {
        // 1. 解析 accessToken 是否真正过期
        if (!jwtUtil.isJwtExpired(refreshTokenForm.getAccessToken())) {
            // 1.1 未过期刷新 token 未恶意刷新
            throw new ServiceException(ResultCode.AUTH_TOKEN_EXPIRED);
        }
        // 2. 解析 refreshTokenForm 中的用户id
        Long userId = jwtUtil.getRefreshTokenUserId(refreshTokenForm.getRefreshToken());
        if (userId == null) {
            // 2.1 userId 等于 null 表示 refreshToken 错误
            if (jwtUtil.isJwtExpired(refreshTokenForm.getRefreshToken())) {
                // 2.1.1 RefreshToken 过期
                throw new ServiceException(ResultCode.AUTH_TOKEN_EXPIRED);
            } else {
                // 2.1.2 错误的 RefreshToken
                throw new ServiceException(ResultCode.AUTH_TOKEN_EXPIRED);
            }
        }
        // 3. 校验是否和缓存中 refreshToken 一致
        LoginResult loginResult = redisUtil.getCacheObject(RedisKeyConstants.USER_TOKEN_CACHE_PREFIX + userId);
        if (loginResult == null) {
            // 3.1 表示 refreshToken 过期
            throw new ServiceException(ResultCode.AUTH_TOKEN_EXPIRED);
        }
        if (!refreshTokenForm.getAccessToken().equals(loginResult.getAccessToken()) ||
                !refreshTokenForm.getRefreshToken().equals(loginResult.getRefreshToken())) {
            // 3.2 如果 accessToken 和 refreshToken 有一个不一致 ， 表示恶意刷新 Token
            throw new ServiceException(ResultCode.AUTH_TOKEN_EXPIRED);
        }
        // 4. 返回 刷新后的token
        return jwtUtil.refreshToken(refreshTokenForm);
    }

    /**
     * 退出登陆
     *
     * @return 是否退出登陆成功
     */
    @Override
    public boolean logout() {
        Long userId = SecurityUtil.getUserId();
        // 删除 token
        boolean deletedToken = redisUtil.deleteObject(RedisKeyConstants.USER_TOKEN_CACHE_PREFIX + userId);
        // 删除 权限
        boolean deletedPermission = redisUtil.deleteObject(RedisKeyConstants.USER_PERMISSIONS_CACHE_PREFIX + userId);
        // 删除用户信息
        redisUtil.deleteObject(RedisKeyConstants.SYSTEM_ME_CACHE_PREFIX + userId);
        return deletedToken && deletedPermission;
    }
}
