package com.yf.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yf.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 角色表-SysRole
 *
 * @author YiFei
 * @since 2024-04-23 18:43:36
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色表")
@TableName("sys_role")
public class SysRole extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -69347400941032255L;


    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String name;

    /**
     * 角色编码
     */
    @Schema(description = "角色编码")
    private String code;

    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序")
    private Integer sort;

    /**
     * 角色状态(1-正常；0-停用)
     */
    @Schema(description = "角色状态(1-正常；0-停用)")
    private Integer status;

    /**
     * 数据权限(0-所有数据；1-部门及子部门数据；2-本部门数据；3-本人数据)
     */
    @Schema(description = "数据权限(0-所有数据；1-部门及子部门数据；2-本部门数据；3-本人数据)")
    private Integer dataScope;

}
