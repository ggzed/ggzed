<template>
  <div class="search-container">
    <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
      <el-form-item label="字典名称 :" prop="name">
        <el-input
            v-model="query.name"
            clearable
            placeholder="字典名称搜索"
            @keyup.enter="props.loadData()"
        />
      </el-form-item>
      <el-form-item label="字典类型 :" prop="type">
        <el-input
            v-model="query.type"
            clearable
            placeholder="字典类型搜索"
            @keyup.enter="props.loadData()"
        />
      </el-form-item>
      <el-form-item label="状态 :" prop="status">
        <el-select
            v-model="query.status"
            clearable
            placeholder="字典使用状态"
        >
          <el-option v-for="(value,key) in EnableStatusEnum.OPTIONS" :label="value" :value="Number(key)"/>
        </el-select>
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
import {EnableStatusEnum} from "@/constants/system";
import {FormInstance} from "element-plus";
import {DictTypePageQuery} from "@/api/system/dict-type/type";

defineOptions({
  name: "DeptTypeSearch",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  query: DictTypePageQuery;
  loadData: (callback?: () => void) => Promise<void>;                    // 加载数据函数
  resetQuery: (callback?: () => void) => Promise<void>;                  // 重置查询条件
}>(), {});

const emits = defineEmits<{
  (event: "update:query", query: DictTypePageQuery): void
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
