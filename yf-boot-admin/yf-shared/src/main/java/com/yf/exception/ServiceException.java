package com.yf.exception;

import com.yf.base.IResultCode;
import lombok.Getter;


/**
 * 自定义业务异常类
 *
 * @author : YiFei
 * @since : 2022/10/2 17:31
 */
@Getter
public class ServiceException extends RuntimeException {

    public IResultCode resultCode;

    public ServiceException(IResultCode errorCode) {
        super(errorCode.getMsg());
        this.resultCode = errorCode;
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }


}