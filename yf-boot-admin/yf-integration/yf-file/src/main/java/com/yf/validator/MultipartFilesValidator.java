package com.yf.validator;

import com.yf.constraints.MultipartFileValid;
import com.yf.utils.FileUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
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

    @Override
    public void initialize(MultipartFileValid constraintAnnotation) {
        this.maxFileNameLength = constraintAnnotation.maxFileNameLength();
        this.allowedFileTypes = constraintAnnotation.allowedFileTypes();
    }

    @Override
    public boolean isValid(MultipartFile[] files, ConstraintValidatorContext context) {
        return Arrays.stream(files).allMatch(file -> {
            String originalFilename = file.getOriginalFilename();
            // 校验扩展名 && 文件名
            return FileUtils.isAllowedExtension(file, this.allowedFileTypes)
                    && StringUtils.hasText(originalFilename)
                    && originalFilename.length() <= this.maxFileNameLength;
        });
    }
}
