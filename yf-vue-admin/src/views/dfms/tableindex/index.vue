<template>
    <div class="app-container">
        <!--  Search  -->
        <dfms-table-index-search
            v-model:query="query"
            :load-data="loadData"
            :reset-query="resetQuery"
        />
        <!--  Table  -->
        <dfms-table-index-table
            v-model:query="query"
            :data-list="dataList"
            :load-data="loadData"
            :loading="loading"
            :total="total"
        />
    </div>
</template>

<script lang="ts" setup>
import {
        DfmsTableIndexPageQuery,
        DfmsTableIndexPageVO
    } from "@/api/dfms/tableindex/type";
import {DfmsTableIndexAPI} from "@/api/dfms/tableindex";
import {useDataLoader} from "@/hooks/useDataLoader";

// 组件定义
defineOptions({
    name: "DfmsTableIndex",
    inheritAttrs: false,
});
// 公共数据 & 方法
const initialQuery: DfmsTableIndexPageQuery = {
    indexName: undefined,

    columns: undefined,

    indexType: [],

    isOnly: undefined,

    status: [],

    createBy: undefined,

    createTimeStart: undefined,
    createTimeEnd: undefined,

    updateByStart: undefined,
    updateByEnd: undefined,

    updateTimeStart: undefined,
    updateTimeEnd: undefined,

    pageNum: 1,
    pageSize: 10
}                       // 初始化查询条件
// hooks
const {
    query,
    dataList,
    total,
    loading,
    loadData,
    resetQuery
} = useDataLoader<DfmsTableIndexPageVO, DfmsTableIndexPageQuery>(DfmsTableIndexAPI.PAGE.request, initialQuery);
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
