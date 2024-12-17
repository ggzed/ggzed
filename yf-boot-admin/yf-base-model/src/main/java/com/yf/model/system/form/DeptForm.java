package com.yf.model.system.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeptForm {
    @Schema(description = "父部门ID")
    @NotNull(message = "父部门ID不能为空")
    private Integer parentId;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "显示状态(0: 显示;1: 隐藏)", example = "0")
    private Integer status;

    @Schema(description = "排序(数字越小排名越靠前)")
    private Integer sort;
}
