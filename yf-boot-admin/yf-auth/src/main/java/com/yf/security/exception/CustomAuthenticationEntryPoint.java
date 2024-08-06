package com.yf.security.exception;

import com.yf.model.result.ResultCode;
import com.yf.utils.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 认证异常处理
 *
 * @author : YiFei
 * @since : 2023/9/24 19:53
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        int status = response.getStatus();
        if (status == HttpServletResponse.SC_NOT_FOUND) {
            // 资源不存在
            ResponseUtil.writeIResultCodeMsg(response, HttpStatus.BAD_REQUEST, ResultCode.REQUEST_RESOURCE_NOT_FOUND);
        } else {
            // 未认证或者token过期
            ResponseUtil.writeIResultCodeMsg(response, HttpStatus.BAD_REQUEST, ResultCode.AUTH_ACCESS_UNAUTHORIZED);
        }
    }
}
