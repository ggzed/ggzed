<template>
  <h1>限流监控</h1>
  <div>采用SSE实时推送限流数据，以便修改</div>
</template>

<script lang="ts" setup>
defineOptions({
  name: "RateLimiter",
  inheritAttrs: false,
});
// 数据

// 方法

// 生命周期
onMounted(() => {
  ElMessage.info("【限流监控模块】功能正在开发中...")
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
