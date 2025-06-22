<template>
    <div class="app-container">
        <!--  Search  -->
        <dfms-table-search
            v-model:query="query"
            :load-data="loadData"
            :reset-query="resetQuery"
        />
        <!--  Table  -->
        <dfms-table-table
            v-model:query="query"
            :data-list="dataList"
            :load-data="loadData"
            :loading="loading"
            :total="total"
        />
    </div>
  {{type}}
</template>

<script lang="ts" setup>
import {
        DfmsTablePageQuery,
        DfmsTablePageVO
    } from "@/api/dfms/table/type";
import {DfmsTableAPI} from "@/api/dfms/table";
import {useDataLoader} from "@/hooks/useDataLoader";
import {number} from "echarts";

// 组件定义
defineOptions({
    name: "DfmsTable",
    inheritAttrs: false,
});

const props = defineProps({
  type: Number,
});


// 公共数据 & 方法
const initialQuery: DfmsTablePageQuery = {
    tableName: undefined,

    tableComment: undefined,

    ddl: undefined,

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
} = useDataLoader<DfmsTablePageVO, DfmsTablePageQuery>(DfmsTableAPI.PAGE.request, initialQuery);
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
