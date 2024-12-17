<template>
  <div>
    <h1>Markdown模块</h1>
    <p>功能正在开发中...</p>
    <p>收到来自Markdown模块的信息 ID 为 {{ markdownDetailId }}</p>
  </div>
</template>

<script lang="ts" setup>

defineOptions({
  name: "MarkdownDetail",
  inheritAttrs: false,
});
const route = useRoute();
// 数据
const markdownDetailId = computed(() => (route.params.id || -1) as number)  // 字典类型ID
// 方法

// 生命周期
onMounted(() => {
  ElMessage.info("【Markdown模块】功能正在开发中...")
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
