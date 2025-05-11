package com.yf.constants;

import java.util.Map;

/**
 * 代码生成相关常量
 *
 * @author : YiFei
 * @since : 2025/3/9 12:34
 */
public interface GenerateCrudCodeConstants {

    // ========   Crud Option Key =========
    String TEMPLATE_MODULE_NAME = "module";
    String TEMPLATE_BUSINESS_NAME = "business";
    // ========   GenTableFields Key =========

    /**
     * 后端模版对应的文件名 ： {className} 为统一占位符
     */
    Map<String, String> BACK_TEMPLATE_ROUTING_MAP = Map.ofEntries(
            Map.entry("controller.ftl", "{className}Controller.java"),
            Map.entry("converter.ftl", "{className}Converter.java"),
            Map.entry("mapper.ftl", "{className}Mapper.java"),
            Map.entry("entity.ftl", "{className}.java"),
            Map.entry("form.ftl", "{className}Form.java"),
            Map.entry("query.ftl", "{className}PageQuery.java"),
            Map.entry("vo.ftl", "{className}PageVO.java"),
            Map.entry("service.ftl", "I{className}Service.java"),
            Map.entry("service-impl.ftl", "{className}ServiceImpl.java")
    );

    /**
     * 前端模版对应的文件名 ： {componentName} 为统一占位符
     */
    Map<String, String> FORNT_TEMPLATE_ROUTING_MAP = Map.ofEntries(
            Map.entry("api.ftl", "index.ts"),
            Map.entry("type.ftl", "type.ts"),
            Map.entry("index.ftl", "index.vue"),
            Map.entry("table.ftl", "{componentName}-table.vue"),
            Map.entry("search.ftl", "{componentName}-search.vue"),
            Map.entry("dialog.ftl", "{componentName}-manage-dialog.vue")
    );

    /**
     * 后端模板位置
     */
    Map<String, String> BACK_END_TEMPLATE_PATH_MAP = Map.ofEntries(
            Map.entry("SpringBoot3-MybatisPlus", "templates/sp3-mp")
    );

    /**
     * 前端模板位置
     */
    Map<String, String> FRONT_END_TEMPLATE_PATH_MAP = Map.ofEntries(
            Map.entry("Vue3-Typescript", "templates/v3-ts")
    );

    /**
     * 表单类型
     */
    Map<String, String> FORM_TYPE_MAP = Map.ofEntries(
            Map.entry("input", "输入框"),
            Map.entry("input_number", "数字输入框"),
            Map.entry("input_password", "密码输入框"),
            Map.entry("select", "下拉框"),
//            Map.entry("multi_select" , "下拉框" ),
            Map.entry("radio", "单选框"),
//            Map.entry("checkbox" , "多选框" ),
            Map.entry("date", "日期选择器"),
            Map.entry("date_time", "日期时间选择器"),
            Map.entry("switch", "开关"),
            Map.entry("slider", "滑块"),
            Map.entry("rate", "评分"),
            Map.entry("color", "颜色选择器"),
            Map.entry("file", "文件上传"),
            Map.entry("image", "图片上传"),
            Map.entry("markdown", "富文本"),
            Map.entry("text_area", "文本域")
    );

    /**
     * 查询类型
     */
    Map<String, String> QUERY_TYPE_MAP = Map.ofEntries(
            Map.entry("eq", "等于"),
            Map.entry("like", "模糊匹配"),
            Map.entry("like_left", "左模糊匹配"),
            Map.entry("like_right", "右模糊匹配"),
            Map.entry("gt", "大于"),
            Map.entry("lt", "小于"),
            Map.entry("ge", "大于等于"),
            Map.entry("le", "小于等于"),
            Map.entry("between", "区间"),
            Map.entry("in", "包含")
    );

    /**
     * 查询表单类型
     */
    Map<String, String> QUERY_FORM_TYPE_MAP = Map.ofEntries(
            // between 会自动匹配
            Map.entry("input", "输入框"),
            // between 会自动匹配
            Map.entry("input_number", "数字输入框"),
            // 字典会自动匹配
            Map.entry("select", "单选下拉框"),
            // 字典会自动匹配 && IN
            Map.entry("multi_select", "多选下拉框"),
            // between 会自动匹配
            Map.entry("datetime", "日期时间选择器"),
            // between 会自动匹配
            Map.entry("date", "日期选择器")
    );

    /**
     * 表单中展示类型
     */
    Map<String, String> SHOW_TYPE_MAP = Map.ofEntries(
            Map.entry("default", "文本"),
            Map.entry("text", "特殊文本"),
            Map.entry("tag", "标签"),
            Map.entry("image", "图片"),
            Map.entry("href", "链接"), // 与 file 字段类型配合使用
            Map.entry("rate", "评分")
    );

    /**
     * 字段类型
     */
    Map<String, String[]> FIELD_TYPE_MAP = Map.ofEntries(
            Map.entry("varchar", new String[]{"String", "string"}),
            Map.entry("char", new String[]{"String", "string"}),
            Map.entry("blob", new String[]{"byte[]", "Uint8Array"}),
            Map.entry("text", new String[]{"String", "string"}),
            Map.entry("json", new String[]{"String", "any"}),
            Map.entry("int", new String[]{"Integer", "number"}),
            Map.entry("tinyint", new String[]{"Integer", "number"}),
            Map.entry("smallint", new String[]{"Integer", "number"}),
            Map.entry("mediumint", new String[]{"Integer", "number"}),
            Map.entry("bigint", new String[]{"Long", "string"}),
            Map.entry("float", new String[]{"Float", "number"}),
            Map.entry("double", new String[]{"Double", "number"}),
            Map.entry("decimal", new String[]{"BigDecimal", "string"}),
            Map.entry("date", new String[]{"LocalDate", "string"}),
            Map.entry("datetime", new String[]{"LocalDateTime", "string"})
//            Map.entry("timestamp", new String[]{"Long", "string"})
    );
}
