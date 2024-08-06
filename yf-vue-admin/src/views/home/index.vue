<template>
  <!--  页面  -->
  <div class="home-container">
    <div>
      <h3>漫游导航</h3>
      <el-button type="primary" @click="systemStore.setTourStatus(true)">漫游导航开启</el-button>
    </div>

    <div>
      <h3>设置</h3>
      <el-button type="primary" @click="systemStore.settings.settingsVisible = true">打开设置</el-button>
      <el-button type="success" @click="systemStore.settings.settingsVisible = true">打开设置</el-button>
      <el-button type="warning" @click="systemStore.settings.settingsVisible = true">打开设置</el-button>
      <el-button type="danger" @click="systemStore.settings.settingsVisible = true">打开设置</el-button>
      <el-button type="info" @click="systemStore.settings.settingsVisible = true">打开设置</el-button>
    </div>

    <div>
      <h3>组件缓存测试</h3>
      <div>count 值 : {{ count }}</div>
      <br>
      <el-button type="primary" @click="count += 1">count + 1</el-button>
    </div>
  </div>
</template>

<script lang="ts" setup>
// 数据
import {useSystemStore} from "@/store/modules/system";
import {Ref} from "vue";

defineOptions({
  name: "Home",
  inheritAttrs: false,
});
const count: Ref<number> = ref<number>(0);
const systemStore = useSystemStore();
</script>

<style lang="scss" scoped>
/* 样式 */
.home-container {
  display: flex;
  flex-direction: column; /* 设置基准线为上下方向 */
  justify-content: center;
}
</style>
