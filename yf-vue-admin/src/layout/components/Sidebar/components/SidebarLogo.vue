<template>
  <transition enter-active-class="animate__animated animate__fadeInLeft"
              leave-active-class="animate__animated animate__fadeOutLeft"
  >
    <div v-if="systemStore.settings.sidebarLogo" class="logo-container">
      <router-link to="/">
        <svg-icon icon-class="logo" size="25px"></svg-icon>
        <span v-if="systemStore.app.sidebarStatus === SidebarStatusEnum.OPENED"
              class="logo-container__title"> {{ defaultSettings.title }}</span>
      </router-link>
    </div>
  </transition>
</template>

<script lang="ts" setup>
import {useSystemStore} from "@/store/modules/system";
import {SidebarStatusEnum} from "@/enums/SidebarStatusEnum";
import defaultSettings from "@/settings";
// 数据
const systemStore = useSystemStore();
// 方法
// 生命周期
</script>

<style lang="scss" scoped>
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
