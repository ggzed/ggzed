<template>
    <div class="search-container">
        <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
            <el-form-item label="索引名" prop="indexName">
                <el-input
                        v-model="query.indexName"
                        clearable
                        placeholder="索引名"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="列" prop="columns">
                <el-input
                        v-model="query.columns"
                        clearable
                        placeholder="列"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="索引类型" prop="indexType">
                <el-input
                        v-model="query.indexType"
                        clearable
                        placeholder="索引类型"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="是否唯一(1-是" prop="isOnly">
                <el-select
                        v-model="query.isOnly"
                        clearable
                        placeholder="是否唯一(1-是"
                        @change="props.loadData()"
                >
                    <el-option v-for="(value,key) in props.dictData['']" :label="value"
                               :value="key"/>
                </el-select>
            </el-form-item>
            <el-form-item label="状态(1-在线；" prop="status">
                <el-input
                        v-model="query.status"
                        clearable
                        placeholder="状态(1-在线；"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="创建人" prop="createBy">
                <el-input
                        v-model="query.createBy"
                        clearable
                        placeholder="创建人"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="创建时间开始" prop="createTimeStart">
                <el-input
                        v-model="query.createTimeStart"
                        clearable
                        placeholder="创建时间开始"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="创建时间结束" prop="createTimeEnd">
                <el-input
                        v-model="query.createTimeEnd"
                        clearable
                        placeholder="创建时间结束"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="修改人Id开始" prop="updateByStart">
                <el-input
                        v-model="query.updateByStart"
                        clearable
                        placeholder="修改人Id开始"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="修改人Id结束" prop="updateByEnd">
                <el-input
                        v-model="query.updateByEnd"
                        clearable
                        placeholder="修改人Id结束"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="修改时间开始" prop="updateTimeStart">
                <el-input
                        v-model="query.updateTimeStart"
                        clearable
                        placeholder="修改时间开始"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="修改时间结束" prop="updateTimeEnd">
                <el-input
                        v-model="query.updateTimeEnd"
                        clearable
                        placeholder="修改时间结束"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
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
import {DfmsTableIndexPageQuery} from "@/api/dfms/tableindex/type";
import {DictType} from "@/api/system/dict-data/type";
import {FormInstance} from "element-plus";

// 组件定义
defineOptions({
    name: "DfmsTableIndexSearch",
    inheritAttrs: false,
});

// 组件 props & emits
const props = withDefaults(defineProps<{
    query: DfmsTableIndexPageQuery;
    dictData?: Record<DictType | string, Record<any, string>>;
    loadData: (callback?: () => void) => Promise<void>;
    resetQuery: (callback?: () => void) => Promise<void>;                  // 重置查询条件
}>(), {dictData: () => ({})});

const emits = defineEmits<{
(event: "update:query", query: DfmsTableIndexPageQuery): void
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
