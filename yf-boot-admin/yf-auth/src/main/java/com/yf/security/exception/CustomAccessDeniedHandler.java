package com.yf.security.exception;

import com.yf.model.result.ResultCode;
import com.yf.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Spring Security访问异常处理器
 *
 * @author : YiFei
 * @since : 2023/9/24 19:53
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ResponseUtil.writeIResultCodeMsg(response, HttpStatus.BAD_REQUEST, ResultCode.AUTH_ACCESS_UNAUTHORIZED);
    }

}
