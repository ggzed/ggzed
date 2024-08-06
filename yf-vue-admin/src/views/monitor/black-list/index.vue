<template>
  <h1>系统黑名单</h1>
  <div>不采用SSE实时推送，并不是很重要,也没有实时图</div>
</template>

<script lang="ts" setup>
defineOptions({
  name: "BlackList",
  inheritAttrs: false,
});
// 数据

// 方法

// 生命周期
onMounted(() => {
  ElMessage.info("【系统黑名单模块】功能正在开发中...")
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
