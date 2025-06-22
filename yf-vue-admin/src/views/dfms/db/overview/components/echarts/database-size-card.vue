<template>
  <el-card class="visit-trend-card">
    <template #header>
      <div>
        <el-text size="large" tag="b"> 存储容量对比 🚀 </el-text>
      </div>
    </template>
    <div ref="chartRef" style="width: 100%; height: 400px;"></div>
  </el-card>
</template>

<script lang="ts" setup>
import { ref, computed, watch } from 'vue'
import { useECharts } from '@/hooks/useECharts'
import { DfmsDbOverviewVO } from '@/api/dfms/db/type'
import { EChartsOption } from 'echarts'

defineOptions({
  name: 'DatabaseSizeCard',
  inheritAttrs: false,
})

// 接收 props
const props = withDefaults(
    defineProps<{
      dataList: DfmsDbOverviewVO[]
    }>(),
    {
      dataList: () => [],
    }
)

// 图表容器引用
const chartRef = ref<HTMLDivElement | null>(null)

// 动态生成图表配置项（响应式）
const chartOption = computed<EChartsOption>(() => ({
  xAxis: {
    type: 'category',
    data: props.dataList.map(item => item.name),
  },
  yAxis: {
    type: 'value',
  },
  series: [
    {
      data: props.dataList.map(item => item.num),
      type: 'bar',
      showBackground: true,
      backgroundStyle: {
        color: 'rgba(180, 180, 180, 0.2)',
      },
    },
  ],
}))

// 初始化图表
const { chartInstance } = useECharts(chartRef, chartOption.value)

// 监听 props.dataList 变化并更新图表
watch(
    () => props.dataList,
    () => {
      chartInstance.value?.setOption(chartOption.value)
    },
    { deep: true }
)
</script>

<style lang="scss" scoped>
/* 样式 */
</style>