package com.yf.utils;

import com.yf.file.model.dto.ResourcesFile;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * ResourcesFile 工具类
 *
 * @author : YiFei
 * @since : 2025/4/9 11:29
 */
@Slf4j
public class ResourcesFileUtil {

    private ResourcesFileUtil() {
        // 私有构造函数，防止实例化
    }

    /**
     * 构建资源文件列表
     *
     * @param resourcesPath 资源路径
     * @param keepPrefix    是否保留前缀
     * @return 资源文件列表
     */
    @SneakyThrows
    public static List<ResourcesFile> buildResourcesFileList(String resourcesPath, boolean keepPrefix) {
        if (validatedPath(resourcesPath)) {
            throw new RuntimeException("resourcesPath is error , example : templates/develop");
        }
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:" + resourcesPath + "/**");

        Map<String, ResourcesFile> nodeMap = new LinkedHashMap<>();
        String prefixToRemove = resourcesPath.substring(resourcesPath.lastIndexOf("/") + 1);
        for (Resource resource : resources) {
            String relativePath = getNormalizedPath(resource, prefixToRemove, keepPrefix);
            if (relativePath == null || relativePath.isEmpty()) continue;

            Deque<String> pathStack = new ArrayDeque<>();
            String[] segments = relativePath.split("/");
            StringJoiner currentPath = new StringJoiner("/");

            for (int i = 0; i < segments.length; i++) {
                String segment = segments[i].trim();
                if (segment.isEmpty()) continue;

                currentPath.add(segment);
                String currentPathString = currentPath.toString();
                boolean isLeaf = (i == segments.length - 1);

                if (!nodeMap.containsKey(currentPathString)) {
                    ResourcesFile node = new ResourcesFile();
                    node.setFilePath(currentPathString);
                    node.setFileName(segment);
                    node.setIsFile(isLeaf);
                    node.setParentFilePath(!pathStack.isEmpty() ? pathStack.peek() : "");
                    nodeMap.put(currentPathString, node);
                }

                // 关键改进：确保父节点正确性
                if (!isLeaf) {
                    ResourcesFile currentNode = nodeMap.get(currentPathString);
                    if (currentNode.getIsFile()) {
                        currentNode.setIsFile(false); // 升级为目录节点
                    }
                }
                pathStack.push(currentPathString);
            }
        }
        return new ArrayList<>(nodeMap.values());
    }

    /**
     * 将指定路径下的资源文件复制到临时目录
     *
     * @param resourcesPath 资源路径模式
     * @return 临时目录路径
     */
    @SneakyThrows
    public static Path copyResourcesToTemp(String resourcesPath) {
        if (validatedPath(resourcesPath)) {
            throw new RuntimeException("resourcesPath is error , example : templates/develop");
        }
        String prefixToRemove = resourcesPath.substring(resourcesPath.lastIndexOf("/") + 1);
        // 创建临时目录（不再自动添加前缀）
        Path tempDir = Files.createTempDirectory(prefixToRemove + "_");

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:" + resourcesPath + "/**");

        for (Resource resource : resources) {
            if (!resource.exists() || !resource.isReadable()) continue;

            // 获取已清理前缀的相对路径
            String relativePath = getNormalizedPath(resource, prefixToRemove, false);
            if (relativePath == null) continue;

            // 直接构建到临时目录
            Path targetPath = tempDir.resolve(relativePath);

            // 创建父目录并复制文件
            Files.createDirectories(targetPath.getParent());
            try (InputStream is = resource.getInputStream()) {
                Files.copy(is, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        log.info("resourcesPath : {} , Copy To: {}", resourcesPath, tempDir);
        return tempDir;
    }

    /**
     * 验证路径是否合法
     *
     * @param resourcesPath 资源路径
     * @return true: 不合法， false: 合法
     */
    private static boolean validatedPath(String resourcesPath) {
        return !StringUtils.hasText(resourcesPath) || resourcesPath.contains(".") || resourcesPath.startsWith("/") || resourcesPath.endsWith("/");
    }

    /**
     * 获取规范化路径
     *
     * @param resource       资源对象
     * @param prefixToRemove 要移除的前缀
     * @param keepPrefix     是否保留前缀
     * @return 规范化路径
     */
    private static String getNormalizedPath(Resource resource, String prefixToRemove, boolean keepPrefix) {
        try {
            String uri = resource.getURI().toString();

            // 统一路径格式
            uri = URLDecoder.decode(uri, StandardCharsets.UTF_8).replace("\\", "/").replaceAll("/+", "/");

            // 关键改进：智能路径截取
            String[] pathParts = uri.split("!/");
            String relevantPath = Arrays.stream(pathParts).filter(p -> p.contains(prefixToRemove)).findFirst().orElse(uri);

            // 定位目标前缀起始位置
            int prefixIndex = relevantPath.indexOf(prefixToRemove);
            if (prefixIndex == -1) return null;

            // 提取所需路径段（包含前缀）
            String resultPath = relevantPath.substring(prefixIndex);

            // 路径清理
            String result = resultPath.replaceAll("^/+", "")      // 去除开头斜杠
                    .replaceAll("/+$", "");                 // 去除结尾斜杠

            return keepPrefix ? result : result.replaceAll("^" + prefixToRemove + "/?", ""); // 可选：移除前缀
        } catch (Exception e) {
            return null;
        }
    }
}
