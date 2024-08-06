package com.yf.security.filter;

import com.yf.constants.RedisKeyConstants;
import com.yf.utils.IpUtil;
import com.yf.utils.RedisUtil;
import com.yf.utils.SecurityUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

/**
 * 黑名单过滤 (  建议通过 Nginx 编写 lua 脚本对 ip 进行过滤 ， 简单高效 )
 *
 * @author YiFei
 * @since 2024/5/25 14:02
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BlacklistFilter extends OncePerRequestFilter {

    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        // 1. 拦截 IP : 系统庞大建议 Nginx 处理
        Set<String> blacklistIpCache = redisUtil.getCacheSet(RedisKeyConstants.BLACKLIST_IP_CACHE_PREFIX);
        String ipAddr = IpUtil.getIpAddr(request);
        if (blacklistIpCache.contains(ipAddr)) {
            log.info("黑名单IP被拦截 : {}", ipAddr);
            return;
        }
        // 2. 用户 ID 拦截
        Set<Long> blacklistUserIdCache = redisUtil.getCacheSet(RedisKeyConstants.BLACKLIST_USER_ID_CACHE_PREFIX);
        Long userId = SecurityUtil.getUserId();
        if (userId != null && blacklistUserIdCache.contains(userId)) {
            log.info("用户被拦截 : {}", userId);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
