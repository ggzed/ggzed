<template>
  <div class="app-container">
    <dept-search
        v-model:query="query"
        :load-data="loadData"
        :reset-query="resetQuery"
    />

    <dept-table
        :data-list="dataList"
        :load-data="loadData"
        :loading="loading"
    />
  </div>
</template>

<script lang="ts" setup>
import {DeptPageQuery, DeptPageVO} from "@/api/system/dept/type";
import {DeptAPI} from "@/api/system/dept";
import {useDataLoader} from "@/hooks/useDataLoader";

// 组件定义
defineOptions({
  name: "PgsqlList",
  inheritAttrs: false,
});
// 公共数据 & 方法
const initialQuery: DeptPageQuery = {
  keywords: undefined,
  status: undefined
}                       // 初始化查询条件
const {
  query,
  dataList,
  loading,
  loadData,
  resetQuery
} = useDataLoader<DeptPageVO, DeptPageQuery>(DeptAPI.PAGE.request, initialQuery);

// 生命周期
onMounted(async () => {
  await loadData()
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
