package com.yf.oss.validator;

import com.yf.oss.constraints.MultipartFileValid;
import com.yf.oss.utils.FileUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

/**
 * MultipartFiles 校验
 *
 * @author : YiFei
 * @since : 2024/7/6 18:17
 */
public class MultipartFilesValidator implements ConstraintValidator<MultipartFileValid, MultipartFile[]> {

    private int maxFileNameLength;
    private String[] allowedFileTypes;
    private boolean required;

    @Override
    public void initialize(MultipartFileValid constraintAnnotation) {
        this.maxFileNameLength = constraintAnnotation.maxFileNameLength();
        this.allowedFileTypes = constraintAnnotation.allowedFileTypes();
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(MultipartFile[] files, ConstraintValidatorContext context) {
        boolean exist = ObjectUtils.isEmpty(files);
        // 如果文件是必须的，且文件为空，直接返回 false
        if (required && exist) {
            return false;
        }

        // 如果文件不是必须的，且文件为空，返回 true
        if (exist) {
            return true;
        }
        return Arrays.stream(files).allMatch(file -> {
            String originalFilename = file.getOriginalFilename();
            // 校验扩展名 && 文件名
            return FileUtils.isAllowedExtension(file, this.allowedFileTypes)
                    && StringUtils.hasText(originalFilename)
                    && originalFilename.length() <= this.maxFileNameLength;
        });
    }
}
