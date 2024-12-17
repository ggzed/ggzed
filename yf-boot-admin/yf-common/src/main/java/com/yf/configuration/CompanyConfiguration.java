package com.yf.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 公司信息配置
 *
 * @author YiFei
 * @since 2024/4/16 17:48
 */
@Data
@Component
@ConfigurationProperties(prefix = "company")
public class CompanyConfiguration {
    /**
     * 公司名
     */
    private String name;
    /**
     * 公司邮箱
     */
    private String email;
    /**
     * 公司官网
     */
    private String website;
}
