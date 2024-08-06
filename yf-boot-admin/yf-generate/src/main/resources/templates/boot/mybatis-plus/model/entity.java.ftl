<#-- @formatter:off -->
package ${table.packageName}.${table.moduleName}.model.entity;

import com.yf.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
<#assign now = .now>
<#assign randomUUID = ((1234567890123456789 + (now?long)) % 9223372036854775807)?c>
/**
* ${(table.tableComment?string?trim?length > 0) ? string(table.tableComment,table.className)}-实体类
*
* @author ${table.functionAuthor}
* @since ${now?string("yyyy-MM-dd HH:mm:ss")}
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "${table.tableComment}")
@TableName("${table.tableName}")
public class ${table.className} extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = ${randomUUID}L;

<#list fields as field>
    <#if field.javaField != "createTime" && field.javaField != "updateTime">
    /**
    * ${field.columnComment}
    */
    @Schema(description = "${field.columnComment}")
    <#if field.isPk == 1 && field.isIncrement == 1>
    @TableId(type = IdType.AUTO)
    </#if>
    <#if field.javaType == "Long" && field.isPk == 1 && field.isIncrement == 0>
    @TableId(type = IdType.ASSIGN_ID)
    </#if>
    <#if field.javaType == "LocalDateTime">
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    </#if>
    private ${field.javaType} ${field.javaField};

    </#if>
</#list>
}