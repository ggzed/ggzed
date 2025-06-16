package com.yf.converter;

import com.yf.annotation.ToolCallResultDesc;
import org.springframework.ai.tool.execution.ToolCallResultConverter;
import org.springframework.ai.util.json.JsonParser;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 实体工具调用结果转换器
 *
 * @author : YiFei
 * @since : 2025/5/20 0:04
 */
public class ToolCallResultDescConverter implements ToolCallResultConverter {

    // 将工具调用的结果对象转换为字符串
    @Override
    public String convert(Object result, Type returnType) {
        if (result == null) return ""; // 如果结果为 null，返回空字符串
        if (returnType == Void.TYPE) return JsonParser.toJson("Done"); // 如果返回类型为 void，返回 "Done"
        return parseObject(result).toString(); // 否则，解析对象字段并返回结果
    }

    /**
     * 解析对象的字段，将带有 ToolCallResultDesc 注解的字段拼接为字符串
     */
    private StringBuilder parseObject(Object obj) {
        StringBuilder sb = new StringBuilder();
        boolean isFirstField = true; // 标识是否为第一个字段（用于控制逗号）

        // 遍历对象的所有字段
        for (Field field : obj.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true); // 设置字段可访问
                ToolCallResultDesc display = field.getAnnotation(ToolCallResultDesc.class); // 获取注解
                if (field.get(obj) == null) continue; // 跳过值为 null 的字段

                // 如果不是第一个字段，添加逗号分隔
                if (!isFirstField) {
                    sb.append(", ");
                } else {
                    isFirstField = false;
                }

                Object value = field.get(obj); // 获取字段值
                // 拼接注解中的描述和值
                sb.append(display.value() == null ? field.getName() : display.value())
                        .append(": ")
                        .append(parseValue(value));
            } catch (IllegalAccessException ignored) {
                // 访问字段出错时忽略异常
            }
        }
        return sb;
    }

    /**
     * 解析字段值，支持嵌套对象和集合
     *
     * @param value 字段值
     * @return 解析后的字符串
     */
    private StringBuilder parseValue(Object value) {
        StringBuilder sb = new StringBuilder();

        if (isCustomObject(value)) {
            // 如果是自定义对象，递归解析其字段
            sb.append("[ ")
                    .append(parseObject(value))
                    .append(" ]");
        } else if (value instanceof Collection<?> col && !col.isEmpty()) {
            // 如果是非空集合，逐个解析集合元素
            sb.append("[ ")
                    .append(col.stream()
                            .map(this::parseValue)
                            .collect(Collectors.joining(", ")))
                    .append(" ]");
        } else {
            // 其他类型直接使用 toString()
            sb.append(value);
        }

        return sb;
    }

    /**
     * 判断是否为自定义对象（排除 JDK 类、原始类型和枚举）
     */
    private boolean isCustomObject(Object obj) {
        return !obj.getClass().getName().startsWith("java.") &&
                !obj.getClass().isPrimitive() &&
                !obj.getClass().isEnum();
    }
}
