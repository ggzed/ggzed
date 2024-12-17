package com.yf.auth.security.filter;


import com.yf.auth.utils.JwtUtil;
import com.yf.configuration.SystemConfiguration;
import com.yf.constants.RedisKeyConstants;
import com.yf.model.system.dto.LoginResult;
import com.yf.result.ResultCode;
import com.yf.security.utils.SecurityUtil;
import com.yf.utils.RedisUtil;
import com.yf.utils.ResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Jwt 过滤器
 *
 * @author : YiFei
 * @since : 2023/9/24 19:52
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final SystemConfiguration systemConfiguration;
    private final ServerProperties properties;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.removeTokenPrefix(request);
        String uri = request.getRequestURI();
        String contextPath = properties.getServlet().getContextPath();
        if (StringUtils.hasText(contextPath)) {
            uri = uri.substring(contextPath.length());
        }
        // 1. 判断token是否存在, 判断是否在 security白名单中 ,不存在交给下一个责任链解决
        if (!SecurityUtil.isWhitelisted(uri, systemConfiguration.getSecurityWhitelistPaths())
                && StringUtils.hasText(token)) {
            // 2. 解析权限信息
            Authentication auth = jwtUtil.getAuthentication(token);
            if (auth == null) {
                // 2.1 解析权限信息失败
                if (jwtUtil.isJwtExpired(token)) {
                    // 2.1.1 token 过期处理
                    ResponseUtil.writeIResultCodeMsg(response, HttpStatus.UNAUTHORIZED, ResultCode.AUTH_TOKEN_EXPIRED);
                } else {
                    // 2.1.2 token 无效处理
                    ResponseUtil.writeIResultCodeMsg(response, HttpStatus.UNAUTHORIZED, ResultCode.AUTH_TOKEN_INVALID);
                }
                return;
            }
            // 3. 解析 auth 中用户id
            Long userId = SecurityUtil.getUserId(auth);
            if (userId == null) {
                // 3.1 解析用户id失败表示未授权
                ResponseUtil.writeIResultCodeMsg(response, HttpStatus.UNAUTHORIZED, ResultCode.AUTH_TOKEN_INVALID);
                return;
            }
            // 4. 获取 redis 中 token 是否与当前 token 匹配
            LoginResult loginResult = redisUtil.getCacheObject(RedisKeyConstants.USER_TOKEN_CACHE_PREFIX + userId);
            if (loginResult == null) {
                // 4.1 缓存获取中未存储 token , 表示用户被踢出
                ResponseUtil.writeIResultCodeMsg(response, HttpStatus.UNAUTHORIZED, ResultCode.AUTH_KICK_OUT);
                return;
            }
            if (!token.equals(loginResult.getAccessToken())) {
                // 4.2 token 不相等表示 用户在别处登录
                ResponseUtil.writeIResultCodeMsg(response, HttpStatus.FORBIDDEN, ResultCode.AUTH_USER_ELSEWHERE_LOGIN);
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
