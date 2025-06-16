package com.yf.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * ToolContext 所需数据
 *
 * @author : YiFei
 * @since : 2025/6/6 14:22
 */
@Data
@Builder
public class ToolContextDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户角色
     */
    private Set<String> roles;

    /**
     * 用户 IP
     */
    private String ip;

    /**
     * User Agent 头部信息
     */
    private String headerUserAgent;
}
