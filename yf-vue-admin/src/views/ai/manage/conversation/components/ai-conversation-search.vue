<template>
  <div class="search-container">
    <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
      <el-form-item label="会话标题" prop="title">
        <el-input
            v-model="query.title"
            clearable
            placeholder="会话标题"
            @keyup.enter="props.loadData()"
        />
      </el-form-item>
      <el-form-item label="活跃时间" prop="lastActiveTimeStart">
        <el-date-picker
            v-model="query.lastActiveTimeStart"
            format="YYYY/MM/DD HH:mm:ss"
            placeholder="会话活跃开始时间"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item label="活跃截止" prop="lastActiveTimeEnd">
        <el-date-picker
            v-model="query.lastActiveTimeEnd"
            format="YYYY/MM/DD HH:mm:ss"
            placeholder="会话活动结束时间"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTimeStart">
        <el-date-picker
            v-model="query.createTimeStart"
            format="YYYY/MM/DD HH:mm:ss"
            placeholder="创建时间"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item label="截止时间" prop="createTimeEnd">
        <el-date-picker
            v-model="query.createTimeEnd"
            format="YYYY/MM/DD HH:mm:ss"
            placeholder="截止时间"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
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
import {AiConversationPageQuery} from "@/api/ai/conversation/type";
import {DictType} from "@/api/system/dict-data/type";
import {FormInstance} from "element-plus";

// 组件定义
defineOptions({
  name: "AiConversationSearch",
  inheritAttrs: false,
});

// 组件 props & emits
const props = withDefaults(defineProps<{
  query: AiConversationPageQuery;
  dictData?: Record<DictType | string, Record<any, string>>;
  loadData: (callback?: () => void) => Promise<void>;
  resetQuery: (callback?: () => void) => Promise<void>;                  // 重置查询条件
}>(), {dictData: () => ({})});

const emits = defineEmits<{
  (event: "update:query", query: AiConversationPageQuery): void
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
