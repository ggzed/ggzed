<#macro fieldComment columnComment="">
    <#local comment = columnComment?trim />
    <#if !comment?has_content>
        <#local comment = "数据库未备注" />
    </#if>
    ${comment}<#t>
</#macro>
<#-- @formatter:off -->
package com.${table.packageName}.${table.moduleName}.${table.businessName}.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ${table.tableComment}-${table.className}
 *
 * @author: ${table.functionAuthor}
 * @since : ${.now}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "${table.tableComment}" )
@TableName("${table.tableName}" )
public class ${table.className} implements Serializable {

    @Serial
    private static final long serialVersionUID = ${(.now?long % 1000000000000)?c}L;
<#-- 默认主键采用雪花算法 -->
<#list mapFields.entity as field>
    /**
     * <@fieldComment columnComment=field.columnComment />
     */
    @Schema(description = "<@fieldComment columnComment=field.columnComment />")
    <#if field.isPk>
        <#if field.isIncrement>
    @TableId(type = IdType.AUTO)
        <#else>
    @TableId(type = IdType.ASSIGN_ID)
        </#if>
    <#elseif field.columnName == "create_time" && field.javaType == "LocalDateTime">
    @TableField(fill = FieldFill.INSERT)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    <#elseif field.columnName == "update_time" && field.javaType == "LocalDateTime">
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    <#elseif field.columnName == "create_by" && field.javaType == "Long">
    @TableField(fill = FieldFill.INSERT)
    <#elseif field.columnName == "update_by" && field.javaType == "Long">
    @TableField(fill = FieldFill.INSERT_UPDATE)
    </#if>
    private ${field.javaType} ${field.javaTsFieldName};

</#list>
}
<#-- @formatter:on -->
