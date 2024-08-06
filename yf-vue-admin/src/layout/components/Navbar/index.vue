<template>
  <div class="navbar-container">
    <div class="navbar-container-left"
         :class="{'navbar-container-mobile-left':systemStore.app.device === DeviceEnum.MOBILE && systemStore.settings.layout === LayoutEnum.LEFT}">
      <SidebarLogo v-if="systemStore.settings.layout === LayoutEnum.TOP"></SidebarLogo>
      <template v-else>
        <div class="navbar-container-left__left">
          <svg-icon icon-class="element-menu" size="18px" @click="handleMenuBarClick"></svg-icon>
          <Breadcrumb v-if="systemStore.settings.breadCrumb"></Breadcrumb>
        </div>
      </template>
    </div>
    <div class="navbar-container-center" v-if="systemStore.settings.layout === LayoutEnum.TOP">
      <Sidebar></Sidebar>
    </div>
    <div class="navbar-container-right">
      <NavbarRight></NavbarRight>
    </div>
    <!-- 移动端菜单栏 -->
    <!-- @close="systemStore.setSidebarStatus(SidebarStatusEnum.CLOSED)" 防止打开顶部菜单栏   -->
    <el-drawer
        append-to-body
        modal-class="layout-container-mobile-sidebar"
        v-model="mobileMenuVisible"
        :with-header="false"
        :size="variables['sidebar-width']"
        :direction="'ltr'"
        @close="systemStore.setSidebarStatus(SidebarStatusEnum.CLOSED)"
    >
      <Sidebar></Sidebar>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import {useSystemStore} from "@/store/modules/system";
import variables from "@/styles/variables.module.scss";
import {LayoutEnum} from "@/enums/LayoutEnum";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {SidebarStatusEnum} from "@/enums/SidebarStatusEnum";
// 数据
const systemStore = useSystemStore();

const mobileMenuVisible = ref<boolean>(false)

// 方法
function handleMenuBarClick() {
  if (systemStore.app.device === DeviceEnum.MOBILE) {
    systemStore.setSidebarStatus(SidebarStatusEnum.OPENED)
    mobileMenuVisible.value = true;
  } else {
    // 如果不是移动端，则切换侧边栏状态
    systemStore.setSidebarStatus(systemStore.app.sidebarStatus === SidebarStatusEnum.OPENED ?
        SidebarStatusEnum.CLOSED : SidebarStatusEnum.OPENED)
  }
}

// 监听移动端菜单可视状态
watch(useRoute(), () => {
  mobileMenuVisible.value = false;
})
// 生命周期
</script>

<style lang="scss">
.layout-container-mobile-sidebar {
  .el-drawer__body{
    padding: 0 !important;
  }
}
</style>

<style scoped lang="scss">
/* 样式 */

// 整体样式
.navbar-container {
  display: flex;
  align-items: center;
  height: $navbar-height;
  width: 100%;
  overflow-x: auto;
  overflow-y: hidden;
  white-space: nowrap;

  .svg-icon {
    cursor: pointer;
  }

  &-left {
    flex: 1;

    // 表示为 layout为 left 的时候
    &__left {
      line-height: 60px;
      display: flex;
      padding-left: 20px;

      .svg-icon {
        margin-right: 14px;
      }
    }
  }

  &-mobile-left {
    flex: 3;
  }

  &-center {
    flex: 2;
  }

  &-right {
    flex: 1;
  }
}
</style>
