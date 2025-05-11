<template>
  <div :style="{width: props.width}" class="icon-selector">
    <el-popover
        :visible="popoverVisible"
        trigger="click"
        width="300px"
    >
      <!--   reference   -->
      <template #reference>
        <el-input ref="iconSelectRef" v-model="modelValue" :disabled="disabled" placeholder="请选择您的图标" readonly
                  @click="showPopover()">
          <template #prefix>
            <SystemIcon :icon="modelValue"/>
          </template>
        </el-input>
      </template>
      <!--   内容展示   -->
      <div ref="popoverContentRef" class="icon-selector-content">
        <el-tabs v-model="tabActiveName" type="card">
          <el-tab-pane label="系统图标" name="Svg">
            <el-scrollbar height="200px">
              <div class="icon-selector-content__icons">
                <SystemIcon v-for="icon in systemIcons" :icon="icon" @click="selectIcon(icon)"/>
              </div>
            </el-scrollbar>
          </el-tab-pane>
          <el-tab-pane label="ElementPlus图标" name="ElementPlus">
            <el-scrollbar height="200px">
              <div class="icon-selector-content__icons">
                <SystemIcon v-for="icon in elementIcons" :icon="'el-icon-' + icon"
                            @click="selectIcon('el-icon-' + icon)"/>
              </div>
            </el-scrollbar>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-popover>
  </div>
</template>

<script lang="ts" setup>
import * as ElementPlusIcons from "@element-plus/icons-vue";

defineOptions({
  name: "IconSelector",
  inheritAttrs: false,
});
// 数据
const props = withDefaults(defineProps<{
  modelValue?: string;
  width?: string;
  disabled?: boolean;
}>(), {
  width: "100%",
  disabled: false
});

const emit = defineEmits<{
  (event: 'update:modelValue', value: string): void;
}>();

const modelValue = useVModel(props, 'modelValue', emit)

const iconSelectRef = ref();
const popoverContentRef = ref();

const popoverVisible = ref(false);      //  弹出框显示状态
const systemIcons = ref<string[]>([]);  //  系统SVG图标集合
const elementIcons = ref<string[]>([]); //  elementplus图标集合
const tabActiveName = ref('Svg');       //  当前激活的tab页

// 方法
/**
 * 选择图标
 * @param icon
 */
function selectIcon(icon: string) {
  modelValue.value = icon;
  popoverVisible.value = false;
}

function showPopover() {
  popoverVisible.value = true;
}

function loadIcons() {
  // 加载系统 svg 图标
  const svgIcons = import.meta.glob("../../assets/icons/*.svg");
  for (const path in svgIcons) {
    const iconName = path.replace(/.*\/(.*)\.svg$/, "$1");
    systemIcons.value.push(iconName);
  }
  // 加载 element-plus 图标
  elementIcons.value = Object.keys(ElementPlusIcons);
}

/**
 * 点击容器外的区域关闭弹窗 VueUse onClickOutside
 */
onClickOutside(iconSelectRef, () => (popoverVisible.value = false), {
  ignore: [popoverContentRef],
});

// 生命周期
onMounted(() => {
  loadIcons();
});


</script>

<style lang="scss" scoped>
/* 样式 */
.icon-selector-content {
  &__icons {
    display: flex;
    justify-content: left;
    align-items: center;
    gap: 16px;
    flex-wrap: wrap;
  }
}
</style>
