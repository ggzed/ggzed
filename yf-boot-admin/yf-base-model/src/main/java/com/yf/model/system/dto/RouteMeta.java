package com.yf.model.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Schema(description = "路由属性类型")
@Data
public class RouteMeta implements Serializable {

    @Schema(description = "路由title")
    private String title;

    @Schema(description = "ICON")
    private String icon;

    @Schema(description = "是否隐藏(true-是 false-否)", example = "false")
    private Boolean hidden;

    @Schema(description = "【菜单】是否开启页面缓存", example = "true")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean keepAlive;

    @Schema(description = "是否固定页签(true-是 false-否)", example = "false")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean affix;

    @Schema(description = "是否显示在面包屑(true-是 false-否)", example = "true")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean breadcrumb;

    @Schema(description = "【目录】只有一个子路由是否始终显示", example = "true")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean showSingleChildren;

    @Schema(description = "拥有路由权限的角色编码", example = "['ADMIN','ROOT']")
    private List<String> roles;
}