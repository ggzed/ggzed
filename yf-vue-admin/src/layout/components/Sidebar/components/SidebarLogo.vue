<template>
  <transition enter-active-class="animate__animated animate__fadeInLeft"
              leave-active-class="animate__animated animate__fadeOutLeft"
  >
    <div class="logo-container" v-if="systemStore.settings.sidebarLogo">
      <router-link to="/">
        <svg-icon icon-class="vite" size="25px"></svg-icon>
        <span class="logo-container__title"
              v-if="systemStore.app.sidebarStatus === SidebarStatusEnum.OPENED"> {{ defaultSettings.title }}</span>
      </router-link>
    </div>
  </transition>
</template>

<script setup lang="ts">
import {useSystemStore} from "@/store/modules/system";
import {SidebarStatusEnum} from "@/enums/SidebarStatusEnum";
import defaultSettings from "@/settings";
// 数据
const systemStore = useSystemStore();
// 方法
// 生命周期
</script>

<style scoped lang="scss">
/* 样式 */
.logo-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: $navbar-height;
  font-style: var(--el-font-size-extra-large);
  font-weight: bold;

  .svg-icon {
    vertical-align: middle;
  }

  &__title {
    padding-left: 10px;
  }
}
</style>
