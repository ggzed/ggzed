<template>
    <div class="app-container">
        <!--  Search  -->
        <dfms-table-fields-search
            v-model:query="query"
            :load-data="loadData"
            :reset-query="resetQuery"
        />
        <!--  Table  -->
        <dfms-table-fields-table
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
        DfmsTableFieldsPageQuery,
        DfmsTableFieldsPageVO
    } from "@/api/dfms/tablefields/type";
import {DfmsTableFieldsAPI} from "@/api/dfms/tablefields";
import {useDataLoader} from "@/hooks/useDataLoader";

// 组件定义
defineOptions({
    name: "DfmsTableFields",
    inheritAttrs: false,
});
// 公共数据 & 方法
const initialQuery: DfmsTableFieldsPageQuery = {
    showName: undefined,

    columnName: undefined,

    columnType: [],

    columnComment: undefined,

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
} = useDataLoader<DfmsTableFieldsPageVO, DfmsTableFieldsPageQuery>(DfmsTableFieldsAPI.PAGE.request, initialQuery);
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
