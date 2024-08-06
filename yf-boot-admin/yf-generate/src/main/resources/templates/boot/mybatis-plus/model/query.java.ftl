<#-- @formatter:off -->
package ${table.packageName}.${table.moduleName}.model.query;

import com.yf.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* ${(table.tableComment?string?trim?length > 0) ? string(table.tableComment,table.className)}-查询
*
* @author ${table.functionAuthor}
* @since  ${.now?string("yyyy-MM-dd HH:mm:ss")}
*/
@Data
public class ${table.className}Query extends BasePageQuery{

<#list fields as field>
    <#if field.isQuery == 1>
    /**
    * ${field.columnComment}
    */
    @Schema(description = "${field.columnComment}")
    private ${field.javaType} ${field.javaField};
    </#if>
</#list>

}