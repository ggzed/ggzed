<template>
  <el-menu
      :default-active="currentRoute.path"
      :collapse="systemStore.app.sidebarStatus === SidebarStatusEnum.CLOSED"
      :unique-opened="false"
      :collapse-transition="false"
      :mode="systemStore.settings.layout === LayoutEnum.TOP ? 'horizontal' : 'vertical'"
  >
    <SidebarMenuItem
        v-for="item in userStore.routes"
        :key="item.path"
        :base-path="item.path"
        :menu-item="item"
    ></SidebarMenuItem>
  </el-menu>
</template>

<script setup lang="ts">
// 数据
import {useSystemStore} from "@/store/modules/system";
import {LayoutEnum} from "@/enums/LayoutEnum";
import {SidebarStatusEnum} from "@/enums/SidebarStatusEnum";
import {useUserStore} from "@/store/modules/user";

const systemStore = useSystemStore();
const userStore = useUserStore();
const currentRoute = useRoute();
// 方法
// 生命周期
</script>

<style scoped lang="scss">
/* 样式 */
</style>
