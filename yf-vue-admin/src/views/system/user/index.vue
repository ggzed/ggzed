<template>
  <div class="app-container">
    <!--  搜索框  -->
    <div class="search-container">
      <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
        <el-form-item label="关键词 :" prop="keywords">
          <el-input
              v-model="query.keywords"
              clearable
              placeholder="用户名/昵称/手机号/邮箱"
              @keyup.enter="loadData"
          />
        </el-form-item>
        <el-form-item label="用户状态 :" prop="status">
          <el-select
              v-model="query.status"
              clearable
              placeholder="用户状态"
          >
            <el-option v-for="(value,key) in EnableStatusEnum.OPTIONS" :label="value" :value="Number(key)"/>
          </el-select>
        </el-form-item>

        <el-form-item label="创建时间 :" prop="startTime">
          <el-date-picker
              v-model="query.startTime"
              format="YYYY/MM/DD"
              placeholder="创建时间"
              type="date"
              value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>

        <el-form-item label="截至时间 :" prop="endTime">
          <el-date-picker
              v-model="query.endTime"
              format="YYYY/MM/DD"
              placeholder="创建时间"
              type="date"
              value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>

        <el-form-item label="所属部门 :" prop="deptId">
          <el-scrollbar max-height="400px">
            <el-tree-select
                v-model="query.deptId"
                :data="deptOptions"
                :loading="deptLoading"
                check-strictly
                clearable
                @focus="handleDeptOptions"
            >
            </el-tree-select>
          </el-scrollbar>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="loadData">
            <template #icon>
              <search/>
            </template>
            搜索
          </el-button>
          <el-button plain type="primary" @click="resetQuery">
            <template #icon>
              <refresh/>
            </template>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>
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
                     @click="handleDeleteOrStatusChange(userIds)">
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
          v-loading="loading"
          :data="dataList"
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
            <span>{{ genderDictOptions[scope.row.gender] }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="身份" prop="roleNames" width="120"/>
        <el-table-column align="center" label="部门" prop="deptName" width="120"/>
        <el-table-column align="center" label="邮箱" prop="email" width="200"/>
        <el-table-column align="center" label="手机号码" prop="phoneNumber" width="200"/>
        <el-table-column v-permission="[UserAPI.UPDATE_STATUS.permission]" label="状态" min-width="80">
          <template #default="scope">
            <el-tag v-show="scope.row.status !== null"
                    :type="EnableStatusEnum.TAG_STYLE[scope.row.status % 2]"
                    @click="handleDeleteOrStatusChange([scope.row.id],scope.row.username,!scope.row.status)">
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
                @click="handleDeleteOrStatusChange([scope.row.id],scope.row.username)"
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
                      :total="total" @handle-page-change="loadData"/>
        </el-scrollbar>
      </template>
    </el-card>
    <!--  模态框  -->
    <el-dialog
        v-model="dialog.visible"
        :title="dialog.title"
        :width="device === DeviceEnum.MOBILE ? '90%' : '50%'"
        draggable
        overflow
        @close="closeUserDialog"
    >
      <el-form
          ref="userFormRef"
          :model="form"
          :rules="rules"
          label-width="100px"
      >
        <el-form-item label="用户名 :" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="用户昵称 :" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入用户昵称"/>
        </el-form-item>
        <el-form-item label="性别 :" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio v-for="(value,key) in genderDictOptions" :value="Number(key)">{{ value }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="身份信息 :" prop="roleIds">
          <el-select v-model="form.roleIds" multiple placeholder="请您选择一个角色">
            <el-option
                v-for="item in roleOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属部门 :" prop="deptId">
          <el-tree-select
              v-model="form.deptId"
              :data="deptOptions"
              check-strictly
              clearable
              placeholder="请您选择部门"
          >
          </el-tree-select>
        </el-form-item>
        <el-form-item label="邮箱 :" prop="email">
          <el-input v-model="form.email" placeholder="请输入用户邮箱"/>
        </el-form-item>
        <el-form-item label="手机号 :" prop="phoneNumber">
          <el-input v-model="form.phoneNumber" placeholder="请输入用户手机号"/>
        </el-form-item>
        <el-form-item label="用户状态 :" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="optionType in EnableStatusEnum.OPTIONS_RADIO" :value="Number(optionType.value)">{{
                optionType.label
              }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="closeUserDialog">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
// 数据
import {UserForm, UserPageQuery} from "@/api/system/user/type";
import {DeptAPI} from "@/api/system/dept";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {UserAPI} from "@/api/system/user";
import {useSystemStore} from "@/store/modules/system";
import {EnableStatusEnum} from "@/constants/system";
import {useDictionary} from "@/hooks/userDict";
import {DictType} from "@/api/system/dict/data/type";
import {RoleAPI} from "@/api/system/role";
import {FormInstance, FormRules, TableInstance} from "element-plus";
import {useDialogManagement} from "@/hooks/useDialogManagement";
import {useFormManagement} from "@/hooks/useFormManagement";
import {useHandleDeleteOrStatusChange} from "@/hooks/handleDeleteOrStatusChange";
import {useTableManagement} from "@/hooks/useTableManagement";
import {usePageDataLoader} from "@/hooks/useDataLoader";

defineOptions({
  name: "User",
  inheritAttrs: false,
});

const queryFormRef = ref<FormInstance | null>(null);                             // 查询表单
const dataTableRef = ref<TableInstance | null>(null);                            // 数据Table
const userFormRef = ref<FormInstance | null>(null);                              // 新增/修改用户表单

// 初始查询数据
const initialQuery: UserPageQuery = {
  keywords: "",
  pageNum: 1,
  pageSize: 10
}
// 初始表单数据
const initialForm: UserForm = {
  id: undefined,
  deptId: undefined,
  avatar: undefined,
  username: "",
  nickname: "",
  gender: 1,
  status: 1,
  roleIds: []
}
// 初始校验规则
const initialRules: FormRules = {
  username: [
    {required: true, message: "用户名不能为空", trigger: "blur"},
    {max: 24, message: "用户名过长", trigger: "blur"}
  ],
  nickname: [
    {required: true, message: "用户昵称不能为空", trigger: "blur"},
    {max: 24, message: "昵称过长", trigger: "blur"}
  ],
  phoneNumber: [
    {
      pattern: /^$|^1(3\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\d|9[0-35-9])\d{8}$/,
      message: "请输入正确的手机号码",
      trigger: "blur"
    }
  ],
  email: [{type: "email", message: "请输入正确的邮箱地址", trigger: "blur"}],
  deptId: [{required: true, message: "所属部门不能为空", trigger: "blur"}],
  roleIds: [{required: true, message: "用户角色不能为空", trigger: "blur"}]
}

const pageQueryApi = (query: UserPageQuery) => UserAPI.PAGE.request(query)  // hooks => 查询API
const updateStatusApi = (id: string, status: boolean) => UserAPI.UPDATE_STATUS.request(id, status) // hooks => 修改状态API
const handleDeleteApi = (ids: string) => UserAPI.DELETE.request(ids)        // hooks => 删除API

const {
  dataList,
  total,
  query,
  loading,
  resetQuery,
  loadData
} = usePageDataLoader(pageQueryApi, initialQuery, queryFormRef);
const {selectedIds: userIds, handleCellDblclick, handleSelectionChange} = useTableManagement<string>(dataTableRef);
const {handleDeleteOrStatusChange} = useHandleDeleteOrStatusChange(updateStatusApi, handleDeleteApi, loadData);
const {form, rules, resetForm, handleSubmit} = useFormManagement(initialForm, initialRules, userFormRef);
const {dialog, openDialog, closeDialog} = useDialogManagement();

const deptOptions = ref<OptionType[]>([])                             // 部门数据
const roleOptions = ref<OptionType[]>([])                             // 角色数据
const deptLoading = ref<boolean>(false)                               // 部门加载状态
const genderDictOptions = await useDictionary(DictType.GENDER)        // 性别字典数据
const device = computed(() => useSystemStore().app.device)            // 设备类型

/**
 * 打开模态框
 * @param userId
 */
function openUserDialog(userId?: string) {
  // 加载角色数据
  handleRoleOptions();
  // 加载部门数据
  handleDeptOptions();
  if (userId) {
    openDialog("修改用户", () => {
      UserAPI.FORM.request(userId).then(({data}) => {
        form.id = userId;
        Object.assign(form, data);
      });
    });
  } else {
    openDialog("新增用户");
  }
}

/**
 * 关闭模态框
 */
function closeUserDialog() {
  closeDialog(() => resetForm())
}

/**
 * 处理新增/修改表单
 */
function submitForm() {
  handleSubmit((form) => {
    const userId = form.id;
    // 根据 userId 判断新增还是修改 (注 : 操作完成重置查询)
    if (userId) {
      UserAPI.UPDATE.request(userId, form).then(() => {
        ElMessage.success("修改成功");
        closeUserDialog();
        resetQuery();
      });
    } else {
      UserAPI.SAVE.request(form).then(() => {
        ElMessage.success("新增成功,用户赋予平台默认密码");
        closeUserDialog();
        resetQuery();
      });
    }
  })
}

/**
 * 加载角色数据
 */
function handleRoleOptions() {
  if (roleOptions.value.length === 0) {
    // 获取部门数据
    RoleAPI.OPTIONS.request().then(({data}) => {
      roleOptions.value = data;
    })
  }
}

/**
 * 查询部门操作项
 */
function handleDeptOptions() {
  if (deptOptions.value.length === 0) {
    deptLoading.value = true
    // 获取部门数据
    DeptAPI.OPTIONS.request().then(({data}) => {
      deptOptions.value = data;
    }).finally(() => {
      deptLoading.value = false
    })
  }
}

/**
 * 重置密码
 * @param userId 用户Id
 * @param username 用户名
 */
function resetPassword(userId: number, username: string) {
  console.log("重置密码", userId, username);
}

// 生命周期
onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
