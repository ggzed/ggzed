package com.yf.model.system.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "部门分页查询对象")
@Data
public class DeptPageQuery {
    @Schema(description = "关键字(部门名称)")
    private String keywords;

    @Schema(description = "是否隐藏(0: 显示, 1: 隐藏)")
    private Integer status;
}
