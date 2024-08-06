<template>
  <h1>在线用户</h1>
  <div> Redis 存储用户在线信息 , SSE 定时推送用户在线人数（ 提供实时图数据 ）</div>
</template>

<script lang="ts" setup>
defineOptions({
  name: "OnlineUser",
  inheritAttrs: false,
});
// 数据

// 方法

// 生命周期
onMounted(() => {
  ElMessage.info("【在线用户模块】功能正在开发中...")
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
