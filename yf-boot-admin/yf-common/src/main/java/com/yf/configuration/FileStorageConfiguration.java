package com.yf.configuration;

import com.yf.oss.model.enums.FileStorageType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件存储加载
 *
 * @author : YiFei
 * @since : 2024/7/16 19:18
 */
@Data
@Component
@ConfigurationProperties(prefix = "file.storage")
public class FileStorageConfiguration {

    /**
     * 文件存储服务器类型
     */
    private FileStorageType type;

}
