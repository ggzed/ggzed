<template>
  <div class="app-container">
    <!--  搜索框  -->
    <div class="search-container">
      <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
        <el-form-item label="关键词 :" prop="keywords">
          <el-input
              v-model="query.keywords"
              clearable
              placeholder="部门名称"
              @keyup.enter="loadData"
          />
        </el-form-item>
        <el-form-item label="状态 :" prop="status">
          <el-select v-model="query.status" placeholder="部门状态">
            <el-option v-for="(value,key) in EnableStatusEnum.OPTIONS" :label="value" :value="Number(key)"/>
          </el-select>
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
    <!--  主体-->
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
          v-loading="loading"
          :data="dataList"
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
                    @click="handleDeleteOrStatusChange([scope.row.id],scope.row.name,!scope.row.status)">
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
                @click.stop="handleDeleteOrStatusChange([scope.row.id],scope.row.name)"
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

    <!-- 增加修改模态框   -->
    <el-dialog
        v-model="dialog.visible"
        :title="dialog.title"
        :width="device === DeviceEnum.MOBILE ? '90%' : '50%'"
        destroy-on-close
        draggable
        overflow
        @close="closeDeptDialog"
    >
      <el-form
          ref="deptFormRef"
          :model="form"
          :rules="rules"
          label-width="110px"
      >
        <el-form-item label="父级部门 :" prop="parentId">
          <el-tree-select
              v-model="form.parentId"
              :data="deptOptions"
              :render-after-expand="false"
              check-strictly
              filterable
              placeholder="选择上级部门"
          />
        </el-form-item>

        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称"/>
        </el-form-item>

        <el-form-item
            label="状态 :"
            prop="status"
        >
          <el-radio-group v-model="form.status">
            <el-radio v-for="optionType in EnableStatusEnum.OPTIONS_RADIO" :value="Number(optionType.value)">{{
                optionType.label
              }}
            </el-radio>
          </el-radio-group>
        </el-form-item>


        <el-form-item label="排序 :" prop="sort">
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
          <el-button @click="closeDeptDialog">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>


<script lang="ts" setup>

import {EnableStatusEnum} from "@/constants/system";
import {DeptForm, DeptPageQuery} from "@/api/system/dept/type";
import {DeptAPI} from "@/api/system/dept";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {useSystemStore} from "@/store/modules/system";
import {useFormManagement} from "@/hooks/useFormManagement";
import {FormInstance, FormRules} from "element-plus";
import {useDataLoader} from "@/hooks/useDataLoader";
import {useHandleDeleteOrStatusChange} from "@/hooks/handleDeleteOrStatusChange";
import {useDialogManagement} from "@/hooks/useDialogManagement";

defineOptions({
  name: "Dept",
  inheritAttrs: false,
});
const pageQueryApi = (query: DeptPageQuery) => DeptAPI.PAGE.request(query)  // hooks => 查询API
const updateStatusApi = (id: number, status: boolean) => DeptAPI.UPDATE_STATUS.request(id, status) // hooks => 修改状态API
const handleDeleteApi = (ids: string) => DeptAPI.DELETE.request(ids)        // hooks => 删除API

const device = computed(() => useSystemStore().app.device)    // 设备类型

const queryFormRef = ref<FormInstance | null>(null);         // 查询表单
const deptFormRef = ref<FormInstance | null>(null);          // 部门表单
const initialQuery: DeptPageQuery = {}                       // 初始化查询条件
const initialForm: DeptForm = {
  id: undefined,
  name: "",
  parentId: 0,
  status: 1,
  sort: 1,
}                                                        // 部门表单初始化数据
const initialRules: FormRules = {
  parentId: [{required: true, message: "请选择顶级部门", trigger: "blur"}],
  name: [{required: true, message: "请输入部门名称", trigger: "blur"}],
}                                                        // 部门表单校验规则
const {
  dataList,
  query,
  loading,
  resetQuery,
  loadData
} = useDataLoader(pageQueryApi, initialQuery, queryFormRef);
const {
  form,
  rules,
  resetForm,
  handleSubmit
} = useFormManagement(initialForm, initialRules, deptFormRef);
const {handleDeleteOrStatusChange} = useHandleDeleteOrStatusChange(updateStatusApi, handleDeleteApi, loadData);
const {dialog, openDialog, closeDialog} = useDialogManagement();

const deptOptions = ref<OptionType[]>();                 // 部门操作列表

/**
 * 打开 增加修改 模态框
 */
function openDeptDialog(parentId ?: number, deptId?: number) {
  // 1. 查询菜单 options ( 注 : 不使用懒加载)
  DeptAPI.OPTIONS.request().then(({data}) => {
    deptOptions.value = [{value: 0, label: "顶级部门", children: data}];
  }).then(() => {
    if (deptId) {
      openDialog("编辑部门", () => {
        DeptAPI.FORM.request(deptId).then(({data}) => {
          form.id = deptId;
          Object.assign(form, data);
        })
      })
    } else {
      openDialog("新增部门", () => form.parentId = parentId);
    }
  })
}

/**
 * 关闭 增加修改 模态框
 */
function closeDeptDialog() {
  closeDialog(() => resetForm())
}

/**
 * 增加修改请求提交
 */
function submitForm() {
  handleSubmit((form) => {
    // 2. 判断 deptId 为新增还是修改
    if (form.id) {
      // 3. 提交请求
      DeptAPI.UPDATE.request(form.id, form).then(() => {
        ElMessage.success("修改数据成功");
        // 4. 关闭模态框
        closeDeptDialog();
        loadData();
      });
    } else {
      // 3. 提交请求
      DeptAPI.SAVE.request(form).then(() => {
        ElMessage.success("新增数据成功");
        // 4. 关闭模态框
        closeDeptDialog();
        loadData();
      });
    }
  })
}

// 生命周期
onMounted(() => {
  loadData();
})
</script>

<style lang="scss" scoped>

</style>
