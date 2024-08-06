package com.yf.utils;

import cn.hutool.json.JSONUtil;
import com.yf.base.IResultCode;
import com.yf.model.result.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * 请求响应工具类
 *
 * @author : YiFei
 * @since : 2023/9/24 19:34
 */
public class ResponseUtil {


    /**
     * 异常消息返回(适用过滤器中处理异常响应)
     */
    public static void writeIResultCodeMsg(HttpServletResponse response, HttpStatus status, IResultCode resultCode) throws IOException {
        // 设置响应头为
        response.setStatus(status.value());
        // 设置响应内容
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(JSONUtil.toJsonStr(Result.failed(resultCode)));
    }
}
