package com.yf.websocket.interceptor;

import com.yf.annotation.RateLimiter;
import com.yf.annotation.RateLimiters;
import com.yf.annotation.RateRule;
import com.yf.constants.WebSocketConstant;
import com.yf.model.dto.SessionConnectInfo;
import com.yf.model.dto.SysUserDetails;
import com.yf.model.enums.LimitTypeEnum;
import com.yf.utils.AddressUtil;
import com.yf.utils.IpUtil;
import com.yf.utils.SecurityUtil;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Socket 连接权限校验拦截器
 *
 * @author yiFei
 * @since 2024/3/12 13:21
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRoomAuthInterceptor extends HttpSessionHandshakeInterceptor {

    // 一天访问只能链接10个
    @PreAuthorize("@permission.checker('websocket:chat-room:list')")
    @RateLimiters(rateLimiters = {
            @RateLimiter(
                    limitTypeEnum = LimitTypeEnum.USER_ID,
                    addToBlacklist = true,
                    rateRules = {@RateRule(
                            limit = 10,
                            timeDuration = 24,
                            timeUnit = TimeUnit.HOURS
                    )
            })
    })
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 1. 判断用户是否登录
        SysUserDetails user = SecurityUtil.getUser();
        if (user == null) {
            log.debug("连接 websocket 失败未登录");
            return false;
        }
        // 2. 设置用户信息
        attributes.put(WebSocketConstant.SOCKET_SESSION_USER_ID, user);
        // 3. 记录本次连接信息
        SessionConnectInfo sessionConnectInfo = getSessionConnectInfo((ServletServerHttpRequest) request);
        attributes.put(WebSocketConstant.SOCKET_SESSION_CONNECT_INFO, sessionConnectInfo);
        /* 源码 : 会对本地请求的参数进行记录
               attributes.put(HTTP_SESSION_ID_ATTR_NAME, session.getId());
               attributes.put(name, session.getAttribute(name));
        */
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    /**
     * 获取用户连接信息
     *
     * @param request 请求
     * @return SessionConnectInfo
     */
    private SessionConnectInfo getSessionConnectInfo(ServletServerHttpRequest request) {
        HttpServletRequest servletRequest = request.getServletRequest();
        String ipAddr = IpUtil.getIpAddr(servletRequest);
        // 1. 获取 user-agent 方便后续获取 os，browser
        String headerUserAgent = servletRequest.getHeader("User-Agent");
        // 1.1 解析 userAgent
        UserAgent userAgent = UserAgent.parseUserAgentString(headerUserAgent);
        // 1.2 填写 userAgent 信息 （浏览器及操作系统）
        String realAddressByIP = AddressUtil.getRealAddressByIP(ipAddr);
        // 2. 存储本次连接信息
        return SessionConnectInfo.builder()
                .operatorBrowser(userAgent.getBrowser().getName())
                .operatorIp(ipAddr)
                .operatorOs(userAgent.getOperatingSystem().getName())
                .operatorLocation(realAddressByIP)
                .build();
    }

}
