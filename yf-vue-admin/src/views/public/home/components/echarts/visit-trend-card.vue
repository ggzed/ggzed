<template>
  <!-- 访问趋势统计图 -->
  <el-card class="visit-trend-card">
    <template #header>
      <div>
        <el-text size="large" tag="b">
          系统健康状态 🚀
        </el-text>
      </div>
      <el-radio-group v-model="visitTrendType" size="small" @change="initVisitTrendCharts">
        <el-radio-button label="近7天" value="0"/>
        <el-radio-button label="近1个月" value="1"/>
        <el-radio-button label="近6个月" value="2"/>
      </el-radio-group>
    </template>
    <div ref="VisitTrend" v-loading="visitTrendLoading" class="visit-trend"></div>
  </el-card>
</template>

<script lang="ts" setup>
import {useECharts} from "@/hooks/useECharts";
import {HomeAPI} from "@/api/home";
// 组件定义
defineOptions({
  name: "VisitTrendCard",
  inheritAttrs: false,
});

// 数据
const visitTrendLoading = ref<boolean>(true);
const VisitTrend = ref<HTMLDivElement | null>(null);
const visitTrendType = ref<string>("0");
const {options} = useECharts(VisitTrend, HomeAPI.VISIT_TREND.chartOptions())

// 方法
/**
 * 初始化访问趋势图表
 */
function initVisitTrendCharts() {
  visitTrendLoading.value = true;
  const now = new Date(); // 当前日期
  let startDate = new Date(now); // 默认设置为当前日期
  // 处理时间
  switch (visitTrendType.value) {
    case "0": // 近七天
      startDate.setDate(now.getDate() - 6); // 从 7 天前开始
      break;
    case "1": // 近一个月
      startDate.setMonth(now.getMonth() - 1); // 从 1 个月前开始
      break;
    case "2": // 近六个月
      startDate.setMonth(now.getMonth() - 6); // 从 6 个月前开始
      break;
    default:
      console.error("Invalid visitTrendType value");
      return null;
  }

  // 格式化日期
  const formattedStartDate = useDateFormat(startDate, 'YYYY-MM-DD').value;
  const formattedEndDate = useDateFormat(now, 'YYYY-MM-DD').value;

  // 获取访问趋势数据
  HomeAPI.VISIT_TREND.request(formattedStartDate, formattedEndDate).then(({data}) => {
    // 使用类型断言确保 xAxis 是带有 type 属性的对象
    const xAxis = options.xAxis as { type: string; data?: string[] };
    if (xAxis && xAxis.type === "category") {
      xAxis.data = data.dates;
    }

    // 使用类型断言确保 series 是一个数组
    const series = options.series as Array<{ data: number[] }>;
    if (series && series.length >= 4) {
      series[0].data = data.pvList;
      series[1].data = data.uvList;
      series[2].data = data.ipList;
      series[3].data = data.ipList;
    }
  }).finally(() => {
    visitTrendLoading.value = false;
  });
}

// 生命周期
onMounted(() => {
  initVisitTrendCharts();
})
</script>

<style lang="scss" scoped>
/* 样式 */
.visit-trend-card {
  // 卡片头部样式
  :deep(.el-card__header) {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  //  访问趋势 统计图
  .visit-trend {
    height: 500px;
    width: 100%;
  }
}
</style>
