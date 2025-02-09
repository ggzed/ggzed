<template>
  <!--  主体 ( 左侧操作框 / 右侧操作框 )  -->
  <el-card class="table-container">
    <template #header>
      <div>
        <el-button v-permission="[UserAPI.SAVE.permission]"
                   :circle="device === DeviceEnum.MOBILE"
                   plain
                   type="success"
                   @click="openUserDialog()">
          <el-icon>
            <plus/>
          </el-icon>
          <span v-show="device !== DeviceEnum.MOBILE"> 新增 </span>
        </el-button>
        <el-button v-permission="[UserAPI.UPDATE.permission]"
                   :circle="device === DeviceEnum.MOBILE"
                   :disabled="userIds.length !== 1"
                   plain
                   type="warning"
                   @click="openUserDialog(userIds[0])">
          <el-icon>
            <edit/>
          </el-icon>
          <span v-show="device !== DeviceEnum.MOBILE"> 修改 </span>
        </el-button>
        <el-button v-permission="[UserAPI.DELETE.permission]"
                   :circle="device === DeviceEnum.MOBILE"
                   :disabled="userIds.length === 0"
                   plain
                   type="danger"
                   @click="deleteData(userIds,undefined,props.loadData)">
          <el-icon>
            <delete/>
          </el-icon>
          <span v-show="device !== DeviceEnum.MOBILE"> 删除 </span>
        </el-button>
      </div>
      <div>
        <!-- TODO Excel 文件上传/下载 模板下载 -->
      </div>
    </template>

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
      <el-table-column align="center" type="selection" width="50"/>
      <el-table-column align="center" label="用户名" prop="username"/>
      <el-table-column align="center" label="用户昵称" prop="nickname" width="200"/>
      <el-table-column align="center" label="用户头像" prop="avatar" width="120">
        <template #default="scope">
          <el-image :preview-src-list="[scope.row.avatar]"
                    :src="scope.row.avatar"
                    fit="cover"
                    lazy
                    preview-teleported
                    style="width: 48px; height: 48px"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="性别" prop="gender" width="60">
        <template #default="scope">
          <span>{{ props.genderDict[scope.row.gender] }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="角色" prop="roleNames" width="120"/>
      <el-table-column align="center" label="部门" prop="deptName" width="120"/>
      <el-table-column align="center" label="邮箱" prop="email" width="200"/>
      <el-table-column align="center" label="手机号码" prop="phoneNumber" width="200"/>
      <el-table-column v-permission="[UserAPI.UPDATE_STATUS.permission]" label="状态" min-width="80">
        <template #default="scope">
          <el-tag v-show="scope.row.status !== null"
                  :type="EnableStatusEnum.TAG_STYLE[scope.row.status % 2]"
                  @click="updateDataStatus(scope.row.id,scope.row.username,!scope.row.status,props.loadData)">
            {{ EnableStatusEnum.OPTIONS[scope.row.status] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" prop="createTime" width="180"/>
      <el-table-column v-permission="[UserAPI.UPDATE.permission, UserAPI.DELETE.permission]"
                       :fixed="device === DeviceEnum.MOBILE ? false : 'right'" align="center" label="操作"
                       width="220">
        <template #default="scope">
          <el-button
              v-permission="[UserAPI.ADMIN_RESET_PASSWORD.permission]"
              link
              size="small"
              type="warning"
              @click="resetPassword(scope.row.id,scope.row.username)"
          >
            <el-icon>
              <refresh-left/>
            </el-icon>
            重置密码
          </el-button>
          <el-button
              v-permission="[UserAPI.UPDATE.permission]"
              link
              size="small"
              type="warning"
              @click="openUserDialog(scope.row.id)"
          >
            <el-icon>
              <edit/>
            </el-icon>
            编辑
          </el-button
          >
          <el-button
              v-permission="[UserAPI.DELETE.permission]"
              link
              size="small"
              type="danger"
              @click="deleteData([scope.row.id],scope.row.username,props.loadData)"
          >
            <el-icon>
              <delete/>
            </el-icon>
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <template #footer>
      <el-scrollbar>
        <Pagination v-model:current-page="query.pageNum"
                    v-model:page-size="query.pageSize"
                    :total="props.total" @handle-page-change="props.loadData()"/>
      </el-scrollbar>
    </template>
  </el-card>
  <!-- 用户管理模态框 -->
  <user-manage-dialog
      v-if="visible"
      v-model:visible="visible"
      :close-dialog="closeDialog"
      :current-click-row-id="currentClickRowId"
      :dept-options="props.deptOptions"
      :device="device"
      :gender-dict="props.genderDict"
      :load-data="props.loadData"
      :role-options="props.roleOptions"
      :title="title"
  />
</template>

<script lang="ts" setup>
import {UserAPI} from "@/api/system/user";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {EnableStatusEnum} from "@/constants/system";
import {useDialogManage} from "@/hooks/useDialogManage";
import {useCrudActions} from "@/hooks/useCrudActions";
import {TableInstance} from "element-plus";
import {useTableManagement} from "@/hooks/useTableManagement";
import {useSystemStore} from "@/store/modules/system";
import {UserForm, UserPageQuery, UserPageVO} from "@/api/system/user/type";

defineOptions({
  name: "UserTable",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  query: UserPageQuery;
  dataList: UserPageVO[];
  deptOptions: OptionType[];                                             // 部门集合
  roleOptions: OptionType[];                                             // 角色集合
  genderDict: Record<number | string, string>;                           // 性别字典
  total: number;
  loading: boolean;
  loadData: (callback?: () => void) => Promise<void>;                    // 加载数据函数
  loadDeptData: (callback?: () => void) => Promise<void>;                // 加载部门数据
  loadRoleData: (callback?: () => void) => Promise<void>;                // 加载角色数据
}>(), {});
const emits = defineEmits<{
  (event: "update:query", query: UserPageQuery): void
}>()
// hooks
const query = useVModel(props, 'query', emits)
const {
  visible,
  title,
  openDialog,
  closeDialog
} = useDialogManage();
const {
  deleteData,
  updateDataStatus
} = useCrudActions<string, UserForm>(UserAPI.SAVE.request, UserAPI.UPDATE.request, UserAPI.DELETE.request, UserAPI.UPDATE_STATUS.request);
const dataTableRef = ref<TableInstance | null>(null);
const {selectedIds: userIds, handleCellDblclick, handleSelectionChange} = useTableManagement<string>(dataTableRef);

// 数据
const device = computed(() => useSystemStore().app.device)            // 设备类型
const currentClickRowId = ref<string | undefined>();                  // 打开 dialog 点击的 row

// 方法
/**
 * 打开模态框
 * @param userId
 */
function openUserDialog(userId?: string) {
  currentClickRowId.value = userId;
  // 加载角色数据
  props.loadRoleData();
  // 加载部门数据
  props.loadDeptData();
  if (userId) {
    openDialog("修改用户");
  } else {
    openDialog("新增用户");
  }
}

/**
 * 重置密码
 * @param userId 用户Id
 * @param username 用户名
 */
function resetPassword(userId: string, username: string) {
  ElMessageBox.prompt(`您正在重置【 <b> ${username} </b> 】 用户的密码`, '⚠️ 警告', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    inputPattern: /^.{8,16}$/, // 密码长度在 8 到 16 位之间
    inputErrorMessage: '密码长度在 8 ~ 16 位',
    dangerouslyUseHTMLString: true,
  }).then(({value}) => {
    // /^(?=.{8,16}$)(?![0-9]*$)(?![a-zA-Z]*$)[\w!#$%&'*+/=?^_`{|}~-]+$/, // 正则表达式：长度在8到16之间，包含字母和数字
    UserAPI.ADMIN_RESET_PASSWORD.request(userId, value).then(() => {
      ElMessage.success(`重置密码【 ${username} 】成功`);
    })
  })
}
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
