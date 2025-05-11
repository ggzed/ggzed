<template>
  <div class="app-container">
    <crud-search
        v-model:query="query"
        :load-data="loadData"
        :reset-query="resetQuery"
    />

    <crud-table
        v-model:query="query"
        :data-list="dataList"
        :load-data="loadData"
        :loading="loading"
        :total="total"
    />
  </div>
</template>

<script lang="ts" setup>
import {useDataLoader} from "@/hooks/useDataLoader";
import {GenCrudTablePageQuery, GenCrudTableVO} from "@/api/generate/crud-code/type";
import {GenerateCrudAPI} from "@/api/generate/crud-code";

// 组件定义
defineOptions({
  name: "Crud",
  inheritAttrs: false,
});
// 公共数据 & 方法
const initialQuery: GenCrudTablePageQuery = {
  parentMenuId: undefined,
  functionAuthor: undefined,
  tableName: undefined,
  tableComment: undefined,
  pageNum: 1,
  pageSize: 10,
}                       // 初始化查询条件
const {
  query,
  dataList,
  total,
  loading,
  loadData,
  resetQuery
} = useDataLoader<GenCrudTableVO, GenCrudTablePageQuery>(GenerateCrudAPI.PAGE.request, initialQuery);

// 生命周期
onMounted(async () => {
  await loadData()
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
