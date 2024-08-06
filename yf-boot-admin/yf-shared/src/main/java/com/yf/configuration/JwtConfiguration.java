package com.yf.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Jwt 配置类
 *
 * @author YiFei
 * @since 2024/4/16 16:58
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfiguration {
    /**
     * 生成 token 的前缀
     */
    private String tokenPrefix = "Bearer ";
    /**
     * 请求头存储 token 的 key
     */
    private String requestHeaderKey = HttpHeaders.AUTHORIZATION;
    /**
     * JWT 密匙
     */
    private String secretKey;
    /**
     * 默认 Token 过期时间（小时为单位）
     */
    private Duration accessTokenExpirationTime = Duration.ofHours(8);
    /**
     * 刷新 Token 过期时间（小时为单位）
     */
    private Duration refreshTokenExpirationTime = Duration.ofDays(3);
}
