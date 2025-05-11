<#-- 注释宏 -->
<#macro fieldComment columnComment="">
    <#local comment = columnComment?trim />
    <#if !comment?has_content>
        <#local comment = "数据库未备注" />
    </#if>
    ${comment}<#t>
</#macro>
<#-- 验证注解宏 -->
<#macro validationAnnotation isRequired javaType>
    <#if isRequired>
        <#if javaType == "String">
    @NotBlank
        <#else>
    @NotNull
        </#if>
    </#if>
</#macro>
<#-- @formatter:off -->
package com.${table.packageName}.${table.moduleName}.${table.businessName}.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ${table.tableComment}-${table.className}Form
 *
 * @author: ${table.functionAuthor}
 * @since : ${.now}
 */
@Data
public class ${table.className}Form {

<#list mapFields.form as field>
    /**
     * <@fieldComment columnComment=field.columnComment />
     */
    @Schema(description = "<@fieldComment columnComment=field.columnComment />")
    <@validationAnnotation isRequired=field.isRequired javaType=field.javaType />
    <#if field.javaType == "LocalDateTime">
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    <#elseif field.javaType == "LocalDate">
    @JsonFormat(pattern = "yyyy-MM-dd")
    </#if>
    private ${field.javaType} ${field.javaTsFieldName};

</#list>
}
<#-- @formatter:on -->
