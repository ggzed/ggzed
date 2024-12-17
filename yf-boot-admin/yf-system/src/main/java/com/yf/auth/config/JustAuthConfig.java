package com.yf.auth.config;

import com.yf.configuration.JustAuthConfiguration;
import com.yf.justauth.cache.RedisStateCache;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.cache.AuthDefaultStateCache;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Just配置
 *
 * @author YiFei
 * @since 2024/4/17 19:03
 */
@Slf4j
@Configuration
public class JustAuthConfig {

    /**
     * 默认缓存
     * ConditionalOnMissingBean : 当 Bean 中不存在 AuthStateCache.class 加载该配置
     * just-auth.cache.type : 当等于 default 加载该配置，
     */
    @Bean
    @ConditionalOnMissingBean(AuthStateCache.class)
    @ConditionalOnProperty(name = "just-auth.cache.type", havingValue = "default", matchIfMissing = true)
    public AuthStateCache defaultAuthStateCache() {
        log.info("JustAuth 采用 default 进行缓存");
        return AuthDefaultStateCache.INSTANCE;
    }

    /**
     * Redis 缓存
     * 当 RedisTemplate 类在类路径中，并且当前没有AuthStateCache的bean被定义，以及just-auth.cache.type属性被设置为redis时，使用此Bean。
     */
    @Bean
    @DependsOn("redisTemplate")
    @ConditionalOnClass(RedisTemplate.class)
    @ConditionalOnMissingBean(AuthStateCache.class)
    @ConditionalOnProperty(name = "just-auth.cache.type", havingValue = "redis")
    public AuthStateCache redisAuthStateCache(RedisTemplate<String, Object> redisTemplate, JustAuthConfiguration justAuthConfiguration) {
        log.info("JustAuth 采用 redis 进行缓存");
        return new RedisStateCache(redisTemplate, justAuthConfiguration);
    }
}
