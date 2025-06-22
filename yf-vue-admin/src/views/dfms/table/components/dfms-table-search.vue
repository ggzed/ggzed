<template>
    <div class="search-container">
        <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
            <el-form-item label="表名" prop="tableName">
                <el-input
                        v-model="query.tableName"
                        clearable
                        placeholder="表名"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
<!--            <el-form-item label="数据库表描述" prop="tableComment">-->
<!--                <el-input-->
<!--                        v-model="query.tableComment"-->
<!--                        clearable-->
<!--                        placeholder="数据库表描述"-->
<!--                        @keyup.enter="props.loadData()"-->
<!--                />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="ddl" prop="ddl">-->
<!--                <el-input-->
<!--                        v-model="query.ddl"-->
<!--                        clearable-->
<!--                        placeholder="ddl"-->
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
import {DfmsTablePageQuery} from "@/api/dfms/table/type";
import {DictType} from "@/api/system/dict-data/type";
import {FormInstance} from "element-plus";

// 组件定义
defineOptions({
    name: "DfmsTableSearch",
    inheritAttrs: false,
});

// 组件 props & emits
const props = withDefaults(defineProps<{
    query: DfmsTablePageQuery;
    dictData?: Record<DictType | string, Record<any, string>>;
    loadData: (callback?: () => void) => Promise<void>;
    resetQuery: (callback?: () => void) => Promise<void>;                  // 重置查询条件
}>(), {dictData: () => ({})});

const emits = defineEmits<{
(event: "update:query", query: DfmsTablePageQuery): void
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
