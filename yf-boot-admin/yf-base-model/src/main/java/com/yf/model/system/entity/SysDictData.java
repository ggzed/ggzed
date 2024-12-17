package com.yf.model.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.yf.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 字典数据表-SysDictData
 *
 * @author YiFei
 * @since 2024-04-23 18:52:09
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "字典数据表")
@TableName("sys_dict_data")
public class SysDictData extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -32271707234919556L;


    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 字典类型编码
     */
    @Schema(description = "字典类型编码")
    private String dictType;

    /**
     * 字典项名称
     */
    @Schema(description = "字典项名称")
    private String name;

    /**
     * 字典项值
     */
    @Schema(description = "字典项值")
    private String value;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 状态(1:正常;0:禁用)
     */
    @Schema(description = "状态(1:正常;0:禁用)")
    private Integer status;

    /**
     * 是否默认(1:是;0:否)
     */
    @Schema(description = "是否默认(1:是;0:否)")
    private Integer defaulted;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    private Long createBy;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改人")
    private Long updateBy;

}
