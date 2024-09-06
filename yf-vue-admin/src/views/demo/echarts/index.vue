<template>
  <div>
    <div ref="chartRef" style="width: 100%; height: 400px;"></div>
  </div>
</template>

<script lang="ts" setup>
// 数据
import {ChartStrategy, useECharts} from "@/hooks/useECharts";

// 引用容器的 ref
const chartRef = ref<HTMLDivElement | null>(null);

class LineOptionStrategy implements ChartStrategy {
  // 配置项
  getOptions() {
    return {
      xAxis: {
        type: 'category',
        data: ['', '', '', '', '', '', '']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: ["1", "1", "2", "1", "1", "1", "1"],
          type: 'line'
        }
      ]
    }
  }
}

const {options} = useECharts(chartRef, new LineOptionStrategy().getOptions());

setTimeout(() => {
  options.series[0].data = [210, 200, 200, 200, 200, 220, 280];
}, 2000)

</script>

<style lang="scss" scoped>
/* 样式 */
</style>
