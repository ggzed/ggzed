package com.yf.serializer;

import cn.hutool.core.text.CharSequenceUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.yf.model.enums.DesensitizationTypeEnum;
import com.yf.serializer.annotation.Desensitization;
import com.yf.utils.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * 自定义序列化类
 *
 * @author YiFei
 * @since 2024/3/6 13:02
 */
@AllArgsConstructor
@NoArgsConstructor
public class DesensitizationSerialize extends JsonSerializer<String> implements ContextualSerializer {

    private DesensitizationTypeEnum type;

    private Integer startInclude;

    private Integer endExclude;

    @Override
    public void serialize(String str, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // 展示给管理员的数据不脱敏
        if (SecurityUtil.isAdmin()) {
            jsonGenerator.writeString(str);
            return;
        }
        // 不为管理员则数据脱敏
        if (type.equals(DesensitizationTypeEnum.CUSTOMIZE_RULE)) {
            jsonGenerator.writeString(CharSequenceUtil.hide(type.getSerialize().apply(str), startInclude, endExclude));
        } else {
            jsonGenerator.writeString(type.getSerialize().apply(str));
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            // 检查数据类型是否为String类型
            if (beanProperty.getType().getRawClass() == String.class) {
                // 获取定义的注解
                Desensitization desensitization = beanProperty.getAnnotation(Desensitization.class);
                if (desensitization != null) {
                    // 如果注解不为null，则创建并返回DesensitizationSerialize实例
                    return new DesensitizationSerialize(desensitization.type(),
                            desensitization.startInclude(), desensitization.endExclude());
                }
            }
            // 返回默认的值序列化器
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        // 如果BeanProperty为null，则返回空值序列化器
        return serializerProvider.findNullValueSerializer(null);
    }
}
