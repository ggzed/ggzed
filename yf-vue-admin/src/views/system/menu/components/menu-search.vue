<template>
  <div class="search-container">
    <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
      <el-form-item label="关键词 :" prop="keywords">
        <el-input
            v-model="query.keywords"
            clearable
            placeholder="菜单名称"
            @keyup.enter="props.loadData()"
        />
      </el-form-item>
      <el-form-item label="菜单类型 :" prop="type">
        <el-select v-model="query.type" placeholder="菜单类型选择">
          <el-option v-for="(value,key) in props.menuDict" :label="value" :value="Number(key)"/>
        </el-select>
      </el-form-item>
      <el-form-item label="隐藏类型 :" prop="hidden">
        <el-select v-model="query.hidden" placeholder="菜单是否隐藏">
          <el-option v-for="(value,key) in HiddenStatusEnum.OPTIONS" :label="value" :value="Number(key)"/>
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
import {HiddenStatusEnum} from "@/constants/system";
import {MenuPageQuery} from "@/api/system/menu/type";
import {FormInstance} from "element-plus";

defineOptions({
  name: "MenuSearch",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  query: MenuPageQuery;
  loadData: (callback?: () => void) => Promise<void>;                    // 加载数据函数
  resetQuery: (callback?: () => void) => Promise<void>;                  // 重置查询条件
  menuDict: Record<number | string, string>;                             // 菜单类型
}>(), {});

const emits = defineEmits<{
  (event: "update:query", query: MenuPageQuery): void
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
