package com.yf.rate_limiting.utils;

import cn.hutool.core.lang.Snowflake;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 限流工具类
 *
 * @author YiFei
 * @since 2024/5/24 18:59
 */
@Component
@RequiredArgsConstructor
public class RateLimiterUtil {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisScript<Boolean> limitScript;
    private final Snowflake snowflake;

    /**
     * @param rateLimitRules rateLimitRules 为单个
     * @return 是否限流
     */
    public boolean rateLimiterSingle(RateLimitRule... rateLimitRules) {
        for (RateLimitRule rateLimitRule : rateLimitRules) {
            // Lua 脚本中的 keys
            List<String> keys = List.of(
                    rateLimitRule.getRedisKey(),
                    snowflake.nextIdStr(),
                    String.valueOf(System.currentTimeMillis())
            );
            // Lua 脚本中的 args
            List<Object> args = List.of(
                    rateLimitRule.getLimit(),
                    rateLimitRule.getTimeUnit().toMillis(rateLimitRule.getTimeDuration())
            );
            // 执行脚本
            Boolean isLimited = redisTemplate.execute(limitScript, keys, args);
            if (Boolean.TRUE.equals(isLimited)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param rateLimitRules rateLimitRules 是一组
     * @return 是否限流
     */
    public boolean rateLimiterGroup(String redisKey, RateLimitRule... rateLimitRules) {
        // Lua 脚本中的 keys
        List<String> keys = List.of(
                redisKey,
                snowflake.nextIdStr(),
                String.valueOf(System.currentTimeMillis())
        );
        // Lua 脚本中的 args
        Object[] args = new Object[rateLimitRules.length * 2];
        for (int i = 0; i < rateLimitRules.length; i++) {
            args[i * 2] = rateLimitRules[i].getLimit();
            args[i * 2 + 1] = rateLimitRules[i].getTimeUnit().toMillis(rateLimitRules[i].getTimeDuration());
        }
        // 执行脚本
        return Boolean.TRUE.equals(redisTemplate.execute(limitScript, keys, args));
    }

    /**
     * 规则类：定义限流规则
     */
    @Data
    @Builder
    public static class RateLimitRule {
        /**
         * Redis key
         */
        private String redisKey;
        /**
         * 访问限制次数
         */
        private Integer limit;
        /**
         * 限制时间
         */
        private long timeDuration;
        /**
         * 时间单位
         */
        private TimeUnit timeUnit;
    }
}
