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
    name: "DfmsDbSearch",
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
