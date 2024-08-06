<template>
  <div class="settings-options">
    <span>展示logo</span>
    <el-switch v-model="systemStore.settings.sidebarLogo"/>
  </div>
  <div class="settings-options">
    <span>固定顶部</span>
    <el-switch v-model="systemStore.settings.fixedHeader"/>
  </div>
  <div class="settings-options">
    <span>展示面包屑</span>
    <el-switch v-model="systemStore.settings.breadCrumb"/>
  </div>
  <div class="settings-options">
    <span>展示页签</span>
    <el-switch v-model="systemStore.settings.tagsView"/>
  </div>
  <div class="settings-options">
    <span>展示水印</span>
    <el-switch v-model="systemStore.settings.watermarkEnabled"/>
  </div>
</template>

<script setup lang="ts">
import {useSystemStore} from "@/store/modules/system";

const systemStore = useSystemStore();
// 数据

// 方法

// 生命周期
</script>

<style scoped lang="scss">
/* 样式 */
.settings-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
}
</style>
