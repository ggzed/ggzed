package com.yf.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 角色分页VO
 *
 * @author YiFei
 * @since 2024/5/13 9:46
 */
@Data
public class RolePageVO {

    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色编码")
    private String code;

    @Schema(description = "角色状态")
    private Integer status;

    @Schema(description = "排序")
    private Integer sort;
}
