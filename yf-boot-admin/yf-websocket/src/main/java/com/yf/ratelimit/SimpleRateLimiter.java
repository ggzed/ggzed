package com.yf.ratelimit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简易的限流工具，用于限制在指定时间窗口内的请求数量。
 *
 * @author YiFei
 * @since 2024/5/24 21:26
 */
public class SimpleRateLimiter {

    // 用于跟踪每个键（例如用户ID）的请求计数
    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();

    // 用于跟踪每个键在当前窗口的首次请求时间戳
    private final ConcurrentHashMap<String, Long> requestTimestamps = new ConcurrentHashMap<>();

    // 在时间窗口内允许的最大请求数
    private final int limit;

    // 时间窗口的持续时间（以毫秒为单位）
    private final long durationInMillis;

    /**
     * 构造一个 SimpleRateLimiter，指定限流的请求数和时间窗口。
     *
     * @param limit            在时间窗口内允许的最大请求数
     * @param durationInMillis 时间窗口的持续时间（以毫秒为单位）
     */
    public SimpleRateLimiter(int limit, long durationInMillis) {
        this.limit = limit;
        this.durationInMillis = durationInMillis;
    }

    /**
     * 尝试为指定的键获取请求许可。
     *
     * @param key 表示被限流的实体的键（例如用户ID : 使用 string 支持多个）
     * @return 如果请求被允许，返回 true；如果请求数超过限制，返回 false
     */
    public boolean tryAcquire(String key) {
        long currentTime = System.currentTimeMillis();

        requestCounts.computeIfAbsent(key, k -> new AtomicInteger(0));
        requestTimestamps.putIfAbsent(key, currentTime);

        long elapsedTime = currentTime - requestTimestamps.get(key);
        if (elapsedTime > durationInMillis) {
            requestCounts.get(key).set(0);
            requestTimestamps.put(key, currentTime);
        }

        return requestCounts.get(key).incrementAndGet() <= limit;
    }

    /**
     * 清除缓存内容
     *
     * @param key 表示被限流的实体的键（例如用户ID : 使用 string 支持多个）
     */
    public void cleanUp(String key) {
        requestCounts.remove(key);
        requestTimestamps.remove(key);
    }
}
