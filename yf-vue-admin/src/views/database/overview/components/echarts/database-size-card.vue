<template>
  <!-- 访问趋势统计图 -->
  <el-card class="visit-trend-card">
    <template #header>
      <div>
        <el-text size="large" tag="b">
          存储容量对比 🚀
        </el-text>
      </div>
    </template>
    <div ref="chartRef" style="width: 100%; height: 400px;"></div>
  </el-card>
</template>

<script lang="ts" setup>
import {useECharts} from "@/hooks/useECharts";
import {HomeAPI} from "@/api/home";
// 组件定义
defineOptions({
  name: "DatabaseSizeCard",
  inheritAttrs: false,
});

// 引用容器的 ref
const chartRef = ref<HTMLDivElement | null>(null);
const option = ref({
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      data: [120, 200, 150, 80, 70, 110, 130],
      type: 'bar',
      showBackground: true,
      backgroundStyle: {
        color: 'rgba(180, 180, 180, 0.2)'
      }
    }
  ]
});

const initChart = ref<EChartsOption>(option);


const {chartInstance} = useECharts(chartRef, initChart.value as EChartsOption);

function changeInitChart() {
  chartInstance.value?.setOption(initChart.value);
}
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
