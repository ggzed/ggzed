<#-- @formatter:off -->
package ${table.packageName}.${table.moduleName}.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* ${(table.tableComment?string?trim?length > 0) ? string(table.tableComment,table.className)}-VO
*
* @author ${table.functionAuthor}
* @since  ${.now?string("yyyy-MM-dd HH:mm:ss")}
*/
@Data
public class ${table.className}VO {

<#list fields as field>
    <#if field.isShow == 1>
    /**
    * ${field.columnComment}
    */
    @Schema(description = "${field.columnComment}")
    <#if field.javaType == "LocalDateTime">
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    </#if>
    private ${field.javaType} ${field.javaField};
    </#if>
</#list>

}