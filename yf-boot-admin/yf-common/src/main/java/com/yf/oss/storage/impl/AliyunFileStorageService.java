package com.yf.oss.storage.impl;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.yf.exception.ServiceException;
import com.yf.oss.storage.FileStorageService;
import com.yf.oss.utils.FileUtils;
import com.yf.result.ResultCode;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import static com.yf.oss.utils.FileUtils.DIR_SEPARATOR;


/**
 * 阿里云oss TODO 未测试
 *
 * @author : YiFei
 * @since : 2024/7/21 10:02
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "file.storage.type", havingValue = "aliyun")
@ConfigurationProperties(prefix = "file.storage.aliyun")
@RequiredArgsConstructor
@Data
public class AliyunFileStorageService implements FileStorageService {
    /**
     * OSS服务的Endpoint
     * 示例值 : (https/http)://oss-cn-hangzhou.aliyuncs.com
     */
    private String endpoint;
    /**
     * Endpoint对应的Region信息
     * 示例值 : cn-hangzhou
     */
    private String region;
    /**
     * 访问阿里云OSS的Access Key ID
     */
    private String accessKeyId;
    /**
     * 访问阿里云OSS的Access Key Secret
     */
    private String secretAccessKey;
    /**
     * OSS存储桶的名称
     */
    private String bucketName;
    /**
     * OSS客户端实例
     */
    private OSS ossClient;

    @PostConstruct
    public void init() {
        // 1. 创建凭证
        DefaultCredentialProvider defaultCredentialProvider = CredentialsProviderFactory
                .newDefaultCredentialProvider(accessKeyId, secretAccessKey);
        // 2. 创建客户端配置，设置签名版本为V4 (可自行续写配置代码到属性)
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        // 3. 创建 OssClient
        ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(defaultCredentialProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();
        // 4. 初始化 initBucket
        initBucket();
    }

    @Override
    public String getFileStorageEndpoint() {
        return endpoint;
    }

    @Override
    public String uploadFile(String savePath, MultipartFile file) {
        // 1. 生成文件上传路径
        String datePath = FileUtils.datePath() + file.getOriginalFilename();
        String objectName = StringUtils.hasText(savePath) ? savePath + DIR_SEPARATOR + datePath : datePath;
        try {
            // 2. 上传文件
            ossClient.putObject(bucketName, objectName, file.getInputStream());
            return endpoint + "/" + objectName;
        } catch (Exception e) {
            log.error("Failed to upload file to Aliyun-oss. Object: {}, Error: {}", objectName, e.getMessage(), e);
            throw new ServiceException(ResultCode.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    public boolean deleteFile(String savePath, String fileName) {
        // 1. 拼接文件名
        String objectName = savePath + DIR_SEPARATOR + fileName;
        try {
            // 2. 删除文件
            ossClient.deleteObject(bucketName, objectName);
            return true;
        } catch (Exception e) {
            log.error("Failed to delete file to Aliyun. Object: {}, Error: {}", objectName, e.getMessage(), e);
            throw new ServiceException(ResultCode.FILE_DELETE_ERROR);
        }
    }

    private void initBucket() {
        // 检查存储空间是否存在，如果不存在则创建
        if (!ossClient.doesBucketExist(bucketName)) {
            // 创建存储空间。
            ossClient.createBucket(bucketName);
            log.info("aliyun.oss bucket {} 自动创建完成，", bucketName);
        }
    }
}
