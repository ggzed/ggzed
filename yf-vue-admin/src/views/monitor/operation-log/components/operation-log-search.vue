<template>
  <div class="search-container">
    <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
      <el-form-item label="模块标题 :" prop="title">
        <el-input
            v-model="query.title"
            clearable
            placeholder="模块标题"
            @keyup.enter="props.loadData"
        />
      </el-form-item>
      <el-form-item label="业务类型 :" prop="businessType">
        <el-select v-model="query.businessType" clearable placeholder="业务类型选择">
          <el-option v-for="(value,key) in props.businessDict" :label="value" :value="Number(key)"/>
        </el-select>
      </el-form-item>

      <el-form-item label="客户端 :" prop="operatorType">
        <el-select v-model="query.operatorType" clearable placeholder="对接客户端选择">
          <el-option v-for="(value,key) in props.operatorTypeDict" :label="value" :value="Number(key)"/>
        </el-select>
      </el-form-item>

      <el-form-item label="方法名 :" prop="method">
        <el-input
            v-model="query.method"
            clearable
            placeholder="操作方法名"
            @keyup.enter="props.loadData"
        />
      </el-form-item>

      <el-form-item label="操作人 :" prop="method">
        <el-input
            v-model="query.operatorName"
            clearable
            placeholder="操作人用户名搜索"
            @keyup.enter="props.loadData"
        />
      </el-form-item>

      <el-form-item label="请求路径 :" prop="operatorUrl">
        <el-input
            v-model="query.operatorUrl"
            clearable
            placeholder="请求路径搜索"
            @keyup.enter="props.loadData"
        />
      </el-form-item>

      <el-form-item label="请求ip :" prop="operatorIp">
        <el-input
            v-model="query.operatorIp"
            clearable
            placeholder="请求ip精确搜索"
            @keyup.enter="props.loadData"
        />
      </el-form-item>

      <el-form-item label="响应状态 :" prop="status">
        <el-select
            v-model="query.status"
            clearable
            placeholder="响应状态搜索"
        >
          <el-option v-for="(value,key) in OperatorLogStatusEnum.OPTIONS" :label="value" :value="Number(key)"/>
        </el-select>
      </el-form-item>

      <el-form-item label="接口耗时 :" prop="operatorIp">
        <el-input
            v-model="query.costTime"
            clearable
            placeholder="毫秒,搜索大于您输入的耗时"
            @keyup.enter="props.loadData"
        />
      </el-form-item>

      <el-form-item label="开始时间 :" prop="startTime">
        <el-date-picker
            v-model="query.startTime"
            format="YYYY/MM/DD HH:mm:ss"
            placeholder="操作开始时间"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>

      <el-form-item label="截止时间 :" prop="endTime">
        <el-date-picker
            v-model="query.endTime"
            format="YYYY/MM/DD HH:mm:ss"
            placeholder="操作截止时间"
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
import {OperatorLogStatusEnum} from "@/constants/system";
import {OperationLogPageQuery} from "@/api/monitor/operation-log/type";
import {FormInstance} from "element-plus";
// 组件定义
defineOptions({
  name: "OperationLogSearch",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  query: OperationLogPageQuery;
  businessDict: Record<number | string, string>;                  // 日志业务类型字典数据
  operatorTypeDict: Record<number | string, string>;              // 日志操作类型字典数据
  loadData: (callback?: () => void) => Promise<void>;                    // 加载数据函数
  resetQuery: (callback?: () => void) => Promise<void>;                  // 重置查询条件
}>(), {});

const emits = defineEmits<{
  (event: "update:query", query: OperationLogPageQuery): void
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
