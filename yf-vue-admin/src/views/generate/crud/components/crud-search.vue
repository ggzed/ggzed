<template>
  <div class="search-container">
    <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
      <el-form-item label="作者 :" prop="functionAuthor">
        <el-input
            v-model="query.functionAuthor"
            clearable
            placeholder="作者"
            @keyup.enter="props.loadData()"
        />
      </el-form-item>
      <el-form-item label="表名 :" prop="tableName">
        <el-input
            v-model="query.tableName"
            clearable
            placeholder="数据库表名"
            @keyup.enter="props.loadData()"
        />
      </el-form-item>
      <el-form-item label="备注 :" prop="remark">
        <el-input
            v-model="query.remark"
            clearable
            placeholder="备注"
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
// 组件定义
import {GenCrudTablePageQuery} from "@/api/generate/crud-code/type";
import {FormInstance} from "element-plus";

defineOptions({
  name: "CrudSearch",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  query: GenCrudTablePageQuery;
  loadData: (callback?: () => void) => Promise<void>;                    // 加载数据函数
  resetQuery: (callback?: () => void) => Promise<void>;                  // 重置查询条件
}>(), {});

const emits = defineEmits<{
  (event: "update:query", query: GenCrudTablePageQuery): void
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
