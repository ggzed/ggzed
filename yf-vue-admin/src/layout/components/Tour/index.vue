<template>
  <div class="tour-container">
    <!--   该组件只是为了简化 layout/index.vue 页面展示压力     -->
    <el-tour v-model="tempTourRef" @close="$emit('update:modelValue', false)">
      <el-tour-step
          placement="right"
          title="进行引导"
      >
        <div class="tour-container-item">
          让 我 来 引 导 您 使 用 系 统 吧 ! (*^▽^*)
        </div>
      </el-tour-step>
      <!--      侧边栏     -->
      <el-tour-step
          :target="props.sidebarEl"
          placement="right"
          title="侧边栏">
        <h4>解释: 该盒子为系统的侧边栏组件</h4>
        <div class="tour-container-item"> 1. 可以通过点击对应菜单项切换对应组件</div>
        <div class="tour-container-item"> 2. 我会自动适应屏幕大小 ( 以下为侧边栏模式的解释 )</div>
        <div class="tour-container-item">
          <b>电脑端</b> : 我会<b>展开</b>，您可以根据标题选择对应菜单,您可以通过点击导航栏的
          <svg-icon icon-class="element-menu"></svg-icon>
          按钮收缩我
        </div>
        <div class="tour-container-item">
          <b>平板端</b> : 我会<b>收起</b>，您可以根据图标选择对应菜单,您也可以按下导航栏的
          <svg-icon icon-class="element-menu"></svg-icon>
          按钮展开我
        </div>
        <div class="tour-container-item">
          <b>移动端</b> : 我会<b>隐藏</b>, 您可以点击导航栏的
          <svg-icon icon-class="element-menu"></svg-icon>
          按钮显示我
        </div>
        <div class="tour-container-item"> 3. 我会自动适应屏幕大小 ( 以下是顶部模式的解释 )
        </div>
        <div class="tour-container-item">
          <b>电脑端</b> : 我会<b>展开菜单</b>，以供您更好的选择
        </div>
        <div class="tour-container-item">
          <b>平板端、移动端</b> : 我会<b>收起菜单,展示图标</b>，以适应屏幕大小
        </div>
      </el-tour-step>
      <!--      顶部导航栏     -->
      <el-tour-step
          :target="props.navbarEl"
          title="顶部导航栏">
        <h4>解释: 该盒子为系统的头部导航栏组件</h4>
        <div class="tour-container-item">1. 我会<b>根据不同的布局方式,展示不同顶部内容</b></div>
        <div class="tour-container-item">2. 这是全屏展示页面的按钮 <svg-icon icon-class="fullscreen"></svg-icon></div>
        <div class="tour-container-item">3. 您可以在 <b>
          <svg-icon icon-class="element-menu"></svg-icon>
          按钮 </b> 打开的界面设置选择是否固定我
        </div>
        <div class="tour-container-item">4. 您可以通过 <b>
          <svg-icon icon-class="element-menu"></svg-icon>
          按钮</b> 对系统进行配置
        </div>
      </el-tour-step>
      <!--      页签     -->
      <el-tour-step
          :target="props.tagsViewEl"
          title="页签">
        <h4>解释: 该盒子为系统的页签组件</h4>
        <div class="tour-container-item">1. 您可以根据您喜欢的排序方式<b>拖拽页签</b>来更改页签位置</div>
        <div class="tour-container-item">2. 您可以通过<b>鼠标右键打开页签的操作菜单</b></div>
        <div class="tour-container-item">3. 您可以点击<b>鼠标中键</b>删除鼠标所指的页签</div>
        <div class="tour-container-item">4. 您可以通过
          <svg-icon icon-class="setting"></svg-icon>
          按钮对页签进行<b>进行设置</b>
        </div>
      </el-tour-step>
    </el-tour>
  </div>
</template>

<script setup lang="ts">
// 数据
import {useSystemStore} from "@/store/modules/system";

const systemStore = useSystemStore();
const props = withDefaults(defineProps<{
  modelValue: boolean,
  // 组件元素
  sidebarEl: HTMLElement,
  navbarEl: HTMLElement,
  tagsViewEl: HTMLElement,
}>(), {})
const tempTourRef = ref(props.modelValue)

let emit = defineEmits<{
  (event: "update:modelValue", modelValue: boolean): void
}>()
</script>

<style scoped lang="scss">
/* 样式 */
.tour-container {
  &-item {
    margin: 12px 0;
  }
}
</style>
