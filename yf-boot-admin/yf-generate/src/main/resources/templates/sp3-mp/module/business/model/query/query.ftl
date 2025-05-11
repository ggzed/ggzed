<#macro fieldComment columnComment="">
    <#local comment = columnComment?trim />
    <#if !comment?has_content>
        <#local comment = "数据库未备注" />
    </#if>
    ${comment}<#t>
</#macro>
<#-- @formatter:off -->
package com.${table.packageName}.${table.moduleName}.${table.businessName}.model.query;

import com.yf.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * ${table.tableComment}-${table.className}PageQuery
 *
 * @author: ${table.functionAuthor}
 * @since : ${.now}
 */
@Schema(description = "${table.tableComment}分页查询对象" )
@EqualsAndHashCode(callSuper = true)
@Data
public class ${table.className}PageQuery extends BasePageQuery {

<#list mapFields.query as field>
    <#if field.queryType != "between" && field.queryType != "in">
    /**
     * <@fieldComment columnComment=field.columnComment />
     */
    @Schema(description = "<@fieldComment columnComment=field.columnComment />")
        <#if field.javaType == "LocalDateTime">
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        <#elseif field.javaType == "LocalDate">
    @DateTimeFormat(pattern = "yyyy-MM-dd")
        </#if>
    private ${field.javaType} ${field.javaTsFieldName};
    <#elseif field.queryType != "between" && field.queryType == "in">
    /**
     * <@fieldComment columnComment=field.columnComment />集合
     */
    @Schema(description = "<@fieldComment columnComment=field.columnComment />集合")
    private Set<${field.javaType}> ${field.javaTsFieldName};
    <#elseif field.queryType == "between" && !field.dictTypeName?has_content>
    /**
    * <@fieldComment columnComment=field.columnComment />Begin
    */
    @Schema(description = "<@fieldComment columnComment=field.columnComment />Start")
    <#if field.javaType == "LocalDateTime">
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    <#elseif field.javaType == "LocalDate">
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    </#if>
    private ${field.javaType} ${field.javaTsFieldName}Begin;

    /**
    * <@fieldComment columnComment=field.columnComment />End
    */
    @Schema(description = "<@fieldComment columnComment=field.columnComment />End")
    <#if field.javaType == "LocalDateTime">
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    <#elseif field.javaType == "LocalDate">
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    </#if>
    private ${field.javaType} ${field.javaTsFieldName}End;
    </#if>

</#list>
}
<#-- @formatter:on -->
