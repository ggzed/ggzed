package com.yf.model.websocket.dto;

import lombok.Builder;
import lombok.Data;

/**
 * session 连接信息
 *
 * @author YiFei
 * @since 2024/5/24 12:36
 */
@Data
@Builder
public class SessionConnectInfo {
    /**
     * 主机地址
     */
    private String operatorIp;
    /**
     * 操作地点
     */
    private String operatorLocation;
    /**
     * 浏览器信息
     */
    private String operatorBrowser;
    /**
     * 操作系统
     */
    private String operatorOs;
}
