<#assign type_base_path = "\"@/api/${table.moduleName}/${table.businessName}/type\"">
<#-- @formatter:off -->
<template>
    <div class="search-container">
        <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
        <#list mapFields.query as field>
            <#if field.queryType != "between">
            <el-form-item label="${field.showName}" prop="${field.javaTsFieldName}">
                <#if field.queryFormType == "input_number">
                <el-input-number
                        v-model="query.${field.javaTsFieldName}"
                        clearable
                        placeholder="${field.showName}"
                        @keyup.enter="props.loadData()"
                />
                <#elseif field.queryFormType == "select">
                <el-select
                        v-model="query.${field.javaTsFieldName}"
                        clearable
                        placeholder="${field.showName}"
                        @change="props.loadData()"
                >
                    <el-option v-for="(value,key) in props.dictData['${field.dictTypeName}']" :label="value"
                               :value="key"/>
                </el-select>
                <#elseif field.queryFormType == "multi_select" && field.dictTypeName?has_content>
                <el-select
                        v-model="query.${field.javaTsFieldName}"
                        multiple
                        clearable
                        filterable
                        placeholder="${field.showName}"
                        @change="props.loadData()"
                >
                    <el-option v-for="(value,key) in props.dictData['${field.dictTypeName}']" :label="value"
                               :value="key"/>
                </el-select>
                <#elseif field.queryFormType == "date">
                <el-date-picker
                        v-model="query.${field.javaTsFieldName}"
                        format="YYYY/MM/DD"
                        placeholder="${field.showName}"
                        type="date"
                        value-format="YYYY-MM-DD"
                        @keyup.enter="props.loadData()"
                />
                <#elseif field.queryFormType == "datetime">
                <el-date-picker
                        v-model="query.${field.javaTsFieldName}"
                        format="YYYY/MM/DD HH:mm:ss"
                        placeholder="${field.showName}"
                        type="datetime"
                        value-format="YYYY-MM-DD HH:mm:ss"
                        @keyup.enter="props.loadData()"
                />
                <#else >
                <el-input
                        v-model="query.${field.javaTsFieldName}"
                        clearable
                        placeholder="${field.showName}"
                        @keyup.enter="props.loadData()"
                />
                </#if>
            </el-form-item>
            <#elseif field.queryType == "between" && !(field.dictTypeName?has_content)>
                <#if field.queryFormType == "input_number">
            <el-form-item label="${field.showName}开始" prop="${field.javaTsFieldName}Start">
                <el-input-number
                        v-model="query.${field.javaTsFieldName}Start"
                        clearable
                        placeholder="${field.showName}开始"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="${field.showName}结束" prop="${field.javaTsFieldName}End">
                <el-input-number
                        v-model="query.${field.javaTsFieldName}End"
                        clearable
                        placeholder="${field.showName}结束"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
                <#elseif field.queryFormType == "date">
            <el-form-item label="${field.showName}开始" prop="${field.javaTsFieldName}Start">
                <el-date-picker
                        v-model="query.${field.javaTsFieldName}Start"
                        format="YYYY/MM/DD"
                        placeholder="${field.showName}开始"
                        type="date"
                        value-format="YYYY-MM-DD"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="${field.showName}结束" prop="${field.javaTsFieldName}End">
                <el-date-picker
                        v-model="query.${field.javaTsFieldName}End"
                        format="YYYY/MM/DD"
                        placeholder="${field.showName}结束"
                        type="date"
                        value-format="YYYY-MM-DD"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
                <#elseif field.queryFormType == "datetime">
            <el-form-item label="${field.showName}开始" prop="${field.javaTsFieldName}Start">
                <el-date-picker
                        v-model="query.${field.javaTsFieldName}Start"
                        format="YYYY/MM/DD HH:mm:ss"
                        placeholder="${field.showName}开始"
                        type="datetime"
                        value-format="YYYY-MM-DD HH:mm:ss"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="${field.showName}结束" prop="${field.javaTsFieldName}End">
                <el-date-picker
                        v-model="query.${field.javaTsFieldName}End"
                        format="YYYY/MM/DD HH:mm:ss"
                        placeholder="${field.showName}结束"
                        type="datetime"
                        value-format="YYYY-MM-DD HH:mm:ss"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
                <#else >
            <el-form-item label="${field.showName}开始" prop="${field.javaTsFieldName}Start">
                <el-input
                        v-model="query.${field.javaTsFieldName}Start"
                        clearable
                        placeholder="${field.showName}开始"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="${field.showName}结束" prop="${field.javaTsFieldName}End">
                <el-input
                        v-model="query.${field.javaTsFieldName}End"
                        clearable
                        placeholder="${field.showName}结束"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
                </#if>
            </#if>
        </#list>
            <el-form-item>
                <el-button type="primary" @click="props.loadData()">
                    <template #icon>
                        <search/>
                    </template>
                    搜索
                </el-button>
                <el-button plain type="primary" @click="props.resetQuery()">
                    <template #icon>
                        <refresh/>
                    </template>
                    重置
                </el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script lang="ts" setup>
import {${table.className}PageQuery} from ${type_base_path};
import {DictType} from "@/api/system/dict-data/type";
import {FormInstance} from "element-plus";

// 组件定义
defineOptions({
    name: "${table.className}Search",
    inheritAttrs: false,
});

// 组件 props & emits
const props = withDefaults(defineProps<{
    query: ${table.className}PageQuery;
    dictData?: Record<DictType | string, Record<any, string>>;
    loadData: (callback?: () => void) => Promise<void>;
    resetQuery: (callback?: () => void) => Promise<void>;                  // 重置查询条件
}>(), {dictData: () => ({})});

const emits = defineEmits<{
(event: "update:query", query: ${table.className}PageQuery): void
}>()
// hooks
const query = useVModel(props, 'query', emits)
// 数据
const queryFormRef = ref<FormInstance | null>(null);         // 查询表单
// 方法

// 生命周期
</script>

<style lang="scss" scoped>
    /* 样式 */
</style>
<#-- @formatter:on -->
