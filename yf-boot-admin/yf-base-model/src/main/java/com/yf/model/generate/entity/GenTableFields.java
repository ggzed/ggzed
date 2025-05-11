package com.yf.model.generate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yf.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * GenTableFields
 *
 * @author YiFei
 * @since 2024-06-14 16:53:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "GenTableFields" )
@TableName("gen_table_fields" )
public class GenTableFields extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -80243610386985556L;

    /**
     * 主键
     */
    @Schema(description = "主键" )
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 所属表ID
     */
    @Schema(description = "所属表ID" )
    private Integer tableId;

    /**
     * 表格展示列名
     */
    @Schema(description = "表格展示列名" )
    private String showName;

    /**
     * 数据库列名
     */
    @Schema(description = "数据库列名" )
    private String columnName;

    /**
     * 数据库字段描述
     */
    @Schema(description = "数据库字段描述" )
    private String columnComment;

    /**
     * 数据库类型
     */
    @Schema(description = "数据库类型" )
    private String columnType;

    /**
     * JAVA类型
     */
    @Schema(description = "JAVA类型" )
    private String javaType;

    /**
     * JAVA&TS字段名
     */
    @Schema(description = "JAVA&TS字段名" )
    private String javaTsFieldName;

    /**
     * TypeScript类型
     */
    @Schema(description = "TypeScript类型" )
    private String tsType;

    /**
     * 是否主键（1:是,0:否）
     */
    @Schema(description = "是否主键（1:是,0:否）" )
    private Boolean isPk;

    /**
     * 是否自增（1:是,0:否）
     */
    @Schema(description = "是否自增（1:是,0:否）" )
    private Boolean isIncrement;

    /**
     * 是否必填（1:是,0:否）
     */
    @Schema(description = "是否必填（1:是,0:否）" )
    private Boolean isRequired;

    /**
     * 是否表单字段（1:是,0:否）
     */
    @Schema(description = "是否表单字段（1:是,0:否）" )
    private Boolean isForm;

    /**
     * 是否展示字段（1:是,0:否）
     */
    @Schema(description = "是否展示字段（1:是,0:否）" )
    private Boolean isShow;

    /**
     * 是否查询字段（1:是,0:否）
     */
    @Schema(description = "是否查询字段（1:是,0:否）" )
    private Boolean isQuery;

    /**
     * 展示类型 ( JSON、文本、Markdown、Tag、图片... )
     */
    @Schema(description = "展示类型 ( JSON、文本、Markdown、Tag、图片... )" )
    private String showType;

    /**
     * 查询方式（等于、不等于、大于、小于、范围）
     */
    @Schema(description = "查询方式（等于、不等于、大于、小于、范围）" )
    private String queryType;

    /**
     * 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    @Schema(description = "显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）" )
    private String queryFormType;

    /**
     * 表单类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    @Schema(description = "表单类型（文本框、文本域、下拉框、复选框、单选框、日期控件）" )
    private String saveFormType;

    /**
     * 字典类型
     */
    @Schema(description = "字典类型" )
    private String dictTypeName;

    /**
     * 排序
     */
    @Schema(description = "排序" )
    private Integer sort;

}
