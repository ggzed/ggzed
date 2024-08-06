<template>
  <div>
    Markdown : 采用字节的 markdown 组件( 优先考虑 ) , 或者 markdown-it 库等
  </div>
</template>

<script lang="ts" setup>

defineOptions({
  name: "Markdown",
  inheritAttrs: false,
});
// 数据

// 方法

// 生命周期
onMounted(() => {
  ElMessage.info("【Markdown模块】功能正在开发中...")
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
