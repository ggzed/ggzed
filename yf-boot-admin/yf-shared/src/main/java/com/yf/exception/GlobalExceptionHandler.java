package com.yf.exception;

import com.yf.model.result.Result;
import com.yf.model.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 统一错误校验
 *
 * @author yiFei
 * @since 2024/3/6 20:41
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> Exception(Exception e) {
        log.error("未捕获错误 ======>  {} \n  错误类 ======> {}", e.getMessage(), e.getClass(), e);
        return Result.customize(ResultCode.SYSTEM_EXECUTION_ERROR);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<?> NoResourceFoundException(NoResourceFoundException e) {
        return Result.customize(ResultCode.FILE_NOT_FOUND);
    }

    @ExceptionHandler(ServiceException.class)
    public Result<Void> ServiceException(ServiceException e) {
        log.error("ServiceException错误信息 =======> {}", e.getMessage(), e);
        if (e.getResultCode() == null) {
            return Result.customize(ResultCode.FAIL.getCode(), e.getMessage());
        }
        return Result.customize(e.getResultCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Map<String, String>> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题{}，异常类型：{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach((fieldError) -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return Result.customize(ResultCode.REQUEST_PARAMETER_ERROR, errorMap);
    }

    /**
     * 由于全局异常捕获了 security 的错误，导致无法正确进入 AccessDeniedHandler
     */
    @ExceptionHandler(AccessDeniedException.class)
    public void AccessDeniedException(AccessDeniedException e) {
        throw e;
    }


    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidationException(HandlerMethodValidationException ex) {
        return Result.customize(ResultCode.FAIL.getCode(), Objects.requireNonNull(ex.getDetailMessageArguments())[0].toString());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        return Result.failed(ResultCode.FILE_MAX_UPLOAD_SIZE);
    }

    @ExceptionHandler(MyBatisSystemException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleMyBatisSystemException(MyBatisSystemException ex) {
        return Result.failed(ResultCode.DB_SERVER_NOT_ENABLED);
    }
}
