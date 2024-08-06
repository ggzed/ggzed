package com.yf.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigInteger;
import java.util.List;

@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {


    /**
     * 精度丢失问题配置
     *
     * @param converters 转化器
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(customJackson2HttpMessageConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        // UNKNOWN 字段不解析
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 将Long和BigInteger类型序列化为字符串
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, new ToStringSerializer());
        module.addSerializer(Long.TYPE, new ToStringSerializer());
        module.addSerializer(BigInteger.class, new ToStringSerializer());

        // 注册处理Java 8+ 日期时间的模块
        objectMapper.registerModule(module);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules(); // 自动注册所有找到的模块

        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    /**
     * TODO 没有 Bean 注入 , JSR303 校验优化
     */
    @Bean
    public Validator validator(final AutowireCapableBeanFactory autowireCapableBeanFactory) {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true) // failFast=true 不校验所有参数，只要出现校验失败情况直接返回，不再进行后续参数校验
                .constraintValidatorFactory(new SpringConstraintValidatorFactory(autowireCapableBeanFactory))
                .buildValidatorFactory();

        return validatorFactory.getValidator();
    }


//    /**
//     * 后续跨域请求配置 上线取消配置 用 Nginx 代理
//     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        // 设置允许跨域的路径
//        registry.addMapping("/**")
//                // 设置允许跨域请求的域名
//                .allowedOriginPatterns("*")
//                // 是否允许cookie
//                .allowCredentials(true)
//                // 设置允许的请求方式
//                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")
//                // 设置允许的header属性
//                .allowedHeaders("*")
//                // 跨域允许时间
//                .maxAge(3600);
//    }
}
