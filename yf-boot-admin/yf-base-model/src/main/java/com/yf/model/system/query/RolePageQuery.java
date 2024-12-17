package com.yf.model.system.query;

import com.yf.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色分页查询参数
 *
 * @author YiFei
 * @since 2024/5/13 9:46
 */
@Schema(description = "角色分页查询参数")
@Data
@EqualsAndHashCode(callSuper = true)
public class RolePageQuery extends BasePageQuery {

    @Schema(description = "关键字(角色名/角色编码)")
    private String keywords;
}
