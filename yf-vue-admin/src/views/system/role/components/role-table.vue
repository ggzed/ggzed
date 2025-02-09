<template>
  <el-card class="table-container">
    <!--   操作框   -->
    <template #header>
      <div>
        <el-button v-permission="[MenuAPI.SAVE.permission]"
                   :circle="device === DeviceEnum.MOBILE"
                   plain
                   type="success"
                   @click="openRoleDialog()">
          <el-icon>
            <plus/>
          </el-icon>
          <span v-show="device !== DeviceEnum.MOBILE"> 新增 </span>
        </el-button>
        <el-button v-permission="[MenuAPI.UPDATE.permission]"
                   :circle="device === DeviceEnum.MOBILE"
                   :disabled="roleIds.length !== 1"
                   plain
                   type="warning"
                   @click="openRoleDialog(roleIds[0])">
          <el-icon>
            <edit/>
          </el-icon>
          <span v-show="device !== DeviceEnum.MOBILE"> 修改 </span>
        </el-button>
        <el-button v-permission="[MenuAPI.DELETE.permission]"
                   :circle="device === DeviceEnum.MOBILE"
                   :disabled="roleIds.length === 0"
                   plain
                   type="danger"
                   @click="deleteData(roleIds,null, props.loadData)">
          <el-icon>
            <delete/>
          </el-icon>
          <span v-show="device !== DeviceEnum.MOBILE"> 删除 </span>
        </el-button>
      </div>
    </template>
    <!--   表格    -->
    <el-table
        ref="dataTableRef"
        v-loading="props.loading"
        :data="props.dataList"
        border
        highlight-current-row
        row-key="id"
        @selection-change="handleSelectionChange"
        @cell-dblclick="handleCellDblclick"
    >
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column label="角色名称" min-width="100" prop="name"/>
      <el-table-column label="角色编码" min-width="180" prop="code"/>

      <el-table-column v-permission="[RoleAPI.UPDATE_STATUS.permission]" align="center" label="状态" width="100">
        <template #default="scope">
          <el-tag v-show="scope.row.status !== null"
                  :type="EnableStatusEnum.TAG_STYLE[scope.row.status % 2]"
                  @click="updateDataStatus([scope.row.id],scope.row.name,!scope.row.status,props.loadData)">
            {{ EnableStatusEnum.OPTIONS[scope.row.status] }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" label="排序" prop="sort" width="80"/>

      <el-table-column
          v-permission="[RoleAPI.UPDATE.permission, RoleAPI.DELETE.permission,RoleAPI.UPDATE_MENUS.permission]"
          :fixed="device === DeviceEnum.MOBILE ? false : 'right'" align="center" label="操作"
          width="220">
        <template #default="scope">
          <el-button
              v-permission="[RoleAPI.UPDATE_MENUS.permission]"
              link
              size="small"
              type="warning"
              @click="openRoleMenuDialog(scope.row.id,scope.row.name)"
          >
            <el-icon>
              <position/>
            </el-icon>
            分配权限
          </el-button>
          <el-button
              v-permission="[RoleAPI.UPDATE.permission]"
              link
              size="small"
              type="warning"
              @click="openRoleDialog(scope.row.id)"
          >
            <el-icon>
              <edit/>
            </el-icon>
            编辑
          </el-button>
          <el-button
              v-permission="[RoleAPI.DELETE.permission]"
              link
              size="small"
              type="danger"
              @click="deleteData([scope.row.id],scope.row.name,props.loadData)"
          >
            <el-icon>
              <delete/>
            </el-icon>
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--  分页  -->
    <template #footer>
      <el-scrollbar>
        <Pagination v-model:current-page="query.pageNum"
                    v-model:page-size="query.pageSize"
                    :total="props.total" @handle-page-change="props.loadData"/>
      </el-scrollbar>
    </template>
  </el-card>

  <!-- 角色权限分配模态框  -->
  <role-manage-dialog
      v-if="visible"
      v-model:visible="visible"
      :close-dialog="closeDialog"
      :current-click-row-id="currentClickRowId"
      :data-scope-dict="props.dataScopeDict"
      :device="device"
      :load-data="props.loadData"
      :title="title"
  />

  <!-- 角色新增 / 修改模态框  -->
  <role-permission-dialog
      v-if="menuPermissionVisible"
      v-model:visible="menuPermissionVisible"
      :close-dialog="closeMenuPermissionDialog"
      :currentClickRoleId="currentClickRoleId"
      :device="device"
      :title="menuPermissionTitle"
  />

</template>

<script lang="ts" setup>
import {MenuAPI} from "@/api/system/menu";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {RoleAPI} from "@/api/system/role";
import {EnableStatusEnum} from "@/constants/system";
import {Position} from "@element-plus/icons-vue";
import {RoleForm, RolePageQuery, RolePageVO} from "@/api/system/role/type";
import {useCrudActions} from "@/hooks/useCrudActions";
import {useDialogManage} from "@/hooks/useDialogManage";
import {useSystemStore} from "@/store/modules/system";
import {TableInstance} from "element-plus";
import {useTableManagement} from "@/hooks/useTableManagement";
// 组件定义
defineOptions({
  name: "RoleTable",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  query: RolePageQuery;
  dataList: RolePageVO[];
  dataScopeDict: Record<number | string, string>;
  total: number;
  loading: boolean;
  loadData: (callback?: () => void) => Promise<void>;   // 加载数据函数
}>(), {});
const emits = defineEmits<{
  (event: "update:query", query: RolePageQuery): void
}>()
// hooks
const query = useVModel(props, 'query', emits)
const dataTableRef = ref<TableInstance | null>(null);                 // 数据Table
const {selectedIds: roleIds, handleCellDblclick, handleSelectionChange} = useTableManagement<number>(dataTableRef);
// role manage dialog
const {
  visible,
  title,
  openDialog,
  closeDialog
} = useDialogManage();

// 分配权限 dialog
const {
  visible: menuPermissionVisible,
  title: menuPermissionTitle,
  openDialog: openMenuPermissionDialog,
  closeDialog: closeMenuPermissionDialog
} = useDialogManage();
const {
  deleteData,
  updateDataStatus
} = useCrudActions<RoleForm>(RoleAPI.SAVE.request, RoleAPI.UPDATE.request, RoleAPI.DELETE.request, RoleAPI.UPDATE_STATUS.request);
// 数据
const device = computed(() => useSystemStore().app.device)            // 设备类型
const currentClickRoleId = ref<number>(0)                     // 当前选中的角色id
const currentClickRowId = ref<number | undefined>();          // 打开 dialog 点击的 row

// 方法
/**
 * 打开角色新增/修改弹窗
 * @param roleId
 */
function openRoleDialog(roleId?: number) {
  currentClickRowId.value = roleId;
  // 根据 role 判断新增或者修改
  if (roleId) {
    openDialog("修改角色")
  } else {
    openDialog("新增角色");
  }
}

/**
 * 打开角色分配菜单权限
 * @param roleId 角色Id
 * @param roleName 角色名
 */
async function openRoleMenuDialog(roleId: number, roleName: string) {
  // 1. 获取当前选中的角色ID
  currentClickRoleId.value = roleId;
  openMenuPermissionDialog(`您正在分配 【 ${roleName} 】  角色的权限`)
}
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
