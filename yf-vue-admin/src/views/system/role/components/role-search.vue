<template>
  <div class="search-container">
    <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
      <el-form-item label="关键词 :" prop="keywords">
        <el-input
            v-model="query.keywords"
            clearable
            placeholder="角色名称/角色编码"
            @keyup.enter="loadData"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="props.loadData">
          <template #icon>
            <search/>
          </template>
          搜索
        </el-button>
        <el-button plain type="primary" @click="props.resetQuery">
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
import {RolePageQuery} from "@/api/system/role/type";
import {FormInstance} from "element-plus";
// 组件定义
defineOptions({
  name: "RoleSearch",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  query: RolePageQuery;
  loadData: (callback?: () => void) => Promise<void>;                    // 加载数据函数
  resetQuery: (callback?: () => void) => Promise<void>;                  // 重置查询条件
}>(), {});

const emits = defineEmits<{
  (event: "update:query", query: RolePageQuery): void
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
