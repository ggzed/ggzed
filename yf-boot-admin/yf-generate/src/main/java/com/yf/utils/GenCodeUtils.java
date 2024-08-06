package com.yf.utils;

import com.yf.model.enums.BackendQueryMethod;
import com.yf.model.enums.FrontendComponentType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * GenCodeUtils ( 生成代码工具类 )
 *
 * @author YiFei
 * @since 2024-06-14 16:53:13
 */
public class GenCodeUtils {
    /**
     * Java 类型集合
     */
    private static final Map<String, String> javaTypeMapping = new HashMap<>();
    /**
     * ts 类型集合
     */
    private static final Map<String, String> tsTypeMapping = new HashMap<>();

    // 假设这里有一个字段名的黑名单集合，用于判断是否需要进行查询、修改/插入、展示操作
    private static final Set<String> nonQueryKeywords = new HashSet<>();
    private static final Set<String> nonModifyKeywords = new HashSet<>();
    private static final Set<String> nonDisplayKeywords = new HashSet<>();

    static {
        initializeJavaTypeMapping();
        initializeTsTypeMapping();
        initializeQueryKeywords();
        initializeModifyKeywords();
        initializeDisplayKeywords();
    }

    /**
     * 根据 columnType 转换为对应的 javaType ( 注 : 不与 ToTsType 合并 ，方便单独使用 )
     *
     * @param columnType 数据库列类型
     * @return 对应的 Java 类型
     */
    public static String convertColumnTypeToJavaType(String columnType) {
        // 处理带有长度的类型，例如 bigint(20)，只取括号前的部分进行匹配
        if (columnType.contains("(")) {
            columnType = columnType.substring(0, columnType.indexOf('(')).toLowerCase();
        } else {
            columnType = columnType.toLowerCase();
        }
        return javaTypeMapping.getOrDefault(columnType, "String"); // 默认为String类型
    }

    /**
     * 根据 columnType 转换为对应的 tsType ( 注 : 不与 ToJavaType 合并 ，方便单独使用 )
     *
     * @param columnType 数据库列类型
     * @return 对应的 ts 类型
     */
    public static String convertColumnTypeToTsType(String columnType) {
        // 处理带有长度的类型，例如 bigint(20)，只取括号前的部分进行匹配
        if (columnType.contains("(")) {
            columnType = columnType.substring(0, columnType.indexOf('(')).toLowerCase();
        } else {
            columnType = columnType.toLowerCase();
        }
        return tsTypeMapping.getOrDefault(columnType, "any"); // 默认为 any 类型
    }

    /**
     * 根据列名推测字段名是否需要查询
     *
     * @param columnName 列名
     * @return 是否需要查询
     */
    public static boolean needsQuery(String columnName) {
        for (String keyword : nonQueryKeywords) {
            if (columnName.toLowerCase().contains(keyword)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据列名推测字段名是否需要修改或插入
     *
     * @param columnName 列名
     * @return 是否需要修改或插入
     */
    public static boolean needsModify(String columnName) {
        for (String keyword : nonModifyKeywords) {
            if (columnName.toLowerCase().contains(keyword)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据列名推测字段是否需要展示（前端）
     *
     * @param columnName 列名
     * @return 是否需要展示
     */
    public static boolean needsDisplay(String columnName) {
        for (String keyword : nonDisplayKeywords) {
            if (columnName.toLowerCase().contains(keyword)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据列名推测字段的前端显示组件类型
     *
     * @param columnName 列名
     * @return 前端显示组件类型（可以是字符串、枚举等）
     */
    public static FrontendComponentType frontendComponentType(String columnName) {
        return FrontendComponentType.TEXT; // 默认为文本输入框
    }

    /**
     * 根据列名推测字段的后端查询方式
     *
     * @param columnName 列名
     * @return 后端查询方式（可以是字符串、枚举等）
     */
    public static BackendQueryMethod backendQueryMethod(String columnName) {
        return BackendQueryMethod.EQUALS; // 默认为等于查询
    }


    private static void initializeJavaTypeMapping() {
        // 添加常见的数据类型
        // 字符串类型
        javaTypeMapping.put("varchar", "String");
        javaTypeMapping.put("char", "String");
        javaTypeMapping.put("text", "String");
        javaTypeMapping.put("tinytext", "String");
        javaTypeMapping.put("mediumtext", "String");
        javaTypeMapping.put("longtext", "String");

        // 数值类型
        javaTypeMapping.put("int", "Integer");
        javaTypeMapping.put("integer", "Integer");
        javaTypeMapping.put("bigint", "Long");
        javaTypeMapping.put("tinyint", "Integer");
        javaTypeMapping.put("smallint", "Short");
        javaTypeMapping.put("mediumint", "Integer");
//        javaTypeMapping.put("float", "Float");
//        javaTypeMapping.put("double", "Double");
        javaTypeMapping.put("float", "BigDecimal");
        javaTypeMapping.put("double", "BigDecimal");
        javaTypeMapping.put("decimal", "BigDecimal");
        javaTypeMapping.put("numeric", "BigDecimal");

        // 日期时间类型
        javaTypeMapping.put("datetime", "LocalDateTime");
        javaTypeMapping.put("date", "LocalDate");
        javaTypeMapping.put("timestamp", "LocalDateTime");
        javaTypeMapping.put("time", "LocalTime");
        javaTypeMapping.put("year", "Integer");

        // 二进制数据类型
        javaTypeMapping.put("binary", "byte[]");
        javaTypeMapping.put("varbinary", "byte[]");
        javaTypeMapping.put("blob", "byte[]");
        javaTypeMapping.put("tinyblob", "byte[]");
        javaTypeMapping.put("mediumblob", "byte[]");
        javaTypeMapping.put("longblob", "byte[]");

        // 布尔类型
        javaTypeMapping.put("bit", "Boolean");
        javaTypeMapping.put("boolean", "Boolean");

        // JSON类型
        javaTypeMapping.put("json", "String");

        // 枚举类型
        javaTypeMapping.put("enum", "String");
        javaTypeMapping.put("set", "String");
    }

    private static void initializeTsTypeMapping() {
        // 字符串类型
        tsTypeMapping.put("varchar", "string");
        tsTypeMapping.put("char", "string");
        tsTypeMapping.put("text", "string");
        tsTypeMapping.put("tinytext", "string");
        tsTypeMapping.put("mediumtext", "string");
        tsTypeMapping.put("longtext", "string");

        // 数值类型
        tsTypeMapping.put("int", "number");
        tsTypeMapping.put("integer", "number");
        tsTypeMapping.put("bigint", "number");
        tsTypeMapping.put("tinyint", "number");
        tsTypeMapping.put("smallint", "number");
        tsTypeMapping.put("mediumint", "number");
        tsTypeMapping.put("float", "number");
        tsTypeMapping.put("double", "number");
        tsTypeMapping.put("decimal", "number");
        tsTypeMapping.put("numeric", "number");

        // 日期时间类型
//        tsTypeMapping.put("datetime", "Date");
//        tsTypeMapping.put("date", "Date");
//        tsTypeMapping.put("timestamp", "Date");
//        tsTypeMapping.put("year", "number");
        tsTypeMapping.put("time", "string");
        tsTypeMapping.put("datetime", "string");
        tsTypeMapping.put("date", "string");
        tsTypeMapping.put("timestamp", "string");
        tsTypeMapping.put("year", "string");

        // 二进制数据类型
        tsTypeMapping.put("binary", "ArrayBuffer");
        tsTypeMapping.put("varbinary", "ArrayBuffer");
        tsTypeMapping.put("blob", "ArrayBuffer");
        tsTypeMapping.put("tinyblob", "ArrayBuffer");
        tsTypeMapping.put("mediumblob", "ArrayBuffer");
        tsTypeMapping.put("longblob", "ArrayBuffer");

        // 布尔类型
        tsTypeMapping.put("bit", "boolean");
        tsTypeMapping.put("boolean", "boolean");

        // JSON类型
        tsTypeMapping.put("json", "any");

        // 枚举类型
        tsTypeMapping.put("enum", "string");
        tsTypeMapping.put("set", "string");
    }

    private static void initializeQueryKeywords() {
        nonQueryKeywords.add("id");
        nonQueryKeywords.add("create_");
        nonQueryKeywords.add("update_");
        nonQueryKeywords.add("remark");
        nonQueryKeywords.add("desc");
        nonQueryKeywords.add("_flag");
        nonQueryKeywords.add("sort");
        nonQueryKeywords.add("status");
        nonQueryKeywords.add("content");
        nonQueryKeywords.add("msg");
        nonQueryKeywords.add("message");
        nonQueryKeywords.add("del");
        nonQueryKeywords.add("delete");
    }

    private static void initializeModifyKeywords() {
        nonModifyKeywords.add("id");
        nonModifyKeywords.add("create_");
        nonModifyKeywords.add("update_");
        nonModifyKeywords.add("_flag");
        nonModifyKeywords.add("del");
        nonModifyKeywords.add("delete");
    }

    private static void initializeDisplayKeywords() {
        nonDisplayKeywords.add("id");
        nonDisplayKeywords.add("_flag");
        nonDisplayKeywords.add("del");
        nonDisplayKeywords.add("delete");
    }
}