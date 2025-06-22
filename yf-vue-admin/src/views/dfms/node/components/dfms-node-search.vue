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
            <el-form-item label="IP地址" prop="ip">
                <el-input
                        v-model="query.ip"
                        clearable
                        placeholder="IP地址"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="端口" prop="port">
                <el-input
                        v-model="query.port"
                        clearable
                        placeholder="端口"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="用户名" prop="username">
                <el-input
                        v-model="query.username"
                        clearable
                        placeholder="用户名"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="角色(1-主节点" prop="role">
                <el-input
                        v-model="query.role"
                        clearable
                        placeholder="角色(1-主节点"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="角色状态(1-在" prop="status">
                <el-input
                        v-model="query.status"
                        clearable
                        placeholder="角色状态(1-在"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="cpu占用" prop="cpu">
                <el-input
                        v-model="query.cpu"
                        clearable
                        placeholder="cpu占用"
                        @keyup.enter="props.loadData()"
                />
            </el-form-item>
            <el-form-item label="内存占用" prop="memory">
                <el-input
                        v-model="query.memory"
                        clearable
                        placeholder="内存占用"
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
import {DfmsNodePageQuery} from "@/api/dfms/node/type";
import {DictType} from "@/api/system/dict-data/type";
import {FormInstance} from "element-plus";

// 组件定义
defineOptions({
    name: "DfmsNodeSearch",
    inheritAttrs: false,
});

// 组件 props & emits
const props = withDefaults(defineProps<{
    query: DfmsNodePageQuery;
    dictData?: Record<DictType | string, Record<any, string>>;
    loadData: (callback?: () => void) => Promise<void>;
    resetQuery: (callback?: () => void) => Promise<void>;                  // 重置查询条件
}>(), {dictData: () => ({})});

const emits = defineEmits<{
(event: "update:query", query: DfmsNodePageQuery): void
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
