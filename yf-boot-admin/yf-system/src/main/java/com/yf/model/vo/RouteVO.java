package com.yf.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yf.model.system.dto.RouteMeta;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Caffeine 配置
 *
 * @author YiFei
 * @since 2024/4/29 19:13
 */
@Schema(description = "路由Vo对象")
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouteVO implements Serializable {

    @Schema(description = "路由路径", example = "template")
    private String path;

    @Schema(description = "组件路径", example = "home/index")
    private String component;

    @Schema(description = "跳转链接", example = "/template/one")
    private String redirect;

    @Schema(description = "路由名称")
    private String name;

    @Schema(description = "路由属性")
    private RouteMeta meta;

    @Schema(description = "子路由列表")
    private List<RouteVO> children = new ArrayList<>();
}