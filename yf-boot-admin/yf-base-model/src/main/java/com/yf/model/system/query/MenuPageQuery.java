package com.yf.model.system.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 菜单查询参数
 *
 * @author YiFei
 * @since 2024/5/15 15:55
 */
@Schema(description = "菜单分页查询对象")
@Data
public class MenuPageQuery {

    @Schema(description = "关键字(菜单名称)")
    private String keywords;

    @Schema(description = "菜单类型", example = "MENU")
    private Integer type;

    @Schema(description = "是否隐藏(0: 显示, 1: 隐藏)")
    private Integer hidden;

}
