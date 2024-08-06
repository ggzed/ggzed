<template>
  <div class="app-container">
    <!--  搜索框  -->
    <div class="search-container">
      <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
        <el-form-item label="关键词 :" prop="keywords">
          <el-input
              v-model="query.keywords"
              clearable
              placeholder="角色名称/角色编码"
              @keyup.enter="loadData"
          />
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
    <!--  主体( 操作/表单/分页 )  -->
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
                     @click="handleDeleteOrStatusChange(roleIds)">
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
          v-loading="loading"
          :data="dataList"
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
                    @click="handleDeleteOrStatusChange([scope.row.id],scope.row.name,!scope.row.status)">
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
                @click="openMenuDialog(scope.row.id,scope.row.name)"
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
                @click="handleDeleteOrStatusChange([scope.row.id],scope.row.name)"
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
                      :total="total" @handle-page-change="loadData"/>
        </el-scrollbar>
      </template>
    </el-card>
    <!--  增加/修改模态框  -->
    <el-dialog
        v-model="dialog.visible"
        :title="dialog.title"
        :width="device === DeviceEnum.MOBILE ? '90%' : '50%'"
        draggable
        overflow
        @close="closeRoleDialog"
    >
      <el-form
          ref="roleFormRef"
          :model="form"
          :rules="rules"
          label-width="100px"
      >
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称"/>
        </el-form-item>

        <el-form-item label="角色编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入角色编码"/>
        </el-form-item>

        <el-form-item label="数据权限" prop="dataScope">
          <el-select v-model="form.dataScope">
            <el-option v-for="(value,key) in dataScopeDictData" :key="Number(key)" :label="value"
                       :value="Number(key)"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="optionType in EnableStatusEnum.OPTIONS_RADIO" :value="Number(optionType.value)">{{
                optionType.label
              }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="排序" prop="sort">
          <el-input-number
              v-model="form.sort"
              :min="0"
              controls-position="right"
              style="width: 100px"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="closeRoleDialog">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <!--  角色分配模态框  -->
    <el-dialog
        v-model="menuDialog.visible"
        :title="menuDialog.title"
        :width="device === DeviceEnum.MOBILE ? '90%' : '50%'"
        draggable
        overflow
    >
      <el-scrollbar max-height="600px">
        <el-tree
            ref="menuRef"
            :data="menuList"
            :loading="menuDialog.loading"
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
          <el-button type="primary" @click="handleSaveMenuIds">
            确 定
          </el-button>
          <el-button @click="menuDialog.visible = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import {RoleForm, RolePageQuery} from "@/api/system/role/type";
import {MenuAPI} from "@/api/system/menu";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {useSystemStore} from "@/store/modules/system";
import {RoleAPI} from "@/api/system/role";
import {EnableStatusEnum} from "@/constants/system";
import {Position} from "@element-plus/icons-vue";
import {useDictionary} from "@/hooks/userDict";
import {DictType} from "@/api/system/dict/data/type";
import {FormInstance, FormRules, TableInstance} from "element-plus";
import {useTableManagement} from "@/hooks/useTableManagement";
import {usePageDataLoader} from "@/hooks/useDataLoader";
import {useHandleDeleteOrStatusChange} from "@/hooks/handleDeleteOrStatusChange";
import {useFormManagement} from "@/hooks/useFormManagement";
import {useDialogManagement} from "@/hooks/useDialogManagement";

defineOptions({
  name: "Role",
  inheritAttrs: false,
});
// 数据
const queryFormRef = ref<FormInstance | null>(null);                             // 查询表单
const dataTableRef = ref<TableInstance | null>(null);                            // 数据Table
const roleFormRef = ref<FormInstance | null>(null);                              // 新增/修改角色表单
const menuRef = ref(ElTree);                                                     // 分配菜单表单

const dataScopeDictData: Record<number | string, string> = reactive({})          // 数据权限字典数据

// 初始查询数据
const initialQuery: RolePageQuery = {
  keywords: "",
  pageNum: 1,
  pageSize: 10
}
// 初始表单数据
const initialForm: RoleForm = {
  id: undefined,
  sort: 1,
  status: 1,
  dataScope: 0,
  code: "",
  name: ""
}
// 初始校验规则
const initialRules: FormRules = {
  name: [{required: true, message: "请输入角色名称", trigger: "blur"}],
  code: [{required: true, message: "请输入角色编码", trigger: "blur"}],
  dataScope: [{required: true, message: "请选择数据权限", trigger: "blur"}],
  status: [{required: true, message: "请选择状态", trigger: "blur"}]
}

const pageQueryApi = (query: RolePageQuery) => RoleAPI.PAGE.request(query)  // hooks => 查询API
const updateStatusApi = (id: number, status: boolean) => RoleAPI.UPDATE_STATUS.request(id, status) // hooks => 修改状态API
const handleDeleteApi = (ids: string) => RoleAPI.DELETE.request(ids)        // hooks => 删除API

const {
  dataList,
  total,
  query,
  loading,
  resetQuery,
  loadData
} = usePageDataLoader(pageQueryApi, initialQuery, queryFormRef);
const {selectedIds: roleIds, handleCellDblclick, handleSelectionChange} = useTableManagement<number>(dataTableRef);
const {handleDeleteOrStatusChange} = useHandleDeleteOrStatusChange(updateStatusApi, handleDeleteApi, loadData);
const {form, rules, resetForm, handleSubmit} = useFormManagement(initialForm, initialRules, roleFormRef);
const {dialog, openDialog, closeDialog} = useDialogManagement();

const currentCheckId = ref<number>()                          // 当前选中的角色id
const device = computed(() => useSystemStore().app.device)    // 设备类型
const menuList = ref<OptionType[]>([])                        // 菜单列表
const {
  dialog: menuDialog,
  openDialog: openMenuDialogManagement
} = useDialogManagement();

// 方法

/**
 * 打开角色新增/修改弹窗
 * @param roleId
 */
function openRoleDialog(roleId?: number) {
  // 加载字典数据
  if (Object.keys(dataScopeDictData).length === 0) {
    useDictionary(DictType.DATA_PERMISSION).then((dictData) => {
      Object.assign(dataScopeDictData, dictData);
    })
  }
  // 根据 role 判断新增或者修改
  if (roleId) {
    openDialog("修改角色", () => {
      RoleAPI.FORM.request(roleId).then(({data}) => {
        form.id = roleId;
        Object.assign(form, data);
      });
    })
  } else {
    openDialog("新增角色");
  }
}

/**
 * 关闭角色新增/修改弹窗
 */
function closeRoleDialog() {
  closeDialog(() => resetForm())
}

/**
 * 角色新增/修改表单提交
 */
function submitForm() {
  // 校验表单
  handleSubmit((form) => {
    const roleId = form.id;
    // 根据 roleId 判断新增还是修改 (注 : 操作完成重置查询)
    if (roleId) {
      RoleAPI.UPDATE.request(roleId, form).then(() => {
        ElMessage.success("修改成功");
        closeRoleDialog();
        resetQuery();
      });
    } else {
      RoleAPI.SAVE.request(form).then(() => {
        ElMessage.success("新增成功");
        closeRoleDialog();
        resetQuery();
      });
    }
  })
}

/**
 * 打开角色分配菜单权限
 * @param roleId 角色Id
 * @param roleName 角色名
 */
async function openMenuDialog(roleId: number, roleName: string) {
  openMenuDialogManagement(`您正在分配 【 ${roleName} 】  角色的权限`, async () => {
    // 1. 获取当前选中的角色ID
    currentCheckId.value = roleId;
    // 2. 获取所有菜单展示
    const {data} = await MenuAPI.OPTIONS.request();
    menuList.value = data;
    // 3. 获取当前角色拥有的菜单
    RoleAPI.MENU_IDS.request(roleId).then(({data}) => {
      data.forEach(menuId => {
        menuRef.value.setChecked(menuId, true, false);
      })
    })
  })
}

/**
 * 保存角色分配的菜单权限
 */
function handleSaveMenuIds() {
  // 1. 获取选中角色ID
  const roleId = currentCheckId.value;
  if (roleId) {
    // 2. 获取选中节点id
    const checkedMenuIds: number[] = menuRef.value
        .getCheckedNodes(false, true)
        .map((node: OptionType) => node.value);
    // 3. 给选中角色分配菜单权限
    RoleAPI.UPDATE_MENUS.request(roleId, checkedMenuIds).then(() => {
      ElMessage.success("分配权限成功");
      menuDialog.visible = false;
      currentCheckId.value = undefined;
    })
  }
}

// 生命周期
onMounted(() => {
  loadData();
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
