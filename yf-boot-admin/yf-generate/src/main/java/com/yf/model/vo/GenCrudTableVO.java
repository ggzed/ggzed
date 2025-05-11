package com.yf.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 生成 CRUD 代码 VO
 *
 * @author : YiFei
 * @since : 2025/3/30 15:46
 */
@Data
public class GenCrudTableVO {

    /**
     * TableId
     */
    @Schema(description = "主键")
    private Integer id;

    /**
     * 菜单名
     */
    @Schema(description = "菜单名")
    private String menuName;

    /**
     * 作者
     */
    @Schema(description = "作者")
    private String functionAuthor;

    /**
     * 数据库表名
     */
    @Schema(description = "数据库表名")
    private String tableName;

    /**
     * 数据库表描述
     */
    @Schema(description = "数据库表描述")
    private String tableComment;

    /**
     * 类名(根据数据库表名生成)
     */
    @Schema(description = "类名(根据数据库表名生成)")
    private String className;

    /**
     * 前端 component 名(根据数据库表名生成)
     */
    @Schema(description = "前端 component 名(根据数据库表名生成)")
    private String componentName;

    /**
     * 后端生成类型
     */
    @Schema(description = "后端生成类型")
    private String backEndType;

    /**
     * 前端生成类型
     */
    @Schema(description = "前端生成类型")
    private String frontEndType;

    /**
     * 主包名
     */
    @Schema(description = "主包名")
    private String packageName;

    /**
     * 模块名（外层包名）
     */
    @Schema(description = "模块名（外层包名）")
    private String moduleName;

    /**
     * 业务名（内层包名）
     */
    @Schema(description = "业务名（内层包名）")
    private String businessName;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
