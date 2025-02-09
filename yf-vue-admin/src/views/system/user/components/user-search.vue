<template>
  <div class="search-container">
    <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
      <el-form-item label="关键词 :" prop="keywords">
        <el-input
            v-model="query.keywords"
            clearable
            placeholder="用户名/昵称/手机号/邮箱"
            @keyup.enter="loadData"
        />
      </el-form-item>
      <el-form-item label="用户状态 :" prop="status">
        <el-select
            v-model="query.status"
            clearable
            placeholder="用户状态"
        >
          <el-option v-for="(value,key) in EnableStatusEnum.OPTIONS" :label="value" :value="Number(key)"/>
        </el-select>
      </el-form-item>

      <el-form-item label="创建时间 :" prop="startTime">
        <el-date-picker
            v-model="query.startTime"
            format="YYYY/MM/DD"
            placeholder="创建时间"
            type="date"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>

      <el-form-item label="截至时间 :" prop="endTime">
        <el-date-picker
            v-model="query.endTime"
            format="YYYY/MM/DD"
            placeholder="创建时间"
            type="date"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>

      <el-form-item label="所属部门 :" prop="deptId">
        <!--   v-loading="deptLoading"     -->
        <el-tree-select
            v-model="query.deptId"
            :data="props.deptOptions"
            check-strictly
            clearable
            @focus="props.loadDeptData"
        >
        </el-tree-select>
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
// 组件定义
import {FormInstance} from "element-plus";
import {UserPageQuery} from "@/api/system/user/type";
import {EnableStatusEnum} from "@/constants/system";

defineOptions({
  name: "UserSearch",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  query: UserPageQuery;
  deptOptions: OptionType[];                                             // 部门集合
  loadData: (callback?: () => void) => Promise<void>;                    // 加载数据函数
  resetQuery: (callback?: () => void) => Promise<void>;                  // 重置查询条件
}>(), {});

const emits = defineEmits<{
  (event: "update:query", query: UserPageQuery): void
}>()
// hooks
const query = useVModel(props, 'query', emits);
// 数据
const queryFormRef = ref<FormInstance | null>(null);         // 查询表单
// 方法

// 生命周期
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
