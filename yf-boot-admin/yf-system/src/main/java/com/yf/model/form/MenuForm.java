package com.yf.model.form;

import com.yf.model.enums.MenuTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 菜单表单
 *
 * @author YiFei
 * @since 2024/5/15 19:34
 */
@Data
public class MenuForm {

    @Schema(description = "父菜单ID")
    @NotNull(message = "父菜单ID不能为空")
    private Integer parentId;

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "菜单类型(1-菜单；2-目录；3-外链；4-按钮权限)")
    private MenuTypeEnum type;

    @Schema(description = "路由路径")
    private String path;

    @Schema(description = "组件路径(vue页面完整路径，省略.vue后缀)")
    private String component;

    @Schema(description = "权限标识")
    private String permission;

    @Schema(description = "是否固定(0 : 否 , 1: 是)", example = "1")
    private Integer affix;

    @Schema(description = "显示状态(0: 显示;1: 隐藏)", example = "0")
    private Integer hidden;

    @Schema(description = "排序(数字越小排名越靠前)")
    private Integer sort;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "跳转路径")
    private String redirect;

    @Schema(description = "【菜单】是否开启页面缓存(1:是 0:否)", example = "1")
    private Integer keepAlive;

    @Schema(description = "【目录】是否折叠单个子菜单(1:是 0:否)", example = "1")
    private Integer showSingleChildren;

}
