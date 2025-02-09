<template>
  <el-row v-loading="loading" :gutter="20" style="min-height: 300px">
    <template v-for="(hotInfo, index) in hotList" :key="index">
      <el-col :lg="8" :sm="12" :xs="24">
        <el-card class="hot-item" shadow="hover">
          <template #header>
            <div>
              <svg-icon icon-class="hot-list" size="24"></svg-icon>
              <el-text tag="b">{{ hotInfo.name }} : {{ hotInfo.subtitle }}</el-text>
            </div>
            <div>
              {{ hotInfo.update_time }}
            </div>
          </template>
          <!-- 内容 -->
          <el-scrollbar height="300">
            <div v-for="(hotInfoItem,itemIndex) in hotInfo.data" :key="itemIndex" class="hot-item-content">
              <el-link :href="hotInfoItem.url" target="_blank">
                <p>
                  {{ hotInfoItem.title }}
                </p>
                <p>
                  {{ hotInfoItem.hot }}
                </p>
              </el-link>
            </div>
          </el-scrollbar>
        </el-card>
      </el-col>
    </template>
  </el-row>
</template>

<script lang="ts" setup>
// 数据
const hotList = ref<HotList[]>([])
const loading = ref(true)
// 方法

// 生命周期
import {HomeAPI} from "@/api/home";

onMounted(() => {
  loading.value = true
  HomeAPI.HOT_LIST.request().then(({data}) => {
    hotList.value = data.data
  }).finally(() => {
    loading.value = false;
  })
})
</script>

<style lang="scss" scoped>
/* 样式 */
.hot-item {
  margin-bottom: 10px;

  :deep(.el-card__header) {
    display: flex;
    justify-content: space-between;
    align-items: center;

    :first-child {
      display: flex;
      justify-content: center;
      align-items: center;
      gap: 6px;
    }
  }

  .hot-item-content {
    width: 100%;

    .el-link {
      display: block;
    }

    :deep(.el-link__inner) {
      width: 100%;
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 6px;
      gap: 20px;

      :first-child {
        flex: 8;
      }

      :last-child {
        flex: 2;
      }
    }
  }

}
</style>
