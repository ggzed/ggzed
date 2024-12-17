package com.yf.model.system.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author YiFei
 * @since 2024/5/13 14:20
 */
@Data
@Schema(description = "角色表单")
public class RoleForm {

    @Schema(description = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    private String name;

    @Schema(description = "角色编码")
    @NotBlank(message = "角色编码不能为空")
    private String code;

    @Schema(description = "显示顺序")
    private Integer sort;

    @Schema(description = "角色状态(1-正常；0-停用)")
    private Integer status;

    @Schema(description = "数据权限(0-所有数据；1-部门及子部门数据；2-本部门数据；3-本人数据)")
    private Integer dataScope;

}
