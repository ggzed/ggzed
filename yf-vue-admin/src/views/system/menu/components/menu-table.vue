<template>
  <el-card class="table-container">
    <!--   头部   -->
    <template #header>
      <div>
        <el-button v-permission="[MenuAPI.SAVE.permission]"
                   :circle="device === DeviceEnum.MOBILE"
                   plain
                   type="success"
                   @click="openMenuDialog(0)">
          <el-icon>
            <plus/>
          </el-icon>
          <span v-show="device !== DeviceEnum.MOBILE"> 新增 </span>
        </el-button>
      </div>
    </template>
    <!--   表格    -->
    <el-table
        v-loading="props.loading"
        :data="props.dataList"
        border
        row-key="id"
    >
      <el-table-column label="菜单名称" min-width="200">
        <template #default="scope">
          <svg-icon :icon-class="scope.row.icon"/>
          {{ scope.row.name }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="类型" min-width="80">
        <template #default="scope">
          <el-tag :type="MENU_STYLE[scope.row.type % 4]">{{ menuDict[scope.row.type] }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="路由路径" min-width="160">
        <template #default="scope">
          {{ scope.row.path }}
        </template>
      </el-table-column>

      <el-table-column label="组件路径" min-width="180">
        <template #default="scope">
          {{ scope.row.component }}
        </template>
      </el-table-column>

      <el-table-column label="权限标识" min-width="160">
        <template #default="scope">
          {{ scope.row.permission }}
        </template>
      </el-table-column>

      <el-table-column v-permission="[MenuAPI.UPDATE_HIDDEN.permission]" align="center" label="是否隐藏"
                       min-width="80">
        <template #default="scope">
          <el-tag v-show="scope.row.hidden !== null"
                  :type="HiddenStatusEnum.TAG_STYLE[scope.row.hidden % 2]"
                  @click="updateDataStatus(scope.row.id,scope.row.name,!scope.row.hidden,props.loadData)">
            {{ HiddenStatusEnum.OPTIONS[scope.row.hidden] }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" label="排序" min-width="60">
        <template #default="scope">
          {{ scope.row.sort }}
        </template>
      </el-table-column>
      <el-table-column v-permission="[MenuAPI.UPDATE.permission, MenuAPI.DELETE.permission]"
                       :fixed="device === DeviceEnum.MOBILE ? false : 'right'"
                       align="center" label="操作" width="220">
        <template #default="scope">
          <el-button
              v-if="scope.row.type !== MenuTypeEnum.BUTTON && scope.row.type !== MenuTypeEnum.EXT_LINK"
              v-permission="[MenuAPI.SAVE.permission]"
              link
              size="small"
              type="primary"
              @click.stop="openMenuDialog(scope.row.id)"
          >
            <el-icon>
              <edit/>
            </el-icon>
            新增子菜单
          </el-button>
          <el-button
              v-permission="[MenuAPI.UPDATE.permission]"
              link
              size="small"
              type="warning"
              @click.stop="openMenuDialog(undefined, scope.row.id)"
          >
            <el-icon>
              <edit/>
            </el-icon>
            编辑
          </el-button>
          <el-button
              v-permission="[MenuAPI.DELETE.permission]"
              link
              size="small"
              type="danger"
              @click.stop="deleteData([scope.row.id],scope.row.name,props.loadData)"
          >
            <el-icon>
              <delete/>
            </el-icon>
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <!-- 新增 & 修改数据 -->
  <menu-manage-dialog
      v-if="visible"
      v-model:visible="visible"
      :close-dialog="closeDialog"
      :current-click-row-id="currentClickRowId"
      :device="device"
      :load-data="loadData"
      :menu-dict="props.menuDict"
      :parent-id="clickParentId"
      :title="title"
  />
</template>

<script lang="ts" setup>
// 组件定义
import {MenuAPI} from "@/api/system/menu";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {MENU_STYLE, MenuForm, MenuPageVO} from "@/api/system/menu/type";
import {HiddenStatusEnum} from "@/constants/system";
import {MenuTypeEnum} from "@/enums/MenuTypeEnum";
import {useSystemStore} from "@/store/modules/system";
import {useDialogManage} from "@/hooks/useDialogManage";
import {useCrudActions} from "@/hooks/useCrudActions";

defineOptions({
  name: "MenuTable",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  dataList: MenuPageVO[];
  loading: boolean;
  loadData: (callback?: () => void) => Promise<void>;
  // 菜单类型
  menuDict: Record<number | string, string>;
}>(), {});
// hooks
const {
  visible,
  title,
  openDialog,
  closeDialog
} = useDialogManage();
const {
  deleteData,
  updateDataStatus
} = useCrudActions<number, MenuForm>(MenuAPI.SAVE.request, MenuAPI.UPDATE.request, MenuAPI.DELETE.request, MenuAPI.UPDATE_HIDDEN.request);
// 数据
const clickParentId = ref<number | undefined>();              // 打开 dialog 点击的 row
const currentClickRowId = ref<number | undefined>();          // 打开 dialog 点击的 row
const device = computed(() => useSystemStore().app.device)    // 设备类型
// 方法
function openMenuDialog(parentId ?: number, menuId?: number) {
  clickParentId.value = parentId;
  currentClickRowId.value = menuId;
  if (menuId) {
    openDialog("编辑菜单")
  } else {
    openDialog("新增菜单");
  }
}

// 生命周期
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
