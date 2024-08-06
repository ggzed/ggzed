<#-- @formatter:off -->
package ${table.packageName}.${table.moduleName}.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
* ${(table.tableComment?string?trim?length > 0) ? string(table.tableComment,table.className)}-表单
*
* @author ${table.functionAuthor}
* @since  ${.now?string("yyyy-MM-dd HH:mm:ss")}
*/
@Data
public class ${table.className}Form {

<#list fields as field>
    <#if field.isInsertEdit == 1>
    /**
    * ${field.columnComment}
    */
    @Schema(description = "${field.columnComment}")
        <#if field.isRequired == 1 && field.javaType == "String">
    @NotBlank(message = "${field.javaField}不能为空")
        <#elseif field.isRequired == 1>
    @NotNull(message = "${field.javaField}不能为空")
        </#if>
    private ${field.javaType} ${field.javaField};
    </#if>
</#list>

}