package com.yf.model.generate.query;

import com.yf.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据库表分页查询
 *
 * @author : YiFei
 * @since : 2025/3/30 16:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DBTablePageQuery extends BasePageQuery {

    @Schema(description = "数据库表名" )
    private String tableName;
}
