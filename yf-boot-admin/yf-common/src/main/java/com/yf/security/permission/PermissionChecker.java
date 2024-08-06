package com.yf.security.permission;

import com.yf.constants.RedisKeyConstants;
import com.yf.utils.RedisUtil;
import com.yf.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

/**
 * 权限检查器
 *
 * @author YiFei
 * @since 2024/5/10 12:43
 */
@Component("permission")
@RequiredArgsConstructor
public class PermissionChecker {

    private final RedisUtil redisUtil;

    /**
     * 权限校验
     *
     * @param permissions 资源名
     * @return 是否拥有权限
     */
    public boolean checker(String... permissions) {
        // 1. 管理员不进行校验
        if (SecurityUtil.isAdmin()) {
            return true;
        }
        // 2. 获取当前用户 Id
        Long userId = SecurityUtil.getUserId();
        if (userId == null) {
            return false;
        }
        // 3. 校验
        return checker(userId, permissions);
    }

    /**
     * 权限校验
     *
     * @param userId      用户ID
     * @param permissions 资源名
     * @return 是否拥有权限
     */
    public boolean checker(Long userId, String... permissions) {
        if (permissions.length == 0) {
            return false;
        }
        // 1. 获取缓存中 permission 值
        Set<String> permissionsCache = redisUtil.getCacheObject(RedisKeyConstants.USER_PERMISSIONS_CACHE_PREFIX + userId);
        // 2. 返回缓存中是否包含 permission 值
        return permissionsCache.containsAll(Arrays.asList(permissions));
    }
}
