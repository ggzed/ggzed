package com.yf.model.system.query;

import com.yf.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典数据分页查询参数
 *
 * @author : YiFei
 * @since : 2024/7/30 1:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictDataPageQuery extends BasePageQuery {

    /**
     * 字典项名称
     */
    @Schema(description = "字典项名称")
    private String name;

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
}
