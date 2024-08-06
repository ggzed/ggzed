<template>
  <!--  暗黑模式  -->
  <div class="settings-options">
    <span>暗黑模式:</span>
    <div class="theme-settings">
      <el-switch
          v-model="isDark"
          :active-action-icon="Sunny"
          :inactive-action-icon="Moon"
          @change="changeTheme"
      />
    </div>
  </div>
  <!-- 字体设置  -->
  <div class="settings-options">
    <span>字体大小:</span>
    <el-select
        v-model="systemStore.app.size"
        placeholder="选择字体大小"
        style="width: 80px;padding-bottom: 10px"
    >
      <el-option
          v-for="item in sizeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
      />
    </el-select>
  </div>
  <!-- 主题颜色 -->
  <div class="settings-options">
    <span>主题颜色:info</span>
    <el-color-picker v-model="systemStore.settings.themeColor.info"
                     @change="changeThemeColor(ElementPlusModifyEnum.INFO,systemStore.settings.themeColor.info)"
                     :predefine="predefineColors"/>
  </div>
  <div class="settings-options">
    <span>主题颜色:primary</span>
    <el-color-picker v-model="systemStore.settings.themeColor.primary"
                     @change="changeThemeColor(ElementPlusModifyEnum.PRIMARY,systemStore.settings.themeColor.primary)"
                     :predefine="predefineColors"/>
  </div>
  <div class="settings-options">
    <span>主题颜色:success</span>
    <el-color-picker v-model="systemStore.settings.themeColor.success"
                     @change="changeThemeColor(ElementPlusModifyEnum.SUCCESS,systemStore.settings.themeColor.success)"
                     :predefine="predefineColors"/>
  </div>
  <div class="settings-options">
    <span>主题颜色:warning</span>
    <el-color-picker v-model="systemStore.settings.themeColor.warning"
                     @change="changeThemeColor(ElementPlusModifyEnum.WARNING,systemStore.settings.themeColor.warning)"
                     :predefine="predefineColors"/>
  </div>
  <div class="settings-options">
    <span>主题颜色:danger</span>
    <el-color-picker v-model="systemStore.settings.themeColor.danger"
                     @change="changeThemeColor(ElementPlusModifyEnum.DANGER,systemStore.settings.themeColor.danger)"
                     :predefine="predefineColors"/>
  </div>
</template>

<script setup lang="ts">
import {Moon, Sunny} from "@element-plus/icons-vue";
import {ElementPlusModifyEnum} from "@/enums/ElementPlusModifyEnum";
import {useSystemStore} from "@/store/modules/system";
import {ThemeEnum} from "@/enums/ThemeEnum";
import {SizeEnum} from "@/enums/SizeEnum";

// 数据
const systemStore = useSystemStore();
const sizeOptions = ref<{
  label: string
  value: SizeEnum
}[]>([
  {label: '默认', value: SizeEnum.DEFAULT},
  {label: '大型', value: SizeEnum.LARGE},
  {label: '小型', value: SizeEnum.SMALL}
])
const isDark = ref<boolean>(systemStore.settings.theme === ThemeEnum.LIGHT);
const predefineColors = ref<string[]>([
  // 默认
  '#409EFF',
  '#67C23A',
  '#E6A23C',
  '#F56C6C',
  '#909399',
])
// 方法
/**
 * 切换主题
 */
function changeTheme() {
  systemStore.setTheme(isDark.value ? ThemeEnum.LIGHT : ThemeEnum.DARK);
}
/**
 * 修改主题颜色
 */
function changeThemeColor(type: ElementPlusModifyEnum, color: string | null) {
  if (color && color !== '') {
    systemStore.setThemeColor(type, color)
  }
}
// 生命周期

</script>

<style scoped lang="scss">
/* 样式 */
// 设置主题样式
.theme-settings {
  display: flex;
  justify-content: center;
  align-items: center;
}

.settings-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  margin: 6px 0;
}

:deep(.el-select){
  padding-bottom: 0 !important;
}

</style>
