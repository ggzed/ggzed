<template>
  <el-card class="visit-trend-card">
    <template #header>
      <div>
        <el-text size="large" tag="b">
          数据库类型分布 🚀
        </el-text>
      </div>
    </template>
    <div ref="chartRef" style="width: 100%; height: 400px;"></div>
  </el-card>

</template>

<script lang="ts" setup>
// 数据
import {useECharts} from "@/hooks/useECharts";
import {EChartsOption} from "echarts";
import {defineOptions, ref} from "vue";

defineOptions({
  name: "DatabaseTypeCard",
  inheritAttrs: false,
});

// 引用容器的 ref
const chartRef = ref<HTMLDivElement | null>(null);
const option = ref( {
  tooltip: {
    trigger: 'item'
  },
  legend: {
    top: '5%',
    left: 'center'
  },
  series: [
    {
      name: 'Access From',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 40,
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: [
        { value: 1048, name: 'Search Engine' },
        { value: 735, name: 'Direct' },
        { value: 580, name: 'Email' },
        { value: 484, name: 'Union Ads' },
        { value: 300, name: 'Video Ads' }
      ]
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
