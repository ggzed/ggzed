<#macro fieldComment columnComment="">
    <#local comment = columnComment?trim />
    <#if !comment?has_content>
        <#local comment = "数据库未备注" />
    </#if>
    ${comment}<#t>
</#macro>
<#-- @formatter:off -->
/**
 * ${table.className}PageQuery, 查询条件
 */
export interface ${table.className}PageQuery extends PageQuery {
<#list mapFields.query as field>
    <#if field.queryType != "between" && field.queryType != "in">
    /**
     * <@fieldComment columnComment=field.columnComment />
     */
    ${field.javaTsFieldName}?: ${field.tsType};
    <#elseif field.queryType != "between" && field.queryType == "in">
    /**
    * <@fieldComment columnComment=field.columnComment />集合
    */
    ${field.javaTsFieldName}?: ${field.tsType}[];
    <#elseif field.queryType == "between" && !(field.dictTypeName?has_content)>
    /**
     * <@fieldComment columnComment=field.columnComment />开始
     */
    ${field.javaTsFieldName}Start?: ${field.tsType};

    /**
     * <@fieldComment columnComment=field.columnComment />结束
     */
    ${field.javaTsFieldName}End?: ${field.tsType};
    </#if>

</#list>
}

/**
 * ${table.className}PageVO, 展示集合
 */
export interface ${table.className}PageVO {
<#list mapFields.show as field>
    /**
     * <@fieldComment columnComment=field.columnComment />
     */
    ${field.javaTsFieldName}?: ${field.tsType};

</#list>
}

/**
 * ${table.className}Form, 表单数据
 */
export interface ${table.className}Form {
<#list mapFields.form as field>
    /**
     * <@fieldComment columnComment=field.columnComment />
     */
    ${field.javaTsFieldName}?: ${field.tsType};

</#list>
}
<#-- @formatter:on -->
