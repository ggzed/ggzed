package com.yf.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字典数据表单
 *
 * @author : YiFei
 * @since : 2024/7/30 1:41
 */
@Data
@Schema(description = "字典数据表单")
public class DictDataForm {

    /**
     * 字典类型Id
     */
    @Schema(description = "字典类型Id")
    private Integer dictTypeId;

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
}
