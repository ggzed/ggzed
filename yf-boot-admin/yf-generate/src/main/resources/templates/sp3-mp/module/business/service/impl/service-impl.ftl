<#assign classNameLower = table.className?substring(0, 1)?lower_case + table.className?substring(1)>
<#assign filedNameUpper = mapFields.pk[0].javaTsFieldName?cap_first>
<#macro queryValidated field>
    <#local fieldName = field.javaTsFieldName>
    <#local getter = "get${fieldName?cap_first}()">
    <#local getterBegin = "get${fieldName?cap_first}Begin()">
    <#local getterEnd = "get${fieldName?cap_first}End()">
    <#local condition = "">
    <#switch field.javaType>
        <#case "String">
            <#if field.queryType == 'between'>
                <#local condition = "StringUtils.hasText(query.${getterBegin}) && StringUtils.hasText(query.${getterEnd})">
            <#else>
                <#local condition = "StringUtils.hasText(query.${getter})">
            </#if>
            <#break>
        <#default>
            <#if field.queryType == 'between'>
                <#local condition = "query.${getterBegin} != null && query.${getterEnd} != null">
            <#else>
                <#local condition = "query.${getter} != null">
            </#if>
    </#switch>
    <#if field.queryType = "in">
        <#local condition = "!CollectionUtils.isEmpty(query.${getter})">
    </#if>
    ${condition}<#t>
</#macro>
<#-- @formatter:off -->
package com.${table.packageName}.${table.moduleName}.${table.businessName}.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.${table.packageName}.${table.moduleName}.${table.businessName}.converter.${table.className}Converter;
import com.${table.packageName}.${table.moduleName}.${table.businessName}.mapper.${table.className}Mapper;
import com.${table.packageName}.${table.moduleName}.${table.businessName}.model.entity.${table.className};
import com.${table.packageName}.${table.moduleName}.${table.businessName}.model.form.${table.className}Form;
import com.${table.packageName}.${table.moduleName}.${table.businessName}.model.query.${table.className}PageQuery;
import com.${table.packageName}.${table.moduleName}.${table.businessName}.model.vo.${table.className}PageVO;
import com.${table.packageName}.${table.moduleName}.${table.businessName}.service.I${table.className}Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ${table.tableComment}-${table.className}ServiceImpl
 *
 * @author: ${table.functionAuthor}
 * @since : ${.now}
 */
@Service("${classNameLower}Service" )
@RequiredArgsConstructor
public class ${table.className}ServiceImpl extends ServiceImpl<${table.className}Mapper, ${table.className}> implements I${table.className}Service {

    private final ${table.className}Converter ${classNameLower}Converter;

    /**
     * 查询${table.tableComment}
     *
     * @param queryParams 分页参数
     * @return 分页数据
     */
    @Override
    public IPage<${table.className}PageVO> get${table.className}Page(${table.className}PageQuery queryParams) {
        // 1. 分页查询数据
        Page<${table.className}> page = this.getPageData(queryParams);
        // 2. 转换为 vo 后返回
        return ${classNameLower}Converter.page2pageVO(page);
    }

    /**
     * 获取分页数据
     *
     * @param query 查询参数
     * @return Page
     */
    private Page<${table.className}> getPageData(${table.className}PageQuery query) {
        // 1. 查询数据
        return this.lambdaQuery()
            <#list mapFields.query as query>
                <#if query.queryType == "eq">
                .eq(<@queryValidated field=query />, ${table.className}::get${query.javaTsFieldName?cap_first}, query.get${query.javaTsFieldName?cap_first}())
                </#if>
                <#if query.queryType == "like">
                .like(<@queryValidated field=query />, ${table.className}::get${query.javaTsFieldName?cap_first}, query.get${query.javaTsFieldName?cap_first}())
                </#if>
                <#if query.queryType == "like_left">
                .likeLeft(<@queryValidated field=query />, ${table.className}::get${query.javaTsFieldName?cap_first}, query.get${query.javaTsFieldName?cap_first}())
                </#if>
                <#if query.queryType == "like_right">
                .likeRight(<@queryValidated field=query />, ${table.className}::get${query.javaTsFieldName?cap_first}, query.get${query.javaTsFieldName?cap_first}())
                </#if>
                <#if query.queryType == "gt">
                .gt(<@queryValidated field=query />, ${table.className}::get${query.javaTsFieldName?cap_first}, query.get${query.javaTsFieldName?cap_first}())
                </#if>
                <#if query.queryType == "lt">
                .lt(<@queryValidated field=query />, ${table.className}::get${query.javaTsFieldName?cap_first}, query.get${query.javaTsFieldName?cap_first}())
                </#if>
                <#if query.queryType == "ge">
                .ge(<@queryValidated field=query />, ${table.className}::get${query.javaTsFieldName?cap_first}, query.get${query.javaTsFieldName?cap_first}())
                </#if>
                <#if query.queryType == "le">
                .le(<@queryValidated field=query />, ${table.className}::get${query.javaTsFieldName?cap_first}, query.get${query.javaTsFieldName?cap_first}())
                </#if>
                <#if query.queryType == "between">
                .between(<@queryValidated field=query />, ${table.className}::get${query.javaTsFieldName?cap_first},
                        query.get${query.javaTsFieldName?cap_first}Begin(),
                        query.get${query.javaTsFieldName?cap_first}End())
                </#if>
                <#if query.queryType == "in">
                .in(<@queryValidated field=query />, ${table.className}::get${query.javaTsFieldName?cap_first},query.get${query.javaTsFieldName?cap_first}())
                </#if>
            </#list>
                .page(query.toPage());
    }

    /**
     * 获取${table.tableComment}表单数据
     *
     * @param ${mapFields.pk[0].javaTsFieldName} ${table.tableComment}表主键
     * @return ${table.tableComment}表单数据
     */
    @Override
    public ${table.className}Form get${table.className}Form(${mapFields.pk[0].javaType} ${mapFields.pk[0].javaTsFieldName}) {
        // 1. 查询对应数据
        ${table.className} ${classNameLower} = this.lambdaQuery()
                .eq(${table.className}::get${filedNameUpper}, ${mapFields.pk[0].javaTsFieldName})
                .one();
        // 2. entity 2 form
        return ${classNameLower}Converter.entity2form(${classNameLower});
    }

    /**
     * 新增${table.tableComment}
     *
     * @param ${classNameLower}Form ${table.tableComment}表单
     * @return 主键
     */
    @Override
    public ${mapFields.pk[0].javaType} save${table.className}(${table.className}Form ${classNameLower}Form) {
        // 1. form 转 entity
        ${table.className} ${classNameLower} = ${classNameLower}Converter.form2entity(${classNameLower}Form);
        // 2. 存储数据
        this.save(${classNameLower});
        // 3. 返回主键
        return ${classNameLower}.get${filedNameUpper}();
    }

    /**
     * 删除${table.tableComment}
     *
     * @param ${mapFields.pk[0].javaTsFieldName}s 主键集合
     * @return 是否删除成功
     */
    @Override
    public boolean delete${table.className}(List<${mapFields.pk[0].javaType}> ${mapFields.pk[0].javaTsFieldName}s) {
        this.lambdaUpdate()
                .in(${table.className}::get${filedNameUpper}, ${mapFields.pk[0].javaTsFieldName}s)
                .remove();
        return true;
    }

    /**
     * 修改${table.tableComment}信息
     *
     * @param ${mapFields.pk[0].javaTsFieldName}   ${table.tableComment}${filedNameUpper}
     * @param ${classNameLower}Form ${table.tableComment}表单数据
     * @return 是否修改成功
     */
    @Override
    public boolean update${table.className}(${mapFields.pk[0].javaType} ${mapFields.pk[0].javaTsFieldName}, ${table.className}Form ${classNameLower}Form) {
        // 1. form 转 entity
        ${table.className} ${classNameLower} = ${classNameLower}Converter.form2entity(${classNameLower}Form);
        // 2. 修改数据
        this.lambdaUpdate()
                .eq(${table.className}::get${filedNameUpper}, ${mapFields.pk[0].javaTsFieldName})
                .update(${classNameLower});
        return true;
    }
}
<#-- @formatter:on -->