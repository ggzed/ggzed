<template>
  <div class="app-container">
    <iframe src="https://chat3.eqing.tech/#/chat"></iframe>
  </div>
</template>

<script lang="ts" setup>
defineOptions({
  name: "EasyChat",
  inheritAttrs: false,
});
// 数据

// 方法

// 生命周期
</script>

<style lang="scss" scoped>
/* 样式 */
.app-container {
  width: 100%;
  height: 82.5vh;

  > iframe {
    width: 100%;
    height: 100%;
  }
}
</style>
