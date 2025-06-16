<#assign api_base_path = "\"@/api/${table.moduleName}/${table.businessName}\"">
<#assign type_base_path = "\"@/api/${table.moduleName}/${table.businessName}/type\"">
<#assign importHook = 'import {useDictionary} from "@/hooks/userDict";'>
<#assign dictTypes = []>
<#assign has_dict = false>
<#list mapFields.entity as field>
    <#if field.dictTypeName?has_content>
        <#assign dictTypes = dictTypes + ["'" + field.dictTypeName + "'"]>
        <#assign has_dict = true>
    </#if>
</#list>
<#macro importUseDict hasDict>
    <#if hasDict == true>
        import {useDictionary} from "@/hooks/userDict";<#t>
    </#if>
</#macro>
<#-- @formatter:off -->
<template>
    <div class="app-container">
        <!--  Search  -->
        <${table.componentName}-search
            v-model:query="query"
            :load-data="loadData"
        <#if has_dict == true>
            :dict-data="dictData"
        </#if>
            :reset-query="resetQuery"
        />
        <!--  Table  -->
        <${table.componentName}-table
            v-model:query="query"
            :data-list="dataList"
            :load-data="loadData"
        <#if has_dict == true>
            :dict-data="dictData"
        </#if>
            :loading="loading"
            :total="total"
        />
    </div>
</template>

<script lang="ts" setup>
import {
        ${table.className}PageQuery,
        ${table.className}PageVO
    } from ${type_base_path};
import {${table.className}API} from ${api_base_path};
import {useDataLoader} from "@/hooks/useDataLoader";
<@importUseDict hasDict=has_dict/>

// 组件定义
defineOptions({
    name: "${table.className}",
    inheritAttrs: false,
});
// 公共数据 & 方法
const initialQuery: ${table.className}PageQuery = {
    <#list mapFields.query as field>
    <#if field.queryType != "between" && field.queryType != "in">
    ${field.javaTsFieldName}: undefined,
    <#elseif field.queryType != "between" && field.queryType == "in">
    ${field.javaTsFieldName}: [],
    <#elseif field.queryType == "between" && !(field.dictTypeName?has_content)>
    ${field.javaTsFieldName}Start: undefined,
    ${field.javaTsFieldName}End: undefined,
    </#if>

    </#list>
    pageNum: 1,
    pageSize: 10
}                       // 初始化查询条件
// hooks
const {
    query,
    dataList,
    total,
    loading,
    loadData,
    resetQuery
} = useDataLoader<${table.className}PageVO, ${table.className}PageQuery>(${table.className}API.PAGE.request, initialQuery);
<#if has_dict == true>
const dictData = await useDictionary([${dictTypes?join(",")}]); // 字典数据
</#if>
// 数据

// 方法

// 生命周期
onMounted(async () => {
    await loadData()
})
</script>

<style lang="scss" scoped>
  /* 样式 */
</style>
<#-- @formatter:on -->
