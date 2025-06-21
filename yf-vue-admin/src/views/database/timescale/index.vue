<template>
  <el-tabs
      v-model="activeName"
      type="card"
      class="demo-tabs"
      @tab-click="handleClick"
  >
    <el-tab-pane label="数据库列表" name="first"><pgsql-list/></el-tab-pane>
    <el-tab-pane label="时间序列" name="second"><pgsql-table/></el-tab-pane>
    <el-tab-pane label="时序查询" name="third"><pgsql-search/></el-tab-pane>
    <el-tab-pane label="保留策略" name="third"><pgsql-search/></el-tab-pane>
    <el-tab-pane label="性能监控" name="third"><pgsql-search/></el-tab-pane>
  </el-tabs>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import type { TabsPaneContext } from 'element-plus'
import PgsqlList from "@/views/database/pgsql/components/list/pgsql-list.vue";


defineOptions({
  name: "TimescaleDB",
  inheritAttrs: false,
});

const activeName = ref('first')

const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event)
}
</script>

<style>
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
</style>