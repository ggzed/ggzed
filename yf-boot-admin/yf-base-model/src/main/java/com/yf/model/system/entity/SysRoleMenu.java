package com.yf.model.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 角色和菜单关联表-SysRoleMenu
 *
 * @author YiFei
 * @since 2024-04-23 18:43:36
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "角色和菜单关联表")
@TableName("sys_role_menu")
public class SysRoleMenu implements Serializable {

    @Serial
    private static final long serialVersionUID = 708406389758302323L;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    private Integer roleId;

    /**
     * 菜单ID
     */
    @Schema(description = "菜单ID")
    private Integer menuId;

}
