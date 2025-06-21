<template>
  <el-config-provider :locale="zhCn" :size="size">
    <div class="app-container">
<!--      &lt;!&ndash; 开启水印 z-index > el-affix(z-index = 100) 即可  :locale="'zh-cn'" &ndash;&gt;-->
<!--      <el-watermark-->
<!--          v-if="watermarkEnabled"-->
<!--          :content="defaultSettings.title"-->
<!--          :font="{ color: fontColor }"-->
<!--          :z-index="101"-->
<!--      >-->
<!--        <router-view/>-->
<!--      </el-watermark>-->
<!--      &lt;!&ndash; 关闭水印 &ndash;&gt;-->
<!--      <router-view v-else/>-->
      <router-view/>
    </div>
  </el-config-provider>
</template>

<script lang="ts" setup>
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import {ThemeEnum} from "@/enums/ThemeEnum";
import {SizeEnum} from "@/enums/SizeEnum";
import {useSystemStore} from "@/store/modules/system";
import defaultSettings from "@/settings";
import {ElementPlusModifyEnum} from "@/enums/ElementPlusModifyEnum";

const systemStore = useSystemStore();
const fontStyle = computed(() => systemStore.fontStyle);
const size = computed(() => systemStore.app.size as SizeEnum);
const watermarkEnabled = computed(() => systemStore.settings.watermarkEnabled);
// 明亮/暗黑主题水印字体颜色适配
const fontColor = computed(() => {
  return systemStore.settings.theme === ThemeEnum.DARK
      ? "rgba(255, 255, 255, .15)"
      : "rgba(0, 0, 0, .15)";
});
// 初始化主题
onMounted(() => {
  // 初始化暗黑模式
  if (systemStore.settings.theme === ThemeEnum.DARK) {
    document.documentElement.classList.add('dark')
  }
  // 初始化主题色
  Object.keys(defaultSettings.settings.themeColor).forEach((key) => {
    const enumKey = key as ElementPlusModifyEnum; // 类型断言将键转换为 ElementPlusModifyEnum 类型
    if (defaultSettings.settings.themeColor[enumKey] !== systemStore.settings.themeColor[enumKey]) {
      systemStore.setThemeColor(enumKey, systemStore.settings.themeColor[enumKey])
    }
  })

  // 输出欢迎信息
  console.log(
      '%c🍎 欢迎使用 yf-admin 后台管理系统 🍎' +
      '\n\n有任何问题或建议，请随时联系我:' +
      '\n\n微信号 (添加后邀请进群) : LHyifei ' +
      '\n\nQQ群：957226269' +
      '\n\n邮箱：3634398311@qq.com',
      'font-size: 12px; font-weight: bold; color: #4facfe;'
  );
})
</script>

<style lang="scss" scoped>
//--el-font-size-extra-large: 20px;
//--el-font-size-large: 18px;
//--el-font-size-medium: 16px;
//--el-font-size-base: 14px;
//--el-font-size-small: 13px;
//--el-font-size-extra-small: 12px;
// 使用 el-Scrollbar 组件时 , 会有 bug
.app-container {
  font-size: v-bind(fontStyle);
}
</style>
