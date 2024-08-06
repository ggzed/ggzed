package com.yf.storage.impl;

import com.qiniu.http.Response;
import com.qiniu.processing.OperationManager;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.yf.exception.ServiceException;
import com.yf.model.result.ResultCode;
import com.yf.storage.FileStorageService;
import com.yf.utils.FileUtils;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import static com.yf.utils.FileUtils.DIR_SEPARATOR;

@Slf4j
@Component
@ConditionalOnProperty(value = "file.storage.type", havingValue = "qiniu")
@ConfigurationProperties(prefix = "file.storage.qiniu")
@RequiredArgsConstructor
@Data
public class QiniuFileStorageService implements FileStorageService {

    /**
     * Qiniu Access Key
     */
    private String accessKey;

    /**
     * Qiniu Secret Key
     */
    private String secretKey;

    /**
     * Qiniu Bucket Name
     */
    private String bucketName;

    /**
     * 协议 + 域名
     */
    private String domain;

    /**
     * uploadManager 和 BucketManager 是 Qiniu SDK 中不同的功能模块，它们分别用于文件上传和管理 Bucket 中的文件。
     * 虽然可以在设计上尝试合并它们，但这样做可能会增加复杂性，并不会带来明显的性能或代码简化优势。
     * 您可自定义可能需要用到的 Manager 作为 QiniuFileStorageService 属性一部分以方便统一使用。
     */
    private UploadManager uploadManager;
    private BucketManager bucketManager;
    private OperationManager operationManager;
    private Auth auth;

    @PostConstruct
    public void init() {
        // 1. 初始化 Qiniu SDK 配置，使用 Qiniu 虚拟机 Region ( 可使用 createWithRegionId() 自行选择)
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        uploadManager = new UploadManager(cfg);
        // 2. 初始化认证信息
        auth = Auth.create(accessKey, secretKey);
        // 3. 初始化 BucketManager，用于管理 Bucket 相关操作
        bucketManager = new BucketManager(auth, cfg);
        // 4. 初始化 OperationManager，用于执行文件操作的高级管理
        operationManager = new OperationManager(auth, cfg);
        // 5. 初始化 bucket
        initBucket();
    }

    @Override
    public String getFileStorageEndpoint() {
        return domain;
    }

    @Override
    public String uploadFile(String savePath, MultipartFile file) {
        // 1. 生成文件上传路径
        String datePath = FileUtils.datePath() + file.getOriginalFilename();
        String objectName = StringUtils.hasText(savePath) ? savePath + DIR_SEPARATOR + datePath : datePath;
        try {
            // 2. 获取上传凭证
            String upToken = auth.uploadToken(bucketName);
            // 3. 上传文件到 Qiniu
            Response response = uploadManager.put(file.getInputStream(), objectName, upToken, null, file.getContentType());
            if (!response.isOK()) {
                throw new ServiceException(ResultCode.FILE_UPLOAD_ERROR);
            }
        } catch (Exception e) {
            log.error("Failed to upload file to Qiniu. Object: {}, Error: {}", objectName, e.getMessage(), e);
            throw new ServiceException(ResultCode.FILE_UPLOAD_ERROR);
        }
        return getFileStorageEndpoint() + "/" + objectName;
    }

    @Override
    public boolean deleteFile(String savePath, String fileName) {
        // 1. 拼接文件名
        String objectName = savePath + DIR_SEPARATOR + fileName;
        try {
            // 2. 删除文件
            Response delete = bucketManager.delete(bucketName, objectName);
            return delete.isOK();
        } catch (Exception e) {
            log.error("Failed to delete file to Qiniu. Object: {}, Error: {}", objectName, e.getMessage(), e);
            throw new ServiceException(ResultCode.FILE_DELETE_NOT_FOUND);
        }
    }

    private void initBucket() {
//        bucketManager.createBucket(bucketName,"z0");
        try {
            // 1.检查存储空间是否存在
            String[] buckets = bucketManager.buckets();
            boolean bucketExists = false;
            for (String bucket : buckets) {
                if (bucket.equals(bucketName)) {
                    bucketExists = true;
                    break;
                }
            }
            // 2. bucket 不存在则提示创建
            if (!bucketExists) {
                log.error("bucket :{} , 不存在", bucketName);
            }
        } catch (Exception e) {
            log.error("Failed to init buckets to Qiniu. Error: {}", e.getMessage(), e);
        }
    }
}