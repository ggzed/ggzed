package com.yf.file.model.dto;

import lombok.Data;

/**
 * Resources集合构建
 *
 * @author : YiFei
 * @since : 2025/4/9 11:22
 */
@Data
public class ResourcesFile {
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 父文件路径
     */
    private String parentFilePath;
    /**
     * 文件路径
     */
    private String fileName;
    /**
     * 是否为目录
     */
    private Boolean isFile;

}
