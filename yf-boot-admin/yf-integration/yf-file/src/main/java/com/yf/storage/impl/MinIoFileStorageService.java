package com.yf.storage.impl;

import com.yf.exception.ServiceException;
import com.yf.model.result.ResultCode;
import com.yf.storage.FileStorageService;
import com.yf.utils.FileUtils;
import io.minio.*;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

import static com.yf.utils.FileUtils.DIR_SEPARATOR;
// @formatter:off
/**
 * Minio文件存储
 * 简易的 Docker 启动 Minio 命令 :
  docker run -p 9000:9000 -p 9090:9090 --name minio --restart=always -d \
  -e "MINIO_ACCESS_KEY=minioadmin" \
  -e "MINIO_SECRET_KEY=minioadmin" \
  -v /path/to/local/data:/data \
  -v /usr/local/minio/config:/root/.minio \
  minio/minio server /data --console-address :9090 --address :9000
 *
 * @author : YiFei
 * @since : 2024/7/16 18:21
 */
// @formatter:on
@Slf4j
@Component
@ConditionalOnProperty(value = "file.storage.type", havingValue = "minio")
@ConfigurationProperties(prefix = "file.storage.minio")
@RequiredArgsConstructor
@Data
public class MinIoFileStorageService implements FileStorageService {
    /**
     * 服务协议
     */
    private String protocol = "http";
    /**
     * MinIO 主机地址
     */
    private String host = "localhost";
    /**
     * MinIO 端口
     */
    private int port = 9000;
    /**
     * 访问凭据
     */
    private String accessKey = "minioadmin";
    /**
     * 凭据密钥
     */
    private String secretKey = "minioadmin";
    /**
     * 服务名
     */
    @Value("${spring.application.name}")
    private String applicationName;
    /**
     * 存储桶名称
     */
    private String bucketName;

    private MinioClient minioClient;


    @PostConstruct
    public void init() {
        // 1. 初始化 minioClient
        minioClient = MinioClient.builder()
                .endpoint(this.getFileStorageEndpoint())
                .credentials(accessKey, secretKey)
                .build();

        // 2. 初始化 bucket
        initBucket();
    }

    @Override
    public String getFileStorageEndpoint() {
        return protocol + "://" + host + ":" + port;
    }

    /**
     * 上传单个文件
     *
     * @param savePath 文件存放路径 (savePath不能以 "/" 开头，请求时校验) (编写时请注意 savePath 可能为空)
     * @param file     文件
     * @return 文件上传后的访问路径
     */
    @Override
    public String uploadFile(String savePath, MultipartFile file) {
        // 1. 生成文件上传路径
        String datePath = FileUtils.datePath() + file.getOriginalFilename();
        String objectName = StringUtils.hasText(savePath) ? savePath + DIR_SEPARATOR + datePath : datePath;

        try {
            HashMap<String, String> tags = new HashMap<>();
            tags.put("server", applicationName);
            // 2. 上传文件到minio
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .tags(tags)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        } catch (Exception e) {
            log.error("Failed to upload file to MinIO. Object: {}, Error: {}", objectName, e.getMessage(), e);
            throw new ServiceException(ResultCode.FILE_DELETE_ERROR);
        }

        // 3. 生成上传文件的路径
        return getFileStorageEndpoint() + "/" + bucketName + "/" + objectName;
    }

    /**
     * 删除单个文件
     *
     * @param savePath 保存路径 ( xx/xx )
     * @param fileName 文件访问路径 （ xx.jpg ）
     * @return 是否删除成功，注: 不报错则返回 true
     */
    @Override
    public boolean deleteFile(String savePath, String fileName) {
        // 1. 拼接文件名
        String objectName = savePath + DIR_SEPARATOR + fileName;
        try {
            // 2. 删除文件
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("Failed to delete file to MinIO. Object: {}, Error: {}", objectName, e.getMessage(), e);
            throw new ServiceException(ResultCode.FILE_DELETE_ERROR);
        }
    }

    /**
     * 初始化 bucket
     */
    private void initBucket() {
        try {
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("minio bucket {} 自动创建完成, 如果文件可以外部访问请设置bucket为public", bucketName);
            }
        } catch (Exception e) {
            log.error("MinioFileStorageService -> @PostConstruct.init() -> initBucket", e);
        }
    }
}
