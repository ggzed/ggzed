package com.yf.constants;

/**
 * redis key
 *
 * @author YiFei
 * @since 2024/4/15 14:38
 */
public interface RedisKeyConstants {
    /**
     * 限流前缀
     */
    String RATE_LIMIT_CACHE_PREFIX = "RATE:LIMIT:";
    /**
     * 缓存黑名单IP列表前缀
     */
    String BLACKLIST_IP_CACHE_PREFIX = "BLACKLIST:IP:CACHE";
    /**
     * 缓存黑名单USER_ID列表前缀
     */
    String BLACKLIST_USER_ID_CACHE_PREFIX = "BLACKLIST:USER_ID:CACHE";
    /**
     * 防重复提交前缀
     */
    String PREVENT_DUPLICATE_SUBMIT_PREFIX = "DUPLICATE:SUBMIT:";
    /**
     * 用户 permissions 信息
     */
    String USER_PERMISSIONS_CACHE_PREFIX = "USER:PERMISSIONS:";
    /**
     * 用户 Token 信息
     */
    String USER_TOKEN_CACHE_PREFIX = "USER:TOKEN:";
    /**
     * 系统 routes 存储存储前缀
     */
    String SYSTEM_ROUTE_CACHE_PREFIX = "SYSTEM:ROUTE:";
    /**
     * 系统 routes 存储存储前缀
     */
    String SYSTEM_ME_CACHE_PREFIX = "SYSTEM:ME:";
    /**
     * 普通验证码前缀
     */
    String CAPTCHA_CODE_CACHE_PREFIX = "AUTH:CAPTCHA:CODE:";
    /**
     * 邮箱验证码前缀
     */
    String EMAIL_CODE_CACHE_PREFIX = "AUTH:EMAIL:CODE:";
    /**
     * 系统全部机器ID集合
     */
    String SYSTEM_MACHINE = "SYSTEM:MACHINE";
}
