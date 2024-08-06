package com.yf.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yf.model.ITreeNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 菜单分页VO
 *
 * @author YiFei
 * @since 2024/5/15 15:54
 */
@Data
public class DeptPageVo implements ITreeNode<DeptPageVo> {

    @Schema(description = "部门ID")
    private Integer id;

    @Schema(description = "父部门ID")
    private Integer parentId;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "部门排序(数字越小排名越靠前)")
    private Integer sort;

    @Schema(description = "部门是否可见(0: 显示 ; 1: 隐藏)")
    private Integer status;

    @Schema(description = "子部门")
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private List<DeptPageVo> children;
}
