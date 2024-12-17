package com.yf.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 系统配置
 *
 * @author yiFei
 * @since 2024/3/13 21:40
 */
@Data
@Component
@ConfigurationProperties(prefix = "system")
public class SystemConfiguration {
    /**
     * 机器名 : 用于部分组件在多台机器部署的唯一标识
     */
    private String machineName;
    /**
     * 忽略安全校验的请求 ( 不需要认证、不需要登录的请求 )
     */
    private String[] securityWhitelistPaths;
}
