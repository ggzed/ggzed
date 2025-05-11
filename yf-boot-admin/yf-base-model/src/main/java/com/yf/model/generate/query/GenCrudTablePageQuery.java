package com.yf.model.generate.query;

import com.yf.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 代码生成查询参数
 *
 * @author : YiFei
 * @since : 2025/3/30 15:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GenCrudTablePageQuery extends BasePageQuery {

    @Schema(description = "作者")
    private String functionAuthor;

    @Schema(description = "数据库表名")
    private String tableName;

    @Schema(description = "备注")
    private String remark;
}
