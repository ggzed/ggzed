<template>
  <div class="search-container">
    <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
      <el-form-item label="角色" prop="role">
        <el-select
            v-model="query.role"
            clearable
            filterable
            multiple
            placeholder="角色"
            @change="props.loadData()"
        >
          <el-option v-for="(value,key) in props.dictData['ai_role']" :label="value"
                     :value="key"/>
        </el-select>
      </el-form-item>
      <el-form-item label="聊天内容" prop="content">
        <el-input
            v-model="query.content"
            clearable
            placeholder="聊天内容"
            @keyup.enter="props.loadData()"
        />
      </el-form-item>
      <el-form-item label="开始时间" prop="sendTimeStart">
        <el-date-picker
            v-model="query.sendTimeStart"
            format="YYYY/MM/DD HH:mm:ss"
            placeholder="发送消息开始时间"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item label="截止时间" prop="sendTimeEnd">
        <el-date-picker
            v-model="query.sendTimeEnd"
            format="YYYY/MM/DD HH:mm:ss"
            placeholder="发送消息截止时间"
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
import {AiMessagePageQuery} from "@/api/ai/message/type";
import {DictType} from "@/api/system/dict-data/type";
import {FormInstance} from "element-plus";

// 组件定义
defineOptions({
  name: "AiMessageSearch",
  inheritAttrs: false,
});

// 组件 props & emits
const props = withDefaults(defineProps<{
  query: AiMessagePageQuery;
  dictData?: Record<DictType | string, Record<any, string>>;
  loadData: (callback?: () => void) => Promise<void>;
  resetQuery: (callback?: () => void) => Promise<void>;                  // 重置查询条件
}>(), {dictData: () => ({})});

const emits = defineEmits<{
  (event: "update:query", query: AiMessagePageQuery): void
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
