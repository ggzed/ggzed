package com.yf.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * tensorflow 工具类
 *
 * @author : YiFei
 * @since : 2024/8/4 16:49
 */
@Slf4j
public class TensorflowUtil {

    public static final String TEMP_TENSORFLOW_MODEL_PATH = "tensorflow-model";
    public static final String SEARCH_RESOURCE = "classpath*:%s/*";

    /**
     * @param classPathResource class_path下的资源文件
     * @return 模型路径
     */
    public static String getModelPath(String classPathResource) throws IOException {
        File tempModelDir = Files.createTempDirectory(TEMP_TENSORFLOW_MODEL_PATH).toFile();
        Path tempModelDirPath = tempModelDir.toPath();

        copyTempModel("", classPathResource, tempModelDirPath);
        copyTempModel("assets", classPathResource, tempModelDirPath);
        copyTempModel("variables", classPathResource, tempModelDirPath);
        
        log.info("classPathResource: {} , ===> 临时文件存储在 {} ", classPathResource, tempModelDir.getAbsolutePath());
        return tempModelDir.getAbsolutePath();
    }

    private static void copyTempModel(String dir, String resourcePath, Path targetDirPath) throws IOException {
        // 1. 拼接文件夹
        Path modelPath = targetDirPath.resolve(dir);
        // 2. 创建文件
        if (StringUtils.hasText(dir)) {
            Files.createDirectories(modelPath);
            resourcePath = resourcePath + "/" + dir;
        }
        // 3. 扫描文件
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = patternResolver.getResources(SEARCH_RESOURCE.formatted(resourcePath));

        // 4. 创建对应文件 ( jar包下只会扫描出文件 )
        for (Resource resource : resources) {
            String filename = resource.getFilename();
            if (StringUtils.hasText(filename)) {
                // 如果是文件并且存在文件名，直接拷贝
                try {
                    Files.copy(resource.getInputStream(), modelPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception ignored) {
                }
            }
        }
    }


    /**
     * 拷贝Resource文件到临时目录 ( 注意 : jar包环境下无法获取到对应数据 )
     *
     * @param resourcePath  资源路径
     * @param targetDirPath 临时文件路径
     */
    private static void copyResource2TempDir(String resourcePath, Path targetDirPath) throws IOException {
        // 扫描所有资源
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = patternResolver.getResources(SEARCH_RESOURCE.formatted(resourcePath));
        // 临时存储文件夹信息
        InputStream inputStream;
        for (Resource resource : resources) {
            String filename = resource.getFilename();
            if (StringUtils.hasText(filename)) {
                if ((inputStream = isJarFile(resource)) != null) {
                    // 如果是文件并且存在文件名，直接拷贝
                    Files.copy(inputStream, targetDirPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    Path dirResolve = targetDirPath.resolve(filename);
                    // 如果是文件夹，创建文件夹后创建子文件
                    Files.createDirectories(dirResolve);
                    // 递归创建子文件
                    copyResource2TempDir(resourcePath + "/" + filename, dirResolve);
                }
            }
        }
    }

    /**
     * 获取文件的 InputStream
     *
     * @param resource 资源文件
     * @return 是文件返回 InputStream , 不是返回 null
     */
    private static InputStream isJarFile(Resource resource) {
        try {
            return resource.getInputStream();
        } catch (Exception e) {
            return null;
        }
    }
}
