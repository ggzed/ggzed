package com.yf.model.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yf.base.BaseEntity;
import com.yf.model.system.enums.MenuTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统菜单-SysMenu
 *
 * @author YiFei
 * @since 2024-04-23 18:43:35
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统菜单")
@TableName("sys_menu")
public class SysMenu extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 489248903756093270L;

    /**
     * 菜单主键
     */
    @Schema(description = "菜单主键")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 父菜单ID
     */
    @Schema(description = "父菜单ID")
    private Integer parentId;

    /**
     * 父节点ID路径
     */
    @Schema(description = "父节点ID路径")
    private String treePath;

    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称")
    private String name;

    /**
     * 菜单类型(1:目录；2:菜单；3:外链；4:按钮)
     */
    @Schema(description = "菜单类型(1:目录；2:菜单；3:外链；4:按钮)")
    private MenuTypeEnum type;

    /**
     * 路由路径(浏览器地址栏路径)
     */
    @Schema(description = "路由路径(浏览器地址栏路径)")
    private String path;

    /**
     * 组件路径(vue页面完整路径，省略.vue后缀)
     */
    @Schema(description = "组件路径(vue页面完整路径，省略.vue后缀)")
    private String component;

    /**
     * 权限标识
     */
    @Schema(description = "权限标识")
    private String permission;

    /**
     * 显示状态(0-显示;1-隐藏)
     */
    @Schema(description = "菜单是否可见(0: 显示 ; 1: 隐藏)")
    private Integer hidden;

    /**
     * 【菜单】是否开启页面缓存(1:是 0:否)
     */
    @Schema(description = "【菜单】是否开启页面缓存(1:是 0:否)")
    private Integer keepAlive;

    /**
     * 【目录】是否折叠单个子菜单(1:是 0:否)
     */
    @Schema(description = "【目录】是否折叠单个子菜单(1:是 0:否)")
    private Integer showSingleChildren;

    /**
     * 【菜单】是否固定到 TagsView 上面(1:是 0:否)
     */
    @Schema(description = "【菜单】是否固定到 TagsView 上面(1:是 0:否)")
    private Integer affix;

    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标")
    private String icon;

    /**
     * 标题显示在面包屑上(1: 是 ,0:否)
     */
    @Schema(description = "标题显示在面包屑上(1:是 ,0:否)")
    private Integer breadcrumb;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 跳转路径
     */
    @Schema(description = "跳转路径")
    private String redirect;

}
