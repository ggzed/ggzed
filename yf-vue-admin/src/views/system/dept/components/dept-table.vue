<template>
  <el-card class="table-container">
    <!--   头部   -->
    <template #header>
      <el-button v-permission="[DeptAPI.SAVE.permission]"
                 :circle="device === DeviceEnum.MOBILE"
                 plain
                 type="success"
                 @click="openDeptDialog(0)">
        <el-icon>
          <plus/>
        </el-icon>
        <span v-show="device !== DeviceEnum.MOBILE"> 新增 </span>
      </el-button>
    </template>
    <!--   表格    -->
    <el-table
        v-loading="props.loading"
        :data="props.dataList"
        border
        row-key="id"
    >
      <el-table-column label="部门名称" min-width="200">
        <template #default="scope">
          <svg-icon :icon-class="scope.row.icon"/>
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column v-permission="[DeptAPI.UPDATE_STATUS.permission]" label="状态" min-width="80">
        <template #default="scope">
          <el-tag v-show="scope.row.status !== null"
                  :type="EnableStatusEnum.TAG_STYLE[scope.row.status % 2]"
                  @click="updateDataStatus(scope.row.id,scope.row.name,!scope.row.status,props.loadData)">
            {{ EnableStatusEnum.OPTIONS[scope.row.status] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="排序" min-width="60">
        <template #default="scope">
          {{ scope.row.sort }}
        </template>
      </el-table-column>
      <el-table-column v-permission="[DeptAPI.UPDATE.permission, DeptAPI.DELETE.permission]"
                       :fixed="device === DeviceEnum.MOBILE ? false : 'right'"
                       align="center" label="操作" width="220">
        <template #default="scope">
          <el-button
              v-permission="[DeptAPI.SAVE.permission]"
              link
              size="small"
              type="primary"
              @click.stop="openDeptDialog(scope.row.id)"
          >
            <el-icon>
              <edit/>
            </el-icon>
            新增子部门
          </el-button>
          <el-button
              v-permission="[DeptAPI.UPDATE.permission]"
              link
              size="small"
              type="warning"
              @click.stop="openDeptDialog(undefined, scope.row.id)"
          >
            <el-icon>
              <edit/>
            </el-icon>
            编辑
          </el-button>
          <el-button
              v-permission="[DeptAPI.DELETE.permission]"
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
  <dept-manage-dialog
      v-if="visible"
      v-model:visible="visible"
      :close-dialog="closeDialog"
      :current-click-row-id="currentClickRowId"
      :device="device"
      :load-data="props.loadData"
      :parent-id="clickParentId"
      :title="title"
  />
</template>

<script lang="ts" setup>
import {DeptAPI} from "@/api/system/dept";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {EnableStatusEnum} from "@/constants/system";
import {DeptForm, DeptPageVO} from "@/api/system/dept/type";
import {useCrudActions} from "@/hooks/useCrudActions";
import {useSystemStore} from "@/store/modules/system";
import {useDialogManage} from "@/hooks/useDialogManage";
// 组件定义
defineOptions({
  name: "DeptTable",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  dataList: DeptPageVO[];
  loading: boolean;
  loadData: (callback?: () => void) => Promise<void>;   // 加载数据函数
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
} = useCrudActions<number, DeptForm>(DeptAPI.SAVE.request, DeptAPI.UPDATE.request, DeptAPI.DELETE.request, DeptAPI.UPDATE_STATUS.request);
// 数据
const clickParentId = ref<number | undefined>();              // 打开 dialog 点击的 row
const currentClickRowId = ref<number | undefined>();          // 打开 dialog 点击的 row
const device = computed(() => useSystemStore().app.device)    // 设备类型
// 方法
function openDeptDialog(parentId ?: number, deptId?: number) {
  clickParentId.value = parentId;
  currentClickRowId.value = deptId;
  if (deptId) {
    openDialog("编辑部门")
  } else {
    openDialog("新增部门");
  }
}

// 生命周期
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
