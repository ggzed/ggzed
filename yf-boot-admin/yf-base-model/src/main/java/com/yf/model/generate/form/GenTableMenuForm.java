package com.yf.model.generate.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * GenTableMenuForm
 *
 * @author : YiFei
 * @since : 2025/4/23 23:51
 */
@Data
public class GenTableMenuForm {

    /**
     * 父菜单ID
     */
    @Schema(description = "父菜单ID")
    @NotNull(message = "父菜单ID不能为空")
    private Integer parentId;

    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称")
    @NotBlank(message = "菜单名不能为空")
    private String menuName;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 隐藏状态
     */
    @Schema(description = "隐藏状态(0: 显示;1: 隐藏)", example = "0")
    @NotNull(message = "隐藏状态不能为空")
    private Integer hidden;
}
