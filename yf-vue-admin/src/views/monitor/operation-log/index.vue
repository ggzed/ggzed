<template>
  <div class="app-container">
    <!--  搜索框   -->
    <operation-log-search
        v-model:query="query"
        :business-dict="businessDict"
        :load-data="loadData"
        :operator-type-dict="operatorTypeDict"
        :reset-query="resetQuery"
    />
    <!--  表格   -->
    <operation-log-table
        v-model:query="query"
        :business-dict="businessDict"
        :data-list="dataList"
        :load-data="loadData"
        :loading="loading"
        :operator-type-dict="operatorTypeDict"
        :total="total"
    />
  </div>
</template>

<script lang="ts" setup>
// 组件定义
import {OperationLogAPI} from "@/api/monitor/operation-log";
import {OperationLogPageQuery, OperationLogVO} from "@/api/monitor/operation-log/type";
import {useDataLoader} from "@/hooks/useDataLoader";
import {useDictionary} from "@/hooks/userDict";
import {DictType} from "@/api/system/dict-data/type";

defineOptions({
  name: "OperationLog",
  inheritAttrs: false,
});
// 公共数据 & 方法
const initialQuery: OperationLogPageQuery = {
  businessType: undefined,
  costTime: undefined,
  method: undefined,
  operatorIp: undefined,
  operatorName: undefined,
  operatorType: undefined,
  operatorUrl: undefined,
  title: undefined,
  endTime: undefined,
  startTime: undefined,
  status: undefined,
  pageNum: 1,
  pageSize: 10
}                       // 初始化查询条件
const {
  query,
  dataList,
  loading,
  total,
  loadData,
  resetQuery
} = useDataLoader<OperationLogVO, OperationLogPageQuery>(OperationLogAPI.PAGE.request, initialQuery);
const businessDict = await useDictionary(DictType.BUSINESS)                // 日志业务类型字典数据
const operatorTypeDict = await useDictionary(DictType.OPERATOR_TYPE)       // 日志操作类型字典数据


// 生命周期
onMounted(async () => {
  await loadData()
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
