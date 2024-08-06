package com.yf.constraints;

import com.yf.validator.MultipartFileValidator;
import com.yf.validator.MultipartFilesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * MultipartFile 校验
 *
 * @author : YiFei
 * @since : 2024/7/6 18:11
 */
@Documented
@Constraint(validatedBy = {MultipartFilesValidator.class, MultipartFileValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface MultipartFileValid {

    String message() default "Invalid file";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int maxFileNameLength() default 100;

    String[] allowedFileTypes() default {"bmp", "gif", "jpg", "jpeg", "png"};

}
