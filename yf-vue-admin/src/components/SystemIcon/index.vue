<template>
  <el-icon v-if="icon && icon.startsWith('el-icon')">
    <component :is="icon.replace('el-icon-', '')"/>
  </el-icon>
  <div v-else class="svg-icon-container">
    <svg-icon v-if="icon && icon !== ''" :icon-class="icon" size="18px"/>
    <svg-icon v-else icon-class="logo" size="18px"/>
  </div>
</template>
<script lang="ts" setup>
// 数据
defineProps({
  icon: {
    type: String,
    default: "",
  },
});
</script>

<style lang="scss" scoped>
/* 样式 */
// 统一为 element-plus 中 el-icon 的样式
.el-icon {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 24px;
  height: 100%;
  margin-right: 6px;
}

.svg-icon-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 24px;
  height: 100%;
  margin-right: 6px;
}
</style>
