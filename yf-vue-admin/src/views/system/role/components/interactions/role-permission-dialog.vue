<template>
  <el-dialog
      v-model="visible"
      :title="props.title"
      :width="props.device === DeviceEnum.MOBILE ? '90%' : '50%'"
      draggable
      overflow
  >
    <el-scrollbar max-height="400px">
      <el-tree

          v-loading="loading"
          :data="menuList"
          :render-after-expand="false"
          check-on-click-node
          check-strictly
          default-expand-all
          node-key="value"
          show-checkbox
      >
        <template #default="{ data }">
          {{ data.label }}
        </template>
      </el-tree>
    </el-scrollbar>

    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="onSubmit">
          确 定
        </el-button>
        <el-button @click="props.closeDialog()">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import {DeviceEnum} from "@/enums/DeviceEnum";
import {MenuAPI} from "@/api/system/menu";
import {useDataLoader} from "@/hooks/useDataLoader";
import {RoleAPI} from "@/api/system/role";
// 组件定义
defineOptions({
  name: "RolePermissionDialog",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  // dialog-visible
  visible: boolean;
  // dialog-title
  title: string;
  // 设备
  device?: DeviceEnum;
  // 当前选中角色ID
  currentClickRoleId: number;
  // 关闭弹窗
  closeDialog: (callback?: () => void) => void;
}>(), {device: DeviceEnum.DESKTOP});
const emits = defineEmits<{
  (event: "update:visible", visible: boolean): void
}>()
// hooks
const visible = useVModel(props, 'visible', emits)
// 数据
const menuRef = ref<InstanceType<typeof ElTree>>()
const {
  query,
  dataList: menuList,
  total,
  loading,
  loadData
} = useDataLoader<OptionType, object>(MenuAPI.OPTIONS.request, {});

// 方法
function onSubmit() {
  // 2. 获取选中节点id
  const checkedMenuIds: number[] = menuRef.value?.getCheckedNodes(false, true)
      .map((node: OptionType) => node.value);
  // 3. 给选中角色分配菜单权限
  RoleAPI.UPDATE_MENUS.request(props.currentClickRoleId, checkedMenuIds).then(() => {
    ElMessage.success("分配权限成功");
    props.closeDialog();
  })
}

// 生命周期
onMounted(async () => {
  try {
    loading.value = true;
    // 1. 获取可选菜单
    await MenuAPI.OPTIONS.request().then(({data}) => {
      menuList.value = data;
    });
    // 2. 获取当前选中的角色菜单
    await RoleAPI.MENU_IDS.request(props.currentClickRoleId).then(({data}) => {
      data.forEach(menuId => {
        menuRef.value?.setChecked(menuId, true, false);
      })
    });
  } finally {
    loading.value = false;
  }
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
