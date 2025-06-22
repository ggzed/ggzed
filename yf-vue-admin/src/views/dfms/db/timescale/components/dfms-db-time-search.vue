<template>
    <div class="search-container">
        <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
            <el-form-item label="名称" prop="name">
                <el-input
                        v-model="query.name"
                        clearable
                        placeholder="名称"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
<!--            <el-form-item label="字符集" prop="charset">-->
<!--                <el-input-->
<!--                        v-model="query.charset"-->
<!--                        clearable-->
<!--                        placeholder="字符集"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="排序规则" prop="collation">-->
<!--                <el-input-->
<!--                        v-model="query.collation"-->
<!--                        clearable-->
<!--                        placeholder="排序规则"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="大小" prop="size">-->
<!--                <el-input-->
<!--                        v-model="query.size"-->
<!--                        clearable-->
<!--                        placeholder="大小"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="表数量" prop="tables">-->
<!--                <el-input-->
<!--                        v-model="query.tables"-->
<!--                        clearable-->
<!--                        placeholder="表数量"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="类型(1-pgs" prop="type">-->
<!--                <el-input-->
<!--                        v-model="query.type"-->
<!--                        clearable-->
<!--                        placeholder="类型(1-pgs"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="保留策略(tim" prop="retentionPolicy">-->
<!--                <el-input-->
<!--                        v-model="query.retentionPolicy"-->
<!--                        clearable-->
<!--                        placeholder="保留策略(tim"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="序列数(time" prop="sequenceNumber">-->
<!--                <el-input-number-->
<!--                        v-model="query.sequenceNumber"-->
<!--                        clearable-->
<!--                        placeholder="序列数(time"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="数据点数(tim" prop="dataPointsNumber">-->
<!--                <el-input-number-->
<!--                        v-model="query.dataPointsNumber"-->
<!--                        clearable-->
<!--                        placeholder="数据点数(tim"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="维度(vecto" prop="dimension">-->
<!--                <el-input-->
<!--                        v-model="query.dimension"-->
<!--                        clearable-->
<!--                        placeholder="维度(vecto"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="向量数量(vec" prop="vectorNumber">-->
<!--                <el-input-number-->
<!--                        v-model="query.vectorNumber"-->
<!--                        clearable-->
<!--                        placeholder="向量数量(vec"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="索引类型(vec" prop="indexType">-->
<!--                <el-input-->
<!--                        v-model="query.indexType"-->
<!--                        clearable-->
<!--                        placeholder="索引类型(vec"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="状态(1-在线；" prop="status">-->
<!--                <el-input-->
<!--                        v-model="query.status"-->
<!--                        clearable-->
<!--                        placeholder="状态(1-在线；"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="创建人" prop="createBy">-->
<!--                <el-input-->
<!--                        v-model="query.createBy"-->
<!--                        clearable-->
<!--                        placeholder="创建人"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="创建时间开始" prop="createTimeStart">-->
<!--                <el-input-->
<!--                        v-model="query.createTimeStart"-->
<!--                        clearable-->
<!--                        placeholder="创建时间开始"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="创建时间结束" prop="createTimeEnd">-->
<!--                <el-input-->
<!--                        v-model="query.createTimeEnd"-->
<!--                        clearable-->
<!--                        placeholder="创建时间结束"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="修改人Id开始" prop="updateByStart">-->
<!--                <el-input-->
<!--                        v-model="query.updateByStart"-->
<!--                        clearable-->
<!--                        placeholder="修改人Id开始"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="修改人Id结束" prop="updateByEnd">-->
<!--                <el-input-->
<!--                        v-model="query.updateByEnd"-->
<!--                        clearable-->
<!--                        placeholder="修改人Id结束"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="修改时间开始" prop="updateTimeStart">-->
<!--                <el-input-->
<!--                        v-model="query.updateTimeStart"-->
<!--                        clearable-->
<!--                        placeholder="修改时间开始"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="修改时间结束" prop="updateTimeEnd">-->
<!--                <el-input-->
<!--                        v-model="query.updateTimeEnd"-->
<!--                        clearable-->
<!--                        placeholder="修改时间结束"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
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
import {DfmsDbPageQuery} from "@/api/dfms/db/type";
import {DictType} from "@/api/system/dict-data/type";
import {FormInstance} from "element-plus";

// 组件定义
defineOptions({
    name: "DfmsDbTimeSearch",
    inheritAttrs: false,
});

// 组件 props & emits
const props = withDefaults(defineProps<{
    query: DfmsDbPageQuery;
    dictData?: Record<DictType | string, Record<any, string>>;
    loadData: (callback?: () => void) => Promise<void>;
    resetQuery: (callback?: () => void) => Promise<void>;                  // 重置查询条件
}>(), {dictData: () => ({})});

const emits = defineEmits<{
(event: "update:query", query: DfmsDbPageQuery): void
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
