package com.yf.utils;

import cn.hutool.core.util.StrUtil;
import com.yf.model.dto.GenCodeDto;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * freemarker 代码生成工具类
 *
 * @author : YiFei
 * @since : 2024/6/26 17:55
 */
@Component
@RequiredArgsConstructor
public class FreemarkerGenCodeUtil {

    public static final String BASE_BOOT_TEMPLATE_PATH = "boot";
    public static final String BASE_FRONT_TEMPLATE_PATH = "front";
    public static final String BASE_BUSINESS_PATH = "business";
    public static final String BASE_MODEL_PATH = "model";
    private final Configuration configuration;

    private static Map<String, String> initBootBusinessMap(String className) {
        Map<String, String> templatePaths = new HashMap<>(6);
        templatePaths.put("controller.java.ftl", "controller/%sController.java".formatted(className));
        templatePaths.put("converter.java.ftl", "converter/%sConverter.java".formatted(className));
        templatePaths.put("mapper.java.ftl", "mapper/%sMapper.java".formatted(className));
        templatePaths.put("mapper.xml.ftl", "resources/mapper/%sMapper.xml".formatted(className));
        templatePaths.put("service.java.ftl", "service/I%sService.java".formatted(className));
        templatePaths.put("serviceImpl.java.ftl", "service/impl/%sServiceImpl.java".formatted(className));
        return templatePaths;
    }

    private static Map<String, String> initBootModelMap(String className) {
        Map<String, String> templatePaths = new HashMap<>(4);
        templatePaths.put("entity.java.ftl", "model/entity/%s.java".formatted(className));
        templatePaths.put("form.java.ftl", "model/form/%sForm.java".formatted(className));
        templatePaths.put("query.java.ftl", "model/query/%sQuery.java".formatted(className));
        templatePaths.put("vo.java.ftl", "model/vo/%sVO.java".formatted(className));
        return templatePaths;
    }

    private static Map<String, String> initFrontMap(String moduleName, String businessName) {
        Map<String, String> templatePaths = new HashMap<>(3);
        String realBusinessName = StrUtil.toUnderlineCase(businessName).replace('_', '-');
        templatePaths.put("api.ts.ftl", "api/%s/%s/index.ts".formatted(moduleName, realBusinessName));
        templatePaths.put("api_type.ts.ftl", "api/%s/%s/type.ts".formatted(moduleName, realBusinessName));
        templatePaths.put("views.vue.ftl", "views/%s/%s/index.vue".formatted(moduleName, realBusinessName));
        return templatePaths;
    }

    /**
     * 获取代码生成数据集合
     *
     * @param genCode genCode Dto
     * @return key : zip打包路径 value : 对应字节
     */
    public Map<String, byte[]> getGenCodeByte(GenCodeDto genCode) {
        Map<String, byte[]> bootFrameworkByte = getBootFrameworkByte(genCode);
        Map<String, byte[]> frontFrameworkByte = getFrontFrameworkByte(genCode);

        // 合并两个Map
        Map<String, byte[]> combinedByteMap = new HashMap<>(bootFrameworkByte);
        combinedByteMap.putAll(frontFrameworkByte);

        return combinedByteMap;
    }


    /**
     * 获取代码生成数据集合
     *
     * @param genCode genCode Dto
     * @return key : 文件名 value : 对应文件字符串
     */
    public Map<String, String> getGenCodeString(GenCodeDto genCode) {
        Map<String, String> bootFrameworkString = getBootFrameworkString(genCode);
        Map<String, String> frontFrameworkString = getFrontFrameworkString(genCode);

        // 合并两个Map
        Map<String, String> combinedStringMap = new HashMap<>(bootFrameworkString);
        combinedStringMap.putAll(frontFrameworkString);

        return combinedStringMap;
    }

    private Map<String, String> getFrontFrameworkString(GenCodeDto genCode) {
        // 1. 定义基础变量
        String frontType = genCode.getTable().getFrontType();
        String moduleName = genCode.getTable().getModuleName();     // 外层包名
        String businessName = genCode.getTable().getBusinessName(); // 内层包名
        Map<String, String> templateMap = new HashMap<>(3);
        // 2. 构建基础路径
        String frontPath = buildPath(BASE_FRONT_TEMPLATE_PATH, frontType);

        try (StringWriter writer = new StringWriter()) {
            // 3. 填充前端模板
            Map<String, String> initBusinessMap = initFrontMap(moduleName, businessName);
            fillStringTemplateMap(genCode, templateMap, frontPath, writer, initBusinessMap);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }

        return templateMap;
    }

    private Map<String, String> getBootFrameworkString(GenCodeDto genCode) {
        // 1. 定义基础变量
        String bootType = genCode.getTable().getBootType();
        String className = genCode.getTable().getClassName();
        Map<String, String> templateMap = new HashMap<>(10);
        // 2. 构建基础路径
        String businessPath = buildPath(BASE_BOOT_TEMPLATE_PATH, bootType, BASE_BUSINESS_PATH);
        String modelPath = buildPath(BASE_BOOT_TEMPLATE_PATH, bootType, BASE_MODEL_PATH);

        try (StringWriter writer = new StringWriter()) {
            // 3. 初始化业务模板
            Map<String, String> initBusinessMap = initBootBusinessMap(className);
            fillStringTemplateMap(genCode, templateMap, businessPath, writer, initBusinessMap);
            // 4. 初始化实体类模板
            Map<String, String> initModelMap = initBootModelMap(className);
            fillStringTemplateMap(genCode, templateMap, modelPath, writer, initModelMap);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }

        return templateMap;
    }


    /**
     * 注 : 不论是 mybatis , mybatis-plus 文件路径都是一样
     *
     * @param genCode 代码生成Dto
     * @return key : 应该在 zip 中的路径 , value 具体的 template (ftl) 模版
     */
    private Map<String, byte[]> getBootFrameworkByte(GenCodeDto genCode) {
        // 1. 定义基础变量
        String bootType = genCode.getTable().getBootType();
        String className = genCode.getTable().getClassName();
        String moduleName = genCode.getTable().getModuleName();     // 外层包名
        Map<String, byte[]> templateMap = new HashMap<>(10);
        // 2. 构建基础路径
        String businessPath = buildPath(BASE_BOOT_TEMPLATE_PATH, bootType, BASE_BUSINESS_PATH);
        String modelPath = buildPath(BASE_BOOT_TEMPLATE_PATH, bootType, BASE_MODEL_PATH);

        try (StringWriter writer = new StringWriter()) {
            // 3. 填充业务模板
            Map<String, String> initBusinessMap = initBootBusinessMap(className);
            fillByteTemplateMap(BASE_BOOT_TEMPLATE_PATH + "/" + moduleName + "/",
                    genCode, templateMap, businessPath, writer, initBusinessMap);

            // 4. 填充实体类模板
            Map<String, String> initModelMap = initBootModelMap(className);
            fillByteTemplateMap(BASE_BOOT_TEMPLATE_PATH + "/" + moduleName + "/",
                    genCode, templateMap, modelPath, writer, initModelMap);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }

        return templateMap;
    }

    private Map<String, byte[]> getFrontFrameworkByte(GenCodeDto genCode) {
        // 1. 定义基础变量
        String frontType = genCode.getTable().getFrontType();
        String moduleName = genCode.getTable().getModuleName();     // 外层包名
        String businessName = genCode.getTable().getBusinessName(); // 内层包名
        Map<String, byte[]> templateMap = new HashMap<>(3);
        // 2. 构建基础路径
        String frontPath = buildPath(BASE_FRONT_TEMPLATE_PATH, frontType);

        try (StringWriter writer = new StringWriter()) {
            // 3. 填充前端模板
            Map<String, String> initBusinessMap = initFrontMap(moduleName, businessName);
            fillByteTemplateMap(BASE_FRONT_TEMPLATE_PATH + "/",
                    genCode, templateMap, frontPath, writer, initBusinessMap);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }

        return templateMap;
    }

    /**
     * 填充模板映射表，根据模板文件路径映射和输出路径映射。
     *
     * @param basePath    模板输出的基础路径
     * @param genCode     生成代码的数据传输对象
     * @param templateMap 存储模板内容的映射表，键为输出文件路径，值为模板内容的字节数组
     * @param modulePath  模块路径，用于拼接模板文件的完整路径
     * @param writer      字符串写入器，用于处理模板
     * @param initMap     初始化的模板文件名与输出路径的映射表
     * @throws IOException       如果加载模板文件或处理模板时发生I/O错误
     * @throws TemplateException 如果处理模板时发生错误
     */
    private void fillByteTemplateMap(String basePath, GenCodeDto genCode, Map<String, byte[]> templateMap, String modulePath, StringWriter writer, Map<String, String> initMap) throws IOException, TemplateException {
        for (Map.Entry<String, String> entry : initMap.entrySet()) {
            String templateName = entry.getKey();   // 模板文件名
            String outputPath = entry.getValue();   // 输出文件路径
            String templateContent = processTemplate(modulePath + templateName, genCode, writer); // 处理模板内容
            templateMap.put(basePath + outputPath, templateContent.getBytes());  // 将处理后的模板内容放入映射表
        }
    }

    /**
     * 填充模板映射表，根据模板文件路径映射和输出路径映射。
     *
     * @param genCode     生成代码的数据传输对象
     * @param templateMap 存储模板内容的映射表，键为输出文件路径，值为模板内容的字节数组
     * @param modulePath  模块路径，用于拼接模板文件的完整路径
     * @param writer      字符串写入器，用于处理模板
     * @param initMap     初始化的模板文件名与输出路径的映射表
     * @throws IOException       如果加载模板文件或处理模板时发生I/O错误
     * @throws TemplateException 如果处理模板时发生错误
     */
    private void fillStringTemplateMap(GenCodeDto genCode, Map<String, String> templateMap, String modulePath, StringWriter writer, Map<String, String> initMap) throws IOException, TemplateException {
        for (Map.Entry<String, String> entry : initMap.entrySet()) {
            String templateName = entry.getKey();   // 模板文件名
            String templateContent = processTemplate(modulePath + templateName, genCode, writer); // 处理模板内容
            templateMap.put(templateName, templateContent);  // 将处理后的模板内容放入映射表
        }
    }


    /**
     * 构建基础路径
     *
     * @param basePath 基础路径
     * @param paths    其他路径部分
     * @return 构建的基础路径
     */
    private String buildPath(String basePath, String... paths) {
        StringBuilder fullPath = new StringBuilder(basePath);
        for (String path : paths) {
            fullPath.append(File.separator).append(path);
        }
        fullPath.append(File.separator); // 最后添加一个分隔符
        return fullPath.toString();
    }


    /**
     * 处理 FreeMarker 模板，并将生成的内容作为字符串返回。
     *
     * @param templatePath 模板文件的路径
     * @param genCodeDto   用于模板处理的数据模型
     * @param writer       用于捕获模板输出的 StringWriter
     * @return 处理后的模板内容字符串
     * @throws IOException       如果加载模板时发生错误
     * @throws TemplateException 如果处理模板时发生错误
     */
    private String processTemplate(String templatePath, GenCodeDto genCodeDto, StringWriter writer) throws IOException, TemplateException {
        // 从指定路径加载模板
        Template template = configuration.getTemplate(templatePath);
        // 清空 StringWriter 的内容，确保在处理模板之前是空的
        writer.getBuffer().setLength(0);
        // 使用数据模型处理模板，并将结果写入 StringWriter
        template.process(genCodeDto, writer);
        // 将 StringWriter 中的内容转换为字符串并返回
        return writer.toString();
    }


}
