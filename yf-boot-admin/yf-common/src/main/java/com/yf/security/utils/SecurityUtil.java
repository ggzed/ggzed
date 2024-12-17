package com.yf.security.utils;

import cn.hutool.core.util.StrUtil;
import com.yf.constants.SystemConstants;
import com.yf.security.model.dto.SysUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Security 上下文认证对象工具类
 *
 * @author YiFei
 * @since 2024/5/5 20:56
 */
public class SecurityUtil {

    // 创建AntPathMatcher对象用于匹配请求路径
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private SecurityUtil() {

    }

    /**
     * 获取Security中上下文中的对象
     *
     * @return SysUserDetails
     */
    public static SysUserDetails getUser() {
        // 从全局 SecurityContextHolder 中获取 Authentication 找到 SysUserDetails 进行返回
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof SysUserDetails) {
                return (SysUserDetails) authentication.getPrincipal();
            }
        }
        return null;
    }

    /**
     * 获取当前登录用户ID
     *
     * @return 登录信息
     */
    public static Long getUserId() {
        SysUserDetails user = getUser();
        if (user != null) {
            return user.getUserId();
        }
        return null;
    }

    /**
     * 获取当前登录用户名
     */
    public static String getUsername() {
        SysUserDetails user = getUser();
        if (user != null) {
            return user.getUsername();
        }
        return null;
    }

    /**
     * 获取当前登录用户ID
     *
     * @return 登录信息
     */
    public static Long getUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof SysUserDetails) {
            SysUserDetails userDetails = (SysUserDetails) authentication.getPrincipal();
            return userDetails.getUserId();
        }
        return null;
    }

    /**
     * 获取当前登录用户角色信息
     *
     * @return 角色信息
     */
    public static Set<String> getUserRoles() {
        Collection<? extends GrantedAuthority> authorities = getAuthentication().getAuthorities();
        if (authorities != null) {
            return authorities.stream().filter(item -> item.getAuthority().startsWith("ROLE_")).map(item -> StrUtil.removePrefix(item.getAuthority(), "ROLE_")).collect(Collectors.toSet());
        }
        return null;
    }

    /**
     * 获取 Security 上下文中 Authentication 对象
     *
     * @return Authentication 对象
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    // 判断请求路径是否在白名单中
    public static boolean isWhitelisted(String requestPath, String... whitelistPaths) {
        for (String pattern : whitelistPaths) {
            if (PATH_MATCHER.match(pattern, requestPath)) {
                return true; // 请求路径匹配白名单中的模式
            }
        }
        return false; // 请求路径不在白名单中
    }

    /**
     * @return 是否是管理员
     */
    public static boolean isAdmin() {
        Set<String> userRoles = getUserRoles();
        if (userRoles == null) {
            return false;
        }
        return userRoles.contains(SystemConstants.ADMIN_CODE);
    }
}
