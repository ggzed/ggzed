package com.yf.model.dto;

import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 自定义 MultipartFile （ 用于重构文件名 ）
 *
 * @author yiFei
 * @since 2023/11/21 14:05
 */
public class CustomMultipartFile implements MultipartFile {

    private final MultipartFile originalFile;
    private final String newFileName;

    public CustomMultipartFile(MultipartFile originalFile, String newFileName) {
        this.originalFile = originalFile;
        this.newFileName = newFileName;
    }

    @Override
    public @NonNull String getName() {
        return this.originalFile.getName();
    }

    @Override
    public String getOriginalFilename() {
        return this.newFileName;
    }

    // 其他方法直接委托给原始的 MultipartFile 对象
    @Override
    public String getContentType() {
        return this.originalFile.getContentType();
    }

    @Override
    public boolean isEmpty() {
        return this.originalFile.isEmpty();
    }

    @Override
    public long getSize() {
        return this.originalFile.getSize();
    }

    @Override
    public byte @NonNull [] getBytes() throws IOException {
        return this.originalFile.getBytes();
    }

    @Override
    public @NonNull InputStream getInputStream() throws IOException {
        return this.originalFile.getInputStream();
    }

    @Override
    public void transferTo(java.io.@NonNull File dest) throws IOException, IllegalStateException {
        this.originalFile.transferTo(dest);
    }
}
