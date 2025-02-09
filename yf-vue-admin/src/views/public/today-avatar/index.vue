<template>
  <div class="masonry">
    <div v-for="(image, index) in HomeAPI.IMAGES_API.endpoint" :key="index" class="masonry-item">
      <el-image
          :alt="'Image ' + (index + 1)"
          :src="image"
          class="masonry-image"
          fit="cover"
          lazy
      >
        <template #placeholder>
          <div class="image-placeholder">
            <i class="el-icon-loading"></i>
          </div>
        </template>
        <template #error>
          <div class="image-error">
            <i class="el-icon-picture-outline"></i>
          </div>
        </template>
      </el-image>
    </div>
  </div>
</template>

<script lang="ts" setup>
defineOptions({
  name: "TodayAvatar",
  inheritAttrs: false,
});
// 数据

// 方法

// 生命周期
import {HomeAPI} from "@/api/home";
</script>

<style lang="scss" scoped>
/* 样式 */
.masonry {
  column-count: 2; /* 定义列数 */
  column-gap: 10px; /* 列间距 */
}

.masonry-item {
  break-inside: avoid; /* 防止元素被打断 */
  margin-bottom: 10px;
}

.masonry-image {
  width: 100%;
}

.image-placeholder {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background-color: #f5f5f5;
}

.image-error {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background-color: #ffeded;
  color: #ff4949;
}
</style>
