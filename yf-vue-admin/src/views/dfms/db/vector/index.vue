<template>
    <div class="app-container">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane label="向量集合" name="first">
          <!--  Search  -->
          <dfms-db-vector-search
              v-model:query="query"
              :load-data="loadData"
              :reset-query="resetQuery"
          />
          <!--  Table  -->
          <dfms-db-vector-table
              v-model:query="query"
              :data-list="dataList"
              :load-data="loadData"
              :loading="loading"
              :total="total"
          />
        </el-tab-pane>
        <el-tab-pane label="索引管理" name="second">
          <table-manage/>
        </el-tab-pane>
        <el-tab-pane label="向量搜索" name="third">
          <sql-search/>
        </el-tab-pane>
      </el-tabs>
    </div>
</template>

<script lang="ts" setup>
import {
        DfmsDbPageQuery,
        DfmsDbPageVO
    } from "@/api/dfms/db/type";
import {DfmsDbAPI} from "@/api/dfms/db";
import {useDataLoader} from "@/hooks/useDataLoader";

import TableManage from "@/views/dfms/table/tableManage.vue";
import SqlSearch from "@/views/dfms/db/sqlSearch.vue";
import type {TabsPaneContext} from 'element-plus'

const activeName = ref('first')

const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event)
}
// 组件定义
defineOptions({
    name: "DfmsDbVector",
    inheritAttrs: false,
});
// 公共数据 & 方法
const initialQuery: DfmsDbPageQuery = {
    name: undefined,
    charset: undefined,
    collation: undefined,
    size: undefined,
    tables: undefined,
    type: 3,
    retentionPolicy: undefined,
    sequenceNumber: undefined,
    dataPointsNumber: undefined,

    dimension: undefined,

    vectorNumber: undefined,

    indexType: [],

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
} = useDataLoader<DfmsDbPageVO, DfmsDbPageQuery>(DfmsDbAPI.PAGE.request, initialQuery);
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
