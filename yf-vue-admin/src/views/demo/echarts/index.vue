<template>
  <div>
    <el-row :gutter="20">
      <el-col :lg="12" :md="12" :sm="12" :span="12" :xs="24" style="margin: 24px 0;">
        <div ref="chartRef" style="width: 100%; height: 400px;"></div>
      </el-col>
      <el-col :lg="12" :md="12" :sm="12" :span="12" :xs="24">
        <vue-json-pretty
            v-model:data="initChart"
            :collapsed-node-length="50"
            :deep="2"
            :editable="true"
            :show-double-quotes="false"
            :show-length="true"
            :show-line-number="true"
            editable-trigger="dblclick"
            @change="changeInitChart"
        />
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
// 数据
import {useECharts} from "@/hooks/useECharts";
import {EChartsOption} from "echarts";

// 引用容器的 ref
const chartRef = ref<HTMLDivElement | null>(null);

const initChart = ref<EChartsOption>({
  title: {
    text: 'ECharts展示图',
    subtext: 'useECharts',
    left: 'center'
  },
  legend: {
    top: 'bottom'
  },
  toolbox: {
    show: true,
    feature: {
      mark: {show: true},
      dataView: {show: true, readOnly: false},
      restore: {show: true},
      saveAsImage: {show: true}
    }
  },
  series: [
    {
      name: 'ECharts展示图',
      type: 'pie',
      radius: [25, 125],
      center: ['50%', '50%'],
      roseType: 'area',
      itemStyle: {
        borderRadius: 8
      },
      data: [
        {value: 40, name: 'rose 1'},
        {value: 38, name: 'rose 2'},
        {value: 32, name: 'rose 3'},
        {value: 30, name: 'rose 4'},
        {value: 28, name: 'rose 5'},
        {value: 26, name: 'rose 6'},
        {value: 22, name: 'rose 7'},
        {value: 18, name: 'rose 8'}
      ]
    }
  ]
});


const {chartInstance} = useECharts(chartRef, initChart.value as EChartsOption);

function changeInitChart() {
  chartInstance.value?.setOption(initChart.value);
}
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
