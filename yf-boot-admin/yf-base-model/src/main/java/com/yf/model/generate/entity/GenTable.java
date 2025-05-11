package com.yf.model.generate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yf.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * GenTable
 *
 * @author YiFei
 * @since 2024-06-14 16:52:45
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "GenTable")
@EqualsAndHashCode(callSuper = true)
@TableName("gen_table")
public class GenTable extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 380941061129062827L;

    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单ID
     */
    @Schema(description = "菜单ID")
    private Integer menuId;

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
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

}
