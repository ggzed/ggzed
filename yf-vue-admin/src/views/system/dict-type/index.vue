<template>
  <div class="app-container">
    <!--  搜索框  -->
    <dict-type-search
        v-model:query="query"
        :load-data="loadData"
        :reset-query="resetQuery"
    />
    <!--  Table  -->
    <dict-type-table
        v-model:query="query"
        :data-list="dataList"
        :load-data="loadData"
        :loading="loading"
        :total="total"
    />
  </div>
</template>

<script lang="ts" setup>
// 组件定义
import {useDataLoader} from "@/hooks/useDataLoader";
import {DictTypePageQuery, DictTypePageVO} from "@/api/system/dict-type/type";
import {DictTypeAPI} from "@/api/system/dict-type";

defineOptions({
  name: "DictType",
  inheritAttrs: false,
});
// 公共数据 & 方法
const initialQuery: DictTypePageQuery = {
  name: undefined,
  status: undefined,
  type: undefined,
  pageNum: 1,
  pageSize: 10
}                       // 初始化查询条件
const {
  query,
  dataList,
  total,
  loading,
  loadData,
  resetQuery
} = useDataLoader<DictTypePageVO, DictTypePageQuery>(DictTypeAPI.PAGE.request, initialQuery);
// 数据

// 方法

// 生命周期
onMounted(async () => {
  await loadData()
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
