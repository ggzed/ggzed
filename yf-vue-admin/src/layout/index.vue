<template>
  <div :class="{
    'fixed-header': !systemStore.settings.fixedHeader,
    'has-tags-view' : systemStore.settings.tagsView
  }" class="layout-container">
    <!-- 系统设置组件 -->
    <Settings></Settings>
    <!-- 侧边栏组件 -->
    <div v-if="systemStore.settings.layout == LayoutEnum.LEFT"
         class="layout-container-aside">
      <Sidebar ref="sidebarRef"></Sidebar>
    </div>
    <!-- 内容区域 -->
    <div class="layout-container-header">
      <!-- 顶部导航栏组件 -->
      <Navbar ref="navbarRef"></Navbar>
      <transition :enter-active-class="`animate__animated ${systemStore.settings.animateCss}`">
        <div v-if="systemStore.settings.tagsView">
          <TagsView v-if="systemStore.settings.tagsView" ref="tagsViewRef"></TagsView>
        </div>
      </transition>
    </div>

    <!-- 内容区域 -->
    <div class="layout-container-main">
      <!-- 页面主体区域 -->
      <AppMain ref="appMainRef"></AppMain>
    </div>
    <!-- 回到顶部组件 -->
    <el-backtop :bottom="50" :right="50"/>
    <!--  漫游导航  -->
    <Tour
        :key="systemStore.app.tourStatus.toString()"
        v-model="systemStore.app.tourStatus"
        :navbar-el="navbarRef?.$el"
        :sidebar-el="sidebarRef?.$el"
        :tags-view-el="tagsViewRef?.$el"
    ></Tour>
  </div>
</template>

<script lang="ts" setup>
import {useSystemStore} from "@/store/modules/system";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {SidebarStatusEnum} from "@/enums/SidebarStatusEnum";
import variables from "@/styles/variables.module.scss";
import {LayoutEnum} from "@/enums/LayoutEnum";

const systemStore = useSystemStore();

// ========== 漫游导航 ==========
const tagsViewRef = ref()
const navbarRef = ref()
const sidebarRef = ref()
const appMainRef = ref()
// ========== 漫游导航 ==========


const pcAsideWidth = computed(() => {
  // 如果是移动端不显示 本侧边栏
  if (systemStore.app.device === DeviceEnum.MOBILE || systemStore.settings.layout === LayoutEnum.TOP) {
    return "0px"
  }
  // 如果是PC端和平板显示对应宽度
  if (systemStore.app.sidebarStatus == SidebarStatusEnum.OPENED) {
    return variables['sidebar-width']
  } else {
    return variables['sidebar-width-collapsed']
  }
})
// 监听屏幕宽度
watchEffect(() => {
  systemStore.setDeviceType(useWindowSize().width.value)
});
</script>

<style lang="scss" scoped>
/* 导航栏阴影 */
$header__shadow: 0 2px 2px rgba(0, 0, 0, 0.1);
/* 侧边栏阴影 */
$aside__shadow: inset -2px 0 2px 0 rgba(0, 0, 0, .05);
/* 动画时长 */
$transition__time: 0.25s;

.layout-container {
  height: 100%;
  width: 100%;

  &-aside {
    position: fixed;
    top: 0;
    left: 0;
    height: 100%;
    width: v-bind(pcAsideWidth);
    box-shadow: $aside__shadow;
    transition: width $transition__time;

    :deep(.el-menu) {
      box-shadow: $aside__shadow;
    }
  }

  &-header {
    position: fixed;
    top: 0;
    left: v-bind(pcAsideWidth);
    width: calc(100% - v-bind(pcAsideWidth));
    height: $navbar-height;
    box-shadow: $header__shadow;
    transition: all $transition__time;
    background: var(--el-bg-color);
    z-index: 99;
  }

  &-main {
    margin-top: $navbar-height !important;
    margin-left: v-bind(pcAsideWidth);
    transition: all $transition__time;
  }
}


.has-tags-view {
  .layout-container-header {
    height: calc($navbar-height + $tags-view-height);
  }

  .layout-container-main {
    margin-top: calc($navbar-height + $tags-view-height) !important;
  }
}

.fixed-header {
  .layout-container-header {
    float: right;
    position: static !important;
  }

  .layout-container-main {
    margin-top: 0;
  }
}
</style>
