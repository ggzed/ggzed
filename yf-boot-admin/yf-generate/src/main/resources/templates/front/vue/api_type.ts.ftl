<#-- @formatter:off -->
/**
* 分页查询参数 : ${table.className}PageQuery
*/
export interface ${table.className}PageQuery extends PageQuery {
<#list fields as field>
    <#if field.isQuery == 1>
    /** ${field.columnComment} */
    ${field.tsField}?: ${field.tsType};
    </#if>
</#list>
}

/**
* 分页返回数据 : ${table.className}PageVO
*/
export interface ${table.className}PageVO {
<#list fields as field>
    <#if field.isShow == 1>
    /** ${field.columnComment} */
    ${field.tsField}?: ${field.tsType};
    </#if>
</#list>
}

/**
* 表单数据 : ${table.className}Form
*/
export interface ${table.className}Form {
<#list fields as field>
    <#if field.isInsertEdit == 1>
    /** ${field.columnComment} */
    ${field.tsField}${(field.isRequired == 1) ? string("","?")}: ${field.tsType};
    </#if>
</#list>
}