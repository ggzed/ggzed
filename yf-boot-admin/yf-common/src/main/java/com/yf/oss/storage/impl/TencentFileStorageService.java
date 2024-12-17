package com.yf.oss.storage.impl;

import com.yf.oss.storage.FileStorageService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 腾讯oss文件存储
 *
 * @author : YiFei
 * @since : 2024/7/21 10:04
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "file.storage.type", havingValue = "tencent")
@ConfigurationProperties(prefix = "file.storage.tencent")
@RequiredArgsConstructor
@Data
public class TencentFileStorageService implements FileStorageService {
    @Override
    public String getFileStorageEndpoint() {
        return null;
    }

    @Override
    public String uploadFile(String savePath, MultipartFile file) {
        return null;
    }

    @Override
    public boolean deleteFile(String savePath, String fileName) {
        return false;
    }
}
