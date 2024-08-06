<template>
  <el-drawer
      v-model="systemStore.settings.settingsVisible"
      :with-header="false"
      size="300"
      :direction="'rtl'"
  >
    <el-divider>主题设置</el-divider>
    <ThemeSettings></ThemeSettings>
    <el-divider>界面设置</el-divider>
    <ScreenSettings></ScreenSettings>
    <el-divider>导航设置</el-divider>
    <MenuSettings></MenuSettings>
    <el-divider>动画设置</el-divider>
    <AnimateSettings></AnimateSettings>
    <el-divider>其他设置</el-divider>
  </el-drawer>
</template>

<script setup lang="ts">
import {useSystemStore} from "@/store/modules/system";
// 数据
const systemStore = useSystemStore();


</script>

<style scoped lang="scss">
// 设置选项样式，子组件公共样式
.settings-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
