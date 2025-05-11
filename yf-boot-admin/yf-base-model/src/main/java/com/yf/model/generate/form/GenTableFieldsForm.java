package com.yf.model.generate.form;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * GenTableFieldsForm
 *
 * @author YiFei
 * @since 2025-04-02 16:53:13
 */
@Data
public class GenTableFieldsForm {

    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 表格展示列名
     */
    @NotBlank(message = "表格展示列名不能为空")
    @Size(max = 255, message = "表格展示列名最大值255")
    @Schema(description = "表格展示列名")
    private String showName;

    /**
     * 数据库列名
     */
    @NotBlank(message = "数据库列名不能为空")
    @Size(max = 64, message = "数据库列名最大值64")
    @Schema(description = "数据库列名")
    private String columnName;

    /**
     * 数据库字段描述
     */
    @Size(max = 500, message = "数据库字段描述最大值500")
    private String columnComment;

    /**
     * 数据库类型
     */
    @NotBlank(message = "数据库类型不能为空")
    @Size(max = 64, message = "数据库类型最大值64")
    @Schema(description = "数据库类型")
    private String columnType;

    /**
     * JAVA类型
     */
    @NotBlank(message = "JAVA类型不能为空")
    @Size(max = 64, message = "JAVA类型最大值64")
    @Schema(description = "JAVA类型")
    private String javaType;

    /**
     * JAVA&TS字段名
     */
    @NotBlank(message = "字段名不能为空")
    @Size(max = 64, message = "字段名最大值64")
    @Schema(description = "JAVA&TS字段名")
    private String javaTsFieldName;

    /**
     * TypeScript类型
     */
    @NotBlank(message = "TypeScript类型不能为空")
    @Size(max = 64, message = "TypeScript类型最大值64")
    @Schema(description = "TypeScript类型")
    private String tsType;

    /**
     * 是否主键（1:是,0:否）
     */
    @NotBlank(message = "是否主键不能为空")
    @Schema(description = "是否主键（1:是,0:否）")
    private Boolean isPk;

    /**
     * 是否自增（1:是,0:否）
     */
    @NotBlank(message = "是否自增不能为空")
    @Schema(description = "是否自增（1:是,0:否）")
    private Boolean isIncrement;

    /**
     * 是否必填（1:是,0:否）
     */
    @NotBlank(message = "是否必填不能为空")
    @Schema(description = "是否必填（1:是,0:否）")
    private Boolean isRequired;

    /**
     * 是否表单字段（1:是,0:否）
     */
    @NotBlank(message = "是否表单不能为空")
    @Schema(description = "是否表单字段（1:是,0:否）")
    private Boolean isForm;

    /**
     * 是否展示字段（1:是,0:否）
     */
    @NotBlank(message = "是否展示不能为空")
    @Schema(description = "是否展示字段（1:是,0:否）")
    private Boolean isShow;

    /**
     * 是否查询字段（1:是,0:否）
     */
    @NotBlank(message = "是否查询不能为空")
    @Schema(description = "是否查询字段（1:是,0:否）")
    private Boolean isQuery;

    /**
     * 展示类型 ( JSON、文本、Markdown、Tag、图片... )
     */
    @Schema(description = "展示类型 ( JSON、文本、Markdown、Tag、图片... )")
    private String showType;

    /**
     * 查询方式（等于、不等于、大于、小于、范围）
     */
    @Schema(description = "查询方式（等于、不等于、大于、小于、范围）")
    private String queryType;

    /**
     * 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    @Schema(description = "显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）")
    private String queryFormType;

    /**
     * 表单类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    @Schema(description = "表单类型（文本框、文本域、下拉框、复选框、单选框、日期控件）")
    private String saveFormType;

    /**
     * 字典类型
     */
    @Schema(description = "字典类型")
    private String dictTypeName;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;
}
