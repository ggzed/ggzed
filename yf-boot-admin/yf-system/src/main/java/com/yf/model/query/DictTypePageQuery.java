package com.yf.model.query;

import com.yf.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典类型的查询
 *
 * @author : YiFei
 * @since : 2024/7/30 1:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictTypePageQuery extends BasePageQuery {

    /**
     * 字典名称
     */
    @Schema(description = "字典名称")
    private String name;

    /**
     * 字典类型
     */
    @Schema(description = "字典类型")
    private String type;

    /**
     * 状态（1正常 0停用）
     */
    @Schema(description = "状态（1正常 0停用）")
    private Integer status;

}
