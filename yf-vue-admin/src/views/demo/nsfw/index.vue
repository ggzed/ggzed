<template>
  <div>
    对接文件上传组件, 请求后端对图进行鉴黄, 返回鉴黄结果, 并展示鉴黄结果
  </div>
</template>

<script lang="ts" setup>

defineOptions({
  name: "Nsfw",
  inheritAttrs: false,
});
// 数据

// 方法

// 生命周期
onMounted(() => {
  ElMessage.info("【NSFW 模块】功能正在完善中...")
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
