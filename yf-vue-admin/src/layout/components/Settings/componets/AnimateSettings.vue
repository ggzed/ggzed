<template>
  <div class="settings-options">
    <el-tooltip content="更多样式参考<a href='https://animate.style/' target='_blank'>https://animate.style/</a>" raw-content placement="top">
      <span>动画样式:</span>
    </el-tooltip>
    <el-select
        v-model="systemStore.settings.animateCss"
        placeholder="选择动画"
        style="width: 80px;padding-bottom: 10px"
    >
      <el-option
          v-for="item in animateOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
      />
    </el-select>
  </div>

  <div class="animate-case">
    <transition :enter-active-class="`animate__animated ${systemStore.settings.animateCss}`">
      <div class="animate-case__item" :key="systemStore.settings.animateCss"></div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import {useSystemStore} from "@/store/modules/system";
// 数据
const systemStore = useSystemStore();

const animateOptions = ref<{
  label: string,
  value: string
}[]>([
  {label: 'fadeIn', value: 'animate__fadeIn'},
  {label: 'fadeInRight', value: 'animate__fadeInRight'},
  {label: 'fadeInUp', value: 'animate__fadeInUp'},
  {label: 'fadeInTopRight', value: 'animate__fadeInTopRight'},
  {label: 'fadeInBottomRight', value: 'animate__fadeInBottomRight'},
  {label: 'flipInX', value: 'flipInX'},
  {label: 'flipInY', value: 'animate__flipInY'},
  {label: 'lightSpeedInRight', value: 'animate__lightSpeedInRight'},
  {label: 'rotateInDownRight', value: 'animate__rotateInDownRight'},
  {label: 'jackInTheBox', value: 'animate__jackInTheBox'},
  {label: 'hinge', value: 'animate__hinge'},
  {label: 'rollIn', value: 'animate__rollIn'},
  {label: 'zoomIn', value: 'animate__zoomIn'},
  {label: 'zoomInRight', value: 'animate__zoomInRight'},
  {label: 'slideInRight', value: 'animate__slideInRight'},
  {label: 'slideInUp', value: 'animate__slideInUp'}
])
// 方法

// 生命周期
</script>

<style scoped lang="scss">
/* 样式 */
.animate-case {
  display: flex;
  justify-content: center;
  width: 100%;
  overflow: hidden;

  &__item {
    width: 100px;
    height: 50px;
    background-color: var(--el-color-primary-light-7);
  }
}

.settings-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
}
</style>
