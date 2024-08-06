package com.yf.constants;

/**
 * 媒体类型常量
 *
 * @author yiFei
 * @since 2023/11/20 12:36
 */
public interface MimeTypeConstants {
    String[] IMAGE_EXTENSION = {"bmp", "gif", "jpg", "jpeg", "png"};
    String[] FLASH_EXTENSION = {"swf", "flv"};
    String[] MEDIA_EXTENSION = {"swf", "flv", "mp3", "wav", "wma", "wmv", "mid", "avi", "mpg",
            "asf", "rm", "rmvb"};
    String[] VIDEO_EXTENSION = {"mp4", "avi", "rmvb"};
    String[] DEFAULT_ALLOWED_EXTENSION = {
            // 图片
            "bmp", "gif", "jpg", "jpeg", "png",
            // word excel powerpoint
            "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt",
            // 压缩文件
            "rar", "zip", "gz", "bz2",
            // 视频格式
            "mp4", "avi", "rmvb",
            // pdf
            "pdf"};
}
