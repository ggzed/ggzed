package com.yf.model.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yf.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 部门表-SysDept
 *
 * @author YiFei
 * @since 2024-04-23 18:43:34
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "部门表")
@TableName("sys_dept")
public class SysDept extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -18790763833759141L;

    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    private String name;

    /**
     * 父节点id
     */
    @Schema(description = "父节点id")
    private Integer parentId;

    /**
     * 父节点id路径
     */
    @Schema(description = "父节点id路径")
    private String treePath;

    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序")
    private Integer sort;

    /**
     * 状态(1:正常;0:禁用)
     */
    @Schema(description = "状态(1:正常;0:禁用)")
    private Integer status;

}
