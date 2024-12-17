package com.yf.oss.utils;

import cn.hutool.core.lang.UUID;
import com.yf.oss.model.dto.CustomMultipartFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 文件操作工具类
 *
 * @author : YiFei
 * @since : 2024/7/6 15:40
 */
@Slf4j
public class FileUtils {
    // 文件夹分隔符
    public static final String DIR_SEPARATOR = "/";

    // 日期格式化器，用于生成日期路径 "yyyy/MM/dd/"
    public static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy/MM/dd/");

    /**
     * 判断文件后缀是否在指定的禁止扩展名数组中。
     *
     * @param file             要检查的文件
     * @param allowedExtension 允许的文件扩展名数组
     * @return 是否禁止上传
     */
    public static boolean isAllowedExtension(MultipartFile file, String[] allowedExtension) {
        // 1. 获取文件后缀
        String extension = getFileExtension(file);
        // 2. 判断后缀是否在 disallowedExtension 中 （返回true表示扩展名不通过）
        for (String ext : allowedExtension) {
            if (extension.equalsIgnoreCase(ext)) {
                return true; // 找到禁止的扩展名，返回 true
            }
        }
        return false; // 文件扩展名不在禁止列表中，返回 false
    }

    /**
     * 从文件名中提取文件扩展名。
     *
     * @param file 要获取扩展名的文件
     * @return 文件的扩展名（不包括点），如果找不到扩展名则返回空字符串。
     */
    public static String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
                return originalFilename.substring(dotIndex + 1).toLowerCase();
            }
        }
        return "";
    }

    /**
     * 更改文件名，并返回更名后的文件对象。
     *
     * @param file 要更名的文件
     * @return 更名后的文件对象
     */
    public static MultipartFile renameFile(MultipartFile file) {
        return new CustomMultipartFile(file, UUID.fastUUID() + "." + getFileExtension(file));
    }

    /**
     * 更改文件名，并返回更名后的文件对象。
     *
     * @param file     要更名的文件
     * @param fileName 新的文件名
     * @return 更名后的文件对象
     */
    public static MultipartFile renameFile(MultipartFile file, String fileName) {
        return new CustomMultipartFile(file, fileName);
    }

    public static String parseFileSavePath(String fileHttpUrl) {
        return parseFileSavePath("", fileHttpUrl);
    }

    /**
     * 通过HTTP连接找寻文件路径
     *
     * @param start       起始路径段，可以为空
     * @param fileHttpUrl 文件的HTTP URL
     * @return 文件保存路径，从起始路径段开始到文件名前的路径
     */
    public static String parseFileSavePath(String start, String fileHttpUrl) {
        // 1. 解析出 uri 部分
        int startIndex = fileHttpUrl.indexOf("/", fileHttpUrl.indexOf("//") + 2);
        String uri = startIndex != -1 ? fileHttpUrl.substring(startIndex) : "";

        // 2. 找寻第一次出现 start 的地方
        int index = uri.indexOf(start);
        if (index == -1) {
            // 如果找不到 start，则直接使用整个 uri
            index = 0;
        }

        // 3. 裁剪最后一个 '/' 以后的位置
        int last = uri.lastIndexOf("/");

        return uri.substring(index, last);

    }


    /**
     * 返回当前日期对应的日期路径，格式为 FORMATTER
     *
     * @return 当前日期的日期路径
     */
    public static String datePath() {
        return LocalDate.now().format(FORMATTER);
    }

}
