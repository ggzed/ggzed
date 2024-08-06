package com.yf.storage.impl;

import com.yf.exception.ServiceException;
import com.yf.model.result.ResultCode;
import com.yf.storage.FileStorageService;
import com.yf.utils.FileUtils;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.IOException;

import static com.yf.utils.FileUtils.DIR_SEPARATOR;

/**
 * 本地文件上传 ( 默认 )
 * 注: 设置matchIfMissing = true会使 havingValue 失效
 * 这里 havingValue 只是为表明此类加载的是 local
 *
 * @author yiFei
 * @since 2023/11/20 14:49
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "file.storage.type", havingValue = "local", matchIfMissing = true)
@ConfigurationProperties(prefix = "file.storage.local")
@RequiredArgsConstructor
@Data
public class LocalFileStorageService implements FileStorageService {

    /**
     * 访问端点 ( 注 : 可以不是当前服务 )
     */
    private String endpoint = "http://localhost:8080";
    /**
     * 上传路径
     */
    private String uploadPath = "D:\\";
    /**
     * 访问的路径名 ( 请根据项目情况控制是否放行文件，比如允许不登录即可访问 /images )
     */
    private String accessUrl = "/images";

    /**
     * 配置文件访问路径和实际文件存储路径的映射关系，使得通过指定的访问路径可以访问到对应的文件系统中的资源
     *
     * @return WebMvcConfigurer
     */
    @Bean
    public WebMvcConfigurer resourceHandlerConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
                /*
                    映射: /images/** ---------> file:uploadPath
                    举例: http://ip:host/images/1.png ---------> file:\www\images\1.png
                 */
                registry.addResourceHandler(accessUrl + "/**").addResourceLocations("file:" + uploadPath);
            }
        };
    }

    @Override
    public String getFileStorageEndpoint() {
        return endpoint;
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
        // 1. 拼接文件路径
        String datePath = FileUtils.datePath();
        String baseUrl = StringUtils.hasText(savePath) ? savePath + DIR_SEPARATOR + datePath : datePath;
        String uploadPath = this.uploadPath + baseUrl;
        try {
            // 2. 获取文件对象上传
            File absoluteFile = this.getAbsoluteFile(uploadPath, file.getOriginalFilename());
            // 3. 上传文件
            file.transferTo(absoluteFile);
        } catch (IOException e) {
            log.error("LocalFileStorageService -> uploadFile, 常见错误: 未开启路径权限: {}", uploadPath);
            throw new ServiceException(ResultCode.FILE_UPLOAD_ERROR);
        }

        return endpoint + accessUrl + "/" + baseUrl + file.getOriginalFilename();
    }

    /**
     * 删除单个文件
     *
     * @param savePath 保存路径 ( xx )
     * @param fileName 文件访问路径 （ xx/xx.jpg ）
     * @return 是否删除成功，注: 不报错则返回 true
     */
    @Override
    public boolean deleteFile(String savePath, String fileName) {
        // 1. 拼接 baseUrl
        File file = new File(uploadPath + savePath + File.separator + fileName);
        // 2. 删除对应文件
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 获取上传文件的绝对路径
     *
     * @param uploadDir 上传文件路径
     * @param fileName  文件名
     * @return File 上传文件的绝对路径文件对象
     */
    private File getAbsoluteFile(String uploadDir, String fileName) {
        File desc = new File(uploadDir, fileName);
        // 不存在父目录则创建父目录
        File parentDir = desc.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                throw new RuntimeException("无法创建上传文件的目录：" + parentDir);
            }
        }

        return desc;
    }
}
