<template>
    <div class="app-container">
        <!--  Search  -->
        <dfms-node-search
            v-model:query="query"
            :load-data="loadData"
            :reset-query="resetQuery"
        />
        <!--  Table  -->
        <dfms-node-table
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
        DfmsNodePageQuery,
        DfmsNodePageVO
    } from "@/api/dfms/node/type";
import {DfmsNodeAPI} from "@/api/dfms/node";
import {useDataLoader} from "@/hooks/useDataLoader";

// 组件定义
defineOptions({
    name: "DfmsNode",
    inheritAttrs: false,
});
// 公共数据 & 方法
const initialQuery: DfmsNodePageQuery = {
    name: undefined,

    ip: undefined,

    port: undefined,

    username: undefined,

    role: undefined,

    status: [],

    cpu: undefined,

    memory: undefined,

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
} = useDataLoader<DfmsNodePageVO, DfmsNodePageQuery>(DfmsNodeAPI.PAGE.request, initialQuery);
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
