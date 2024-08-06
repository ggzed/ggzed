<template>
  <div class="menu-settings">
    <div class="menu-settings__left"
         :class="{'menu-settings--active':systemStore.settings.layout === LayoutEnum.LEFT}"
         @click="systemStore.setLayOut(LayoutEnum.LEFT)">
      <div></div>
      <div></div>
    </div>
    <div class="menu-settings__top"
         :class="{'menu-settings--active':systemStore.settings.layout === LayoutEnum.TOP}"
         @click="systemStore.setLayOut(LayoutEnum.TOP)">
      <div></div>
      <div></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {useSystemStore} from "@/store/modules/system";
import {LayoutEnum} from "@/enums/LayoutEnum";
// 数据
const systemStore = useSystemStore();
// 方法

// 生命周期
</script>

<style scoped lang="scss">
/* 样式 */
// 菜单设置
.menu-settings {
  display: flex;
  justify-content: space-around;
  align-items: center;

  > div {
    display: flex;
    align-items: center;
    width: 74px;
    height: 45px;
    cursor: pointer;
    border-radius: 2.5px;
    overflow: hidden;
  }

  &--active {
    border: 2.5px solid var(--el-color-primary-light-3);
  }

  &__top {
    flex-direction: column;

    > div:first-child {
      height: 31%;
      width: 100%;
      background-color: var(--el-color-primary-light-7);
    }

    > div:last-child {
      height: 69%;
      width: 100%;
      background-color: var(--el-color-info-light-7);
    }
  }

  &__left {
    > div:first-child {
      height: 100%;
      width: 28%;
      background-color: var(--el-color-primary-light-7);
    }

    > div:last-child {
      height: 100%;
      width: 72%;
      background-color: var(--el-color-info-light-7);
    }
  }
}
</style>
