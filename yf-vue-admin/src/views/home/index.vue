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
              <p class="double-line">今日天气晴朗，气温在15℃至25℃之间，东南风。今日天气晴朗，气温在15℃至25℃之间，东南风。</p>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!--  小功能向导  -->
    <el-row :gutter="20" class="action-cards">
      <el-col :lg="6" :sm="12" :xs="24">
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
      </el-col>
      <el-col :lg="6" :sm="12" :xs="24">
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
    <el-row :gutter="10" class="mt-5">
      <el-col :span="16" :xs="24" style="margin-bottom: 10px;">
        <!-- 访问趋势统计图 -->
        <div ref="VisitTrend" class="visit-trend"></div>
      </el-col>
      <el-col :span="8" :xs="24" style="margin-bottom: 10px;">
        <div class="image-wrapper">
          <el-image
              class="custom-image"
              fit="cover"
              loading="lazy"
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

defineOptions({
  name: "Home",
  inheritAttrs: false,
});

const systemStore = useSystemStore();
const userStore = useUserStore();
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
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 1;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    // 限制第二个p标签为双行
    .double-line {
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
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

//  U/PV 统计图
.visit-trend {
  height: 600px;
  width: 100%;
  background-color: red;
}

</style>
