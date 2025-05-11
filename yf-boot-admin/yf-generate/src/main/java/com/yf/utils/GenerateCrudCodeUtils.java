package com.yf.utils;

/**
 * GenCodeUtils ( 生成代码工具类 )
 *
 * @author YiFei
 * @since 2024-06-14 16:53:13
 */
public class GenerateCrudCodeUtils {

    /**
     * 通过表名生成类名
     *
     * @param tableName 表名
     * @return 类名
     */
    public static String tableToClassName(String tableName) {
        // 默认表名按照规范进行 table_name -> TableName
        String[] split = tableName.split("_");
        StringBuilder className = new StringBuilder();
        for (String s : split) {
            className.append(s.substring(0, 1).toUpperCase()).append(s.substring(1));
        }
        return className.toString();
    }

    /**
     * 通过表名生成组件名
     *
     * @param tableName 表名
     * @return 组件名
     */
    public static String tableToComponentName(String tableName) {
        // 默认表名按照规范进行 table_name -> table-name
        return tableName.replaceAll("_", "-");
    }

    /**
     * 通过表名生成模块名
     *
     * @param tableName 表名
     * @return 模块名
     */
    public static String getModuleName(String tableName) {
        // 默认表名按照规范进行 table_name -> table
        return tableName.split("_")[0];
    }

    /**
     * 通过表名获取业务名称
     *
     * @param tableName 表名
     * @return 业务名称
     */
    public static String getBusinessName(String tableName) {
        // 默认表名按照规范进行 table_name -> name
        String[] split = tableName.split("_");
        if (split.length > 1) {
            StringBuilder result = new StringBuilder();
            for (int i = 1; i < split.length; i++) {
                result.append(split[i]);
            }
            return result.toString();
        } else {
            return split[0];
        }
    }

    /**
     * 通过字段名获取 Java & Ts 字段名
     *
     * @param fieldName 字段名
     * @return Java & Ts 字段名
     */
    public static String getJavaTsFieldName(String fieldName) {
        // 默认字段名按照规范进行 field_name -> fieldName
        String[] split = fieldName.split("_");
        StringBuilder javaTsFieldName = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            if (i == 0) {
                javaTsFieldName.append(split[i]);
            } else {
                javaTsFieldName.append(split[i].substring(0, 1).toUpperCase()).append(split[i].substring(1));
            }
        }
        return javaTsFieldName.toString();
    }

    /**
     * 判断是否展示字段
     *
     * @param fieldName    字段名
     * @param fieldType    字段类型
     * @param fieldComment 字段注释
     * @return 是否展示
     */
    public static Boolean judgeShowField(String fieldName, String fieldType, String fieldComment) {
        // 判断备注是否适合展示
        if (containsKeywords(fieldComment, "逻辑", "外键", "主键", "自增", "内部", "内容", "外部", "关联", "版本", "乐观锁")
        ) {
            return false;
        }
        // 判断字段名是否适合展示
        if (containsKeywords(fieldName, "id", "Id", "deleted", "version", "create", "update", "is_deleted")) {
            return false;
        }
        // 判断字段类型是否适合表单展示
        return !containsKeywords(fieldType, "blob", "text");
    }

    /**
     * 判断是否为可修改字段
     *
     * @param fieldName    字段名
     * @param fieldType    字段类型
     * @param fieldComment 字段注释
     * @return 是否查询
     */
    public static Boolean judgeFormField(String fieldName, String fieldType, String fieldComment) {
        // 判断备注是否适合展示
        if (containsKeywords(fieldComment, "逻辑", "外键", "主键", "自增", "内部", "外部", "关联", "版本", "乐观锁")) {
            return false;
        }

        // 判断字段名是否适合展示
        return !containsKeywords(fieldName, "id", "Id", "deleted", "version", "create", "update", "is_deleted");
    }

    /**
     * 判断是否为查询字段
     *
     * @param fieldName    字段名
     * @param fieldType    字段类型
     * @param fieldComment 字段注释
     * @return 是否查询
     */
    public static Boolean judgeQueryField(String fieldName, String fieldType, String fieldComment) {
        // 判断备注是否适合展示
        if (containsKeywords(fieldComment, "逻辑", "外键", "主键", "自增", "内部", "外部", "关联", "版本", "乐观锁")) {
            return false;
        }
        if (containsKeywords(fieldComment, "密码", "密钥", "token") ||
                containsKeywords(fieldName, "password", "token", "secret", "key", "pwd", "salt", "auth")) {
            return false;
        }
        if (containsKeywords(fieldComment, "图片", "头像", "图标", "logo", "avatar") ||
                containsKeywords(fieldName, "image", "icon", "avatar", "logo", "img")) {
            return false;
        }
        if (containsKeywords(fieldComment, "文件", "附件", "文档", "压缩", "zip", "rar", "7z", "tar", "gz", "pdf", "doc") ||
                containsKeywords(fieldName, "file", "zip", "rar", "7z", "tar", "gz", "pdf", "doc")) {
            return false;
        }
        // 判断字段名是否适合展示
        return !containsKeywords(fieldName, "id", "Id", "deleted", "version", "is_deleted");
    }

    /**
     * 判断字段内容是否符合不包含某些关键字的规则
     *
     * @param content  字段名或字段注释
     * @param keywords 不符合条件的关键词列表
     * @return 如果符合条件返回 true, 否则 false
     */
    public static Boolean containsKeywords(String content, String... keywords) {
        if (content == null) return true;  // 如果字段内容为空，则不做判断，返回 true

        for (String keyword : keywords) {
            if (content.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 展示表单类型
     *
     * @param fieldName    字段名
     * @param fieldType    字段类型
     * @param fieldComment 字段注释
     * @return 展示表单类型
     */
    public static String judgeShowType(String fieldName, String fieldType, String fieldComment) {
        if (containsKeywords(fieldComment, "图片", "头像", "图标", "logo", "avatar") ||
                containsKeywords(fieldName, "image", "icon", "avatar", "logo", "img")) {
            return "image";
        }
        if (containsKeywords(fieldComment, "文件", "附件", "文档", "压缩", "zip", "rar", "7z", "tar", "gz", "pdf", "doc") ||
                containsKeywords(fieldName, "file", "zip", "rar", "7z", "tar", "gz", "pdf", "doc")) {
            return "href";
        }
        if (containsKeywords(fieldComment, "年龄", "分数", "成绩", "数字", "价格", "金额", "状态", "类型", "性别",
                "类别", "等级", "字典", "开关", "是否", "标签") ||
                containsKeywords(fieldName, "age", "score", "grade", "number", "price", "amount", "status",
                        "type", "gender", "level", "category", "dict", "is", "enable", "disable", "flag")) {
            return "tag";
        }
        if (containsKeywords(fieldComment, "备注", "描述", "注意") || containsKeywords(fieldName, "remark", "note", "prompt", "description")) {
            return "text";
        }
        if (containsKeywords(fieldComment, "评分", "星级") || containsKeywords(fieldName, "rate", "rating")) {
            return "rate";
        }
        return "default";
    }

    public static String judgeSaveFormType(String fieldName, String fieldType, String fieldComment) {
        // 关键词匹配逻辑建议使用全词匹配（需在 containsKeywords 中实现）
        if (containsKeywords(fieldComment, "密码", "密钥", "token") ||
                containsKeywords(fieldName, "password", "token", "secret", "key", "pwd", "salt", "auth")) {
            return "input_password";
        }
        if (containsKeywords(fieldComment, "图片", "头像", "图标", "logo", "avatar") ||
                containsKeywords(fieldName, "image", "icon", "avatar", "logo", "img")) {
            return "image";
        }
        if (containsKeywords(fieldComment, "文件", "附件", "文档", "压缩", "zip", "rar", "7z", "tar", "gz", "pdf", "doc") ||
                containsKeywords(fieldName, "file", "zip", "rar", "7z", "tar", "gz", "pdf", "doc")) {
            return "file";
        }
        if (containsKeywords(fieldComment, "富文本", "html", "markdown", "文章内容") ||
                containsKeywords(fieldName, "html", "markdown")) {
            return "markdown";
        }
        if (containsKeywords(fieldComment, "颜色", "色值") || containsKeywords(fieldName, "color", "hex")) {
            return "color";
        }
        if (containsKeywords(fieldComment, "评分", "星级") || containsKeywords(fieldName, "rate", "rating")) {
            return "rate";
        }
        if (containsKeywords(fieldComment, "滑块", "滑动") || containsKeywords(fieldName, "slider")) {
            return "slider";
        }
        // 先判断开关，避免与 select 冲突
        if (containsKeywords(fieldComment, "开关", "是否") ||
                containsKeywords(fieldName, "is", "enable", "disable", "flag")) {
            return "switch";
        }
        if (containsKeywords(fieldComment, "状态", "类型", "性别", "类别", "等级", "字典") ||
                containsKeywords(fieldName, "status", "type", "gender", "level", "category", "dict")) {
            return "select";
        }
        if (containsKeywords(fieldComment, "生日", "出生") || containsKeywords(fieldName, "birthday")) {
            return "date";
        }
        if (containsKeywords(fieldComment, "日期", "时间") || containsKeywords(fieldName, "date", "time")) {
            return "date_time";
        }
        if (containsKeywords(fieldComment, "年龄", "分数", "成绩", "数字", "价格", "金额") ||
                containsKeywords(fieldName, "age", "score", "grade", "number", "price", "amount")) {
            return "input_number";
        }
        return "input";
    }

    /**
     * 判断查询表单类型
     *
     * @param fieldName    字段名
     * @param fieldType    字段类型
     * @param fieldComment 字段注释
     * @return 查询表单类型
     */
    public static String judgeQueryFormType(String fieldName, String fieldType, String fieldComment) {
        if (containsKeywords(fieldComment, "开关", "是否") ||
                containsKeywords(fieldName, "is", "enable", "disable", "flag")) {
            return "select";
        }
        if (containsKeywords(fieldComment, "状态", "类型", "性别", "类别", "等级", "字典") ||
                containsKeywords(fieldName, "status", "type", "gender", "level", "category", "dict")) {
            return "multi_select";
        }
        if (containsKeywords(fieldComment, "年龄", "分数", "成绩", "数字", "价格", "金额") ||
                containsKeywords(fieldName, "age", "score", "grade", "number", "price", "amount")) {
            return "input_number";
        }
        if (containsKeywords(fieldComment, "生日", "出生") || containsKeywords(fieldName, "birthday")) {
            return "date";
        }
        if (containsKeywords(fieldComment, "日期", "时间") || containsKeywords(fieldName, "date", "time")) {
            return "date_time";
        }
        return "input";
    }

    /**
     * 判断查询类型
     *
     * @param fieldName    字段名
     * @param fieldType    字段类型
     * @param fieldComment 字段注释
     * @return 查询类型
     */
    public static String judgeQueryType(String fieldName, String fieldType, String fieldComment) {
        if (containsKeywords(fieldComment, "年龄", "分数", "成绩", "数字", "价格", "金额", "评分", "星级", "滑块", "滑动") ||
                containsKeywords(fieldName, "age", "score", "grade", "number", "price", "amount", "rate", "rating", "slider")) {
            return "ge";
        }
        if (containsKeywords(fieldComment, "图片", "头像", "图标", "logo", "avatar", "富文本", "html", "markdown", "文章内容"
                , "备注", "描述", "注意", "文件", "附件", "文档", "压缩", "zip", "rar", "7z", "tar", "gz", "pdf", "doc") ||
                containsKeywords(fieldName, "image", "icon", "avatar", "logo", "img",
                        "file", "zip", "rar", "7z", "tar", "gz", "pdf", "html", "markdown",
                        "doc", "remark", "note", "prompt", "description")) {
            return "like";
        }
        if (containsKeywords(fieldComment, "状态", "类型", "性别", "类别", "等级", "字典") ||
                containsKeywords(fieldName, "status", "type", "gender", "level", "category", "dict")) {
            return "in";
        }
        if (containsKeywords(fieldComment, "生日", "出生", "日期", "时间") ||
                containsKeywords(fieldName, "birthday", "date", "time")) {
            return "between";
        }
        return "eq";
    }
}