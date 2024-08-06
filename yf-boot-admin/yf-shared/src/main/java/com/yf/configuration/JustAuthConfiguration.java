package com.yf.configuration;

import com.yf.model.dto.AuthCacheProperties;
import lombok.Data;
import me.zhyd.oauth.config.AuthConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * JustAuthConfiguration
 *
 * @author YiFei
 * @since 2024/4/16 22:29
 */
@Data
@Component
@ConfigurationProperties(prefix = "just-auth")
public class JustAuthConfiguration {

    private Map<String, AuthConfig> type = new HashMap<>();

    /**
     * 缓存配置类
     */
    @NestedConfigurationProperty
    private AuthCacheProperties cache = new AuthCacheProperties();
}


