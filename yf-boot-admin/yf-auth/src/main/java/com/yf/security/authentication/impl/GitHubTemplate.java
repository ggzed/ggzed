package com.yf.security.authentication.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.yf.exception.ServiceException;
import com.yf.justauth.factory.JustAuthFactory;
import com.yf.model.dto.SysUserDetails;
import com.yf.model.dto.UserAuthInfo;
import com.yf.model.enums.LoginTypeEnum;
import com.yf.model.form.LoginForm;
import com.yf.model.form.OauthForm;
import com.yf.model.result.ResultCode;
import com.yf.security.authentication.ILoginProcessTemplate;
import com.yf.service.ISysOauthService;
import com.yf.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthDefaultSource;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * GitHub登陆
 *
 * @author YiFei
 * @since 2024/4/16 18:56
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GitHubTemplate implements ILoginProcessTemplate {

    private final JustAuthFactory justAuthFactory;
    private final ISysOauthService oauthService;
    private final Cache<String, String> shortLivedCache;
    private final ISysUserService userService;

    @Override
    public LoginTypeEnum getLoginTypeSupport() {
        return LoginTypeEnum.GITHUB;
    }

    @Override
    public boolean registeredUsers(LoginForm principal) {
        AuthRequest authRequest = justAuthFactory.getAuthRequest(AuthDefaultSource.GITHUB);
        AuthResponse<AuthUser> login = authRequest.login(principal.getOauth());
        AuthUser authUser = login.getData();
        if (authUser == null) {
            log.debug("Third-party authorization failed , GitHubTemplate , oauth: {}", principal.getOauth());
            return false;
        }
        Long userId = oauthService.autoRegisterOauthInfo(OauthForm.builder()
                .platformName(authUser.getSource())
                .platformUserId(authUser.getUuid())
                .platformUserAvatar(authUser.getAvatar())
                .platformUsername(authUser.getUsername())
                .build());
        // 3. userId 存储到缓存中
        shortLivedCache.put(principal.getOauth().getCode(), userId.toString());
        return true;
    }

    @Override
    public boolean validateParameters(LoginForm principal) {
        AuthCallback oauth = principal.getOauth();
        return StringUtils.hasText(oauth.getCode());
    }

    @Override
    public UserDetails getUserDetailsByPrincipal(LoginForm principal) {
        // 1. 获取缓存中的认证信息
        String cacheUserId = shortLivedCache.getIfPresent(principal.getOauth().getCode());
        if (!StringUtils.hasText(cacheUserId)) {
            throw new ServiceException(ResultCode.AUTH_LOGIN_TIMEOUT);
        }
        // 2. 查询权限信息
        UserAuthInfo userAuthInfo = userService.getUserAuthInfo(LoginForm.builder()
                .userId(cacheUserId)
                .build());
        // 3. 构建 SysUserDetails 信息
        return new SysUserDetails(userAuthInfo);
    }

    @Override
    public boolean validatePostParameters(LoginForm principal, UserDetails userDetails) {
        // 从缓存中删除短期认证信息
        String oauthCode = principal.getOauth().getCode();
        if (StringUtils.hasText(oauthCode)) {
            // 记录日志
            log.debug("GitHubTemplate , Invalidating cache for OAuth code: {}", oauthCode);
            // 删除缓存中的用户ID
            shortLivedCache.invalidate(oauthCode);
        } else {
            log.warn("GitHubTemplate , OAuth code is empty or null for principal: {}", principal);
        }
        return ILoginProcessTemplate.super.validatePostParameters(principal, userDetails);
    }
}

