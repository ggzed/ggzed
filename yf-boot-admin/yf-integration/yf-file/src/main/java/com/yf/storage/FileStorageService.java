package com.yf.storage;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public interface FileStorageService {
    /**
     * 获取文件存储系统所在端点
     */
    String getFileStorageEndpoint();

    /**
     * 上传单个文件
     *
     * @param savePath 文件存放路径 (savePath不能以 "/" 开头，请求时校验) (编写时请注意 savePath 可能为空)
     * @param file     文件
     * @return 文件上传后的访问路径
     */
    String uploadFile(String savePath, MultipartFile file);

    /**
     * 删除单个文件
     *
     * @param savePath 文件存储路径
     * @param fileName 文件名
     * @return 是否删除成功，注: 不报错则返回 true
     */
    boolean deleteFile(String savePath, String fileName);

    /**
     * 删除多个文件
     *
     * @param savePath  存储路径
     * @param fileNames 文件名集合
     * @return 是否删除成功，注: 不报错则返回 true
     */
    default boolean deleteFiles(String savePath, String[] fileNames) {
        return Arrays.stream(fileNames).allMatch(fileName -> this.deleteFile(savePath, fileName));
    }

    /**
     * 删除单个文件
     *
     * @param fileName 文件名
     * @return 是否删除成功，注: 不报错则返回 true
     */
    default boolean deleteFile(String fileName) {
        return deleteFile("", fileName);
    }

    /**
     * 删除多个文件
     *
     * @param fileNames 文件名集合
     * @return 是否删除成功，注: 不报错则返回 true
     */
    default boolean deleteFiles(String[] fileNames) {
        return Arrays.stream(fileNames).allMatch(this::deleteFile);
    }

    /**
     * 上传单个文件
     *
     * @param file 文件
     * @return 文件上传后的访问路径
     */
    default String uploadFile(MultipartFile file) {
        return uploadFile("", file);
    }

    /**
     * 上传多个文件（使用 FileStorageConfig 的 allowedExtension）
     *
     * @param savePath 文件存放路径
     * @param files    文件集合
     * @return 文件上传后的访问路径
     */
    default String[] uploadFiles(String savePath, MultipartFile[] files) {
        return Arrays.stream(files).map(file -> this.uploadFile(savePath, file)).toArray(String[]::new);
    }

    /**
     * 上传多个文件
     *
     * @param files 文件集合
     * @return 文件上传后的访问路径
     */
    default String[] uploadFiles(MultipartFile[] files) {
        return Arrays.stream(files).map(this::uploadFile).toArray(String[]::new);
    }

}

