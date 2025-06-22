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
import { ref, computed, watch } from 'vue'
import { useECharts } from "@/hooks/useECharts"
import { EChartsOption } from "echarts"
import { DfmsDbOverviewVO } from "@/api/dfms/db/type"

// 组件定义
defineOptions({
  name: "DatabaseTypeCard",
  inheritAttrs: false,
})

// 接收 props
const props = withDefaults(defineProps<{
  dataList: DfmsDbOverviewVO[]
}>(), {
  dataList: () => []
})

// 图表容器
const chartRef = ref<HTMLDivElement | null>(null)

// 动态生成图表配置项（响应式）
const chartOption = computed<EChartsOption>(() => ({
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
      data: props.dataList.map(item => ({
        value: item.num,
        name: item.name
      }))
    }
  ]
}))

// 初始化图表
const { chartInstance } = useECharts(chartRef, chartOption.value as EChartsOption)

// 监听 props.dataList 变化，动态更新图表
watch(() => props.dataList, () => {
  chartInstance.value?.setOption(chartOption.value)
}, { deep: true })
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
