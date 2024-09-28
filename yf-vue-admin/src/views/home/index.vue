<template>
  <!--  页面  -->
  <div class="home-container">
    <!--  欢迎信息  -->
    <el-card>
      <el-row justify="space-between">
        <el-col :span="24">
          <div class="user-info">
            <el-avatar :size="80" :src="userStore.userInfo.avatar">
              {{ userStore.userInfo.username }}
            </el-avatar>
            <div class="content">
              <p class="single-line">您好 , {{ userStore.userInfo.nickname }} !</p>
              <p class="double-line"> {{ weather }}</p>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!--  小功能向导  -->
    <el-row :gutter="20" class="action-cards">
      <el-col :lg="6" :sm="12" :xs="24">
        <router-link to="/hot-list">
          <el-card>
            <div>
              <svg-icon icon-class="hot-list" size="24"></svg-icon>
              <el-text tag="b">每日热榜</el-text>
            </div>
            <div>
              <el-icon>
                <ArrowRightBold/>
              </el-icon>
            </div>
          </el-card>
        </router-link>
      </el-col>
      <el-col :lg="6" :sm="12" :xs="24">
        <router-link to="/today-avatar">
          <el-card>
            <div>
              <svg-icon icon-class="today-avatar" size="24"></svg-icon>
              <el-text tag="b">照片推荐墙</el-text>
            </div>
            <div>
              <el-icon>
                <ArrowRightBold/>
              </el-icon>
            </div>
          </el-card>
        </router-link>
      </el-col>
      <el-col :lg="6" :sm="12" :xs="24">
        <el-card @click="systemStore.settings.settingsVisible = true">
          <div>
            <svg-icon icon-class="page-personalize" size="24"></svg-icon>
            <el-text tag="b">页面个性化</el-text>
          </div>
          <div>
            <el-icon>
              <ArrowRightBold/>
            </el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :lg="6" :sm="12" :xs="24">
        <el-card @click="systemStore.setTourStatus(true)">
          <div>
            <svg-icon icon-class="page-wizard" size="24"></svg-icon>
            <el-text tag="b">页面向导</el-text>
          </div>
          <div>
            <el-icon>
              <ArrowRightBold/>
            </el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!--  PV , UV , Echarts   -->
    <el-row :gutter="10">
      <el-col :span="16" :xs="24" style="margin-bottom: 10px;">
        <!-- 访问趋势统计图 -->
        <el-card class="visit-trend-card">
          <template #header>
            <div>
              <el-text size="large" tag="b">
                访问趋势 🚀
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
      </el-col>
      <el-col :span="8" :xs="24" style="margin-bottom: 10px;">
        <div class="image-wrapper">
          <el-image
              class="custom-image"
              fit="cover"
              src="https://api.vvhan.com/api/moyu"
          ></el-image>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
// 数据
import {useSystemStore} from "@/store/modules/system";
import {useUserStore} from "@/store/modules/user";
import {HomeAPI} from "@/api/home";
import {useECharts} from "@/hooks/useECharts";

defineOptions({
  name: "Home",
  inheritAttrs: false,
});

const VisitTrend = ref<HTMLDivElement | null>(null);

const systemStore = useSystemStore();
const userStore = useUserStore();
const visitTrendType = ref<string>("0");
const {options} = useECharts(VisitTrend, HomeAPI.VISIT_TREND.chartOptions())

const weather = ref<string>();
const visitTrendLoading = ref<boolean>(true);

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
    if (series && series.length >= 3) {
      series[0].data = data.pvList;
      series[1].data = data.uvList;
      series[2].data = data.ipList;
    }
  }).finally(() => {
    visitTrendLoading.value = false;
  });


}

onMounted(() => {
  // 获取天气信息
  HomeAPI.WEATHER.request().then((response) => {
    const {city, data, air, tip} = response.data
    const {date, week, type, low, high, fengxiang, fengli, night} = data;

    weather.value = `
    欢迎来到${city}！
    今天是 ${date} (${week})，天气 ${type}，
    气温范围为 ${low} 到 ${high}。白天${fengxiang} ${fengli}，
    晚上天气 ${night.type}，${night.fengxiang} ${night.fengli}。
    当前空气质量：${air.aqi_name} (AQI: ${air.aqi})。
    温馨提示：${tip}`
  })

  // 获取访问趋势信息
  initVisitTrendCharts();
})
</script>

<style lang="scss" scoped>
/* 样式 */
.home-container {
  display: flex;
  flex-direction: column; /* 设置基准线为上下方向 */
  justify-content: center;
}

.user-info {
  display: flex;
  gap: 20px;
  align-items: center;

  // 内部文字内容部分
  .content {
    flex: 1;
    max-width: 80vw;

    // 限制第一个p标签为单行
    .single-line {
      font-size: 16px;
      font-weight: 700;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 1;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    // 限制第二个p标签为双行
    .double-line {
      line-height: 24px;
      font-size: 16px;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 4;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: normal;
    }
  }
}

.action-cards {
  margin-top: 10px;
  margin-bottom: 20px;

  .el-card {
    margin-top: 10px;
  }

  :deep(.el-card__body) {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 10px;
    cursor: pointer;

    :first-child {
      display: flex;
      justify-content: center;
      align-items: center;
      gap: 8px;
    }
  }
}

// 摸鱼日历
.image-wrapper {
  width: 100%;
  height: 93.4%;
  overflow: hidden;
  position: relative;
}

.custom-image {
  width: 100%;
  height: auto;
  position: relative;
  top: -6.6%;
}


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
