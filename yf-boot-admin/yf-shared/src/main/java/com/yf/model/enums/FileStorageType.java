package com.yf.model.enums;

/**
 * 文件存储服务器类型
 *
 * @author : YiFei
 * @since : 2024/7/16 21:57
 */
public enum FileStorageType {
    /**
     * 本地存储
     */
    LOCAL,
    /**
     * Minio 存储
     */
    MINIO,
    /**
     * aliyun存储
     */
    ALIYUN,
    /**
     * 七牛云存储
     */
    QINIU,
    /**
     * 腾讯云存储
     */
    TENCENT,
    /**
     * 华为云存储
     */
    HUAWEI
}
