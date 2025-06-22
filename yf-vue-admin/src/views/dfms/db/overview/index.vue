<!-- ParentComponent.vue -->
<template>
    <database-up-card
        :data-list="dataList"
    />
  <el-row :gutter="20">
    <el-col :span="12" style="margin-bottom: 10px;">
      <database-type-card
          :data-list="dataList"
      />
    </el-col>
    <el-col :span="12" style="margin-bottom: 10px;">
      <database-size-card
          :data-list="dataList"
      />
    </el-col>
  </el-row>
</template>

<script lang="ts" setup>
import {
  DfmsDbOverviewVO,
} from "@/api/dfms/db/type";
import {DfmsDbAPI} from "@/api/dfms/db";
import {useDataLoader} from "@/hooks/useDataLoader";
import DatabaseUpCard from "@/views/dfms/db/overview/components/echarts/database-up-card.vue";

// 组件定义
defineOptions({
  name: "DfmsDbOverview",
  inheritAttrs: false,
});
// 公共数据 & 方法
// const initialQuery = ref
// hooks
const {
  dataList,
  loadData,
} = useDataLoader<DfmsDbOverviewVO>(DfmsDbAPI.OVERVIEW.request);
// 数据

// 方法

// 生命周期
onMounted(async () => {
  await loadData()
})
</script>