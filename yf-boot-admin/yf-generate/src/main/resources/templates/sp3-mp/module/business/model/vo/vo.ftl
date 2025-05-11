<#macro fieldComment columnComment="">
    <#local comment = columnComment?trim />
    <#if !comment?has_content>
        <#local comment = "数据库未备注" />
    </#if>
    ${comment}<#t>
</#macro>
<#-- @formatter:off -->
package com.${table.packageName}.${table.moduleName}.${table.businessName}.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ${table.tableComment}-${table.className}PageVO
 *
 * @author: ${table.functionAuthor}
 * @since : ${.now}
 */
@Schema(description = "${table.tableComment}PageVO" )
@Data
public class ${table.className}PageVO {

    /**
     * <@fieldComment columnComment=mapFields.pk[0].columnComment />
     */
    @Schema(description = "<@fieldComment columnComment=mapFields.pk[0].columnComment />")
    private ${mapFields.pk[0].javaType} ${mapFields.pk[0].javaTsFieldName};

<#list mapFields.show as field>
    /**
     * <@fieldComment columnComment=field.columnComment />
     */
    @Schema(description = "<@fieldComment columnComment=field.columnComment />")
        <#if field.javaType == "LocalDateTime">
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        <#elseif field.javaType == "LocalDate">
    @JsonFormat(pattern = "yyyy-MM-dd")
        </#if>
    private ${field.javaType} ${field.javaTsFieldName};

</#list>
}
<#-- @formatter:on -->
