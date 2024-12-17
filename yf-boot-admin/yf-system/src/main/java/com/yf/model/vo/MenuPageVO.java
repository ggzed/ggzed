package com.yf.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yf.model.common.ITreeNode;
import com.yf.model.system.enums.MenuTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 菜单分页VO
 *
 * @author YiFei
 * @since 2024/5/15 15:54
 */
@Data
public class MenuPageVO implements ITreeNode<Integer, MenuPageVO> {

    @Schema(description = "菜单ID")
    private Integer id;

    @Schema(description = "父菜单ID")
    private Integer parentId;

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "菜单类型")
    private MenuTypeEnum type;

    @Schema(description = "路由路径")
    private String path;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "菜单排序(数字越小排名越靠前)")
    private Integer sort;

    @Schema(description = "是否固定(0 : 否 , 1: 是)")
    private Integer affix;

    @Schema(description = "菜单是否可见(0: 显示 ; 1: 隐藏)")
    private Integer hidden;

    @Schema(description = "ICON")
    private String icon;

    @Schema(description = "跳转路径")
    private String redirect;

    @Schema(description = "按钮权限标识")
    private String permission;

    @Schema(description = "子菜单")
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private List<MenuPageVO> children;
}
