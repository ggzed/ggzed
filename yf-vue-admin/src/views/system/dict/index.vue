<template>
  <div class="app-container">
    <!--  搜索框  -->
    <div class="search-container">
      <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
        <el-form-item label="字典名称 :" prop="name">
          <el-input
              v-model="query.name"
              clearable
              placeholder="字典名称搜索"
              @keyup.enter="loadData"
          />
        </el-form-item>
        <el-form-item label="字典类型 :" prop="type">
          <el-input
              v-model="query.type"
              clearable
              placeholder="字典类型搜索"
              @keyup.enter="loadData"
          />
        </el-form-item>
        <el-form-item label="状态 :" prop="status">
          <el-select
              v-model="query.status"
              clearable
              placeholder="字典使用状态"
          >
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
    <!--  主体 ( 左侧操作框 / 右侧操作框 )  -->
    <el-card class="table-container">
      <!--  顶部操作    -->
      <template #header>
        <div>
          <el-button v-permission="[DictTypeAPI.SAVE.permission]"
                     :circle="device === DeviceEnum.MOBILE"
                     plain
                     type="success"
                     @click="openDictTypeDialog()">
            <el-icon>
              <plus/>
            </el-icon>
            <span v-show="device !== DeviceEnum.MOBILE"> 新增 </span>
          </el-button>
          <el-button v-permission="[DictTypeAPI.UPDATE.permission]"
                     :circle="device === DeviceEnum.MOBILE"
                     :disabled="dictTypeIds.length !== 1"
                     plain
                     type="warning"
                     @click="openDictTypeDialog(dictTypeIds[0])">
            <el-icon>
              <edit/>
            </el-icon>
            <span v-show="device !== DeviceEnum.MOBILE"> 修改 </span>
          </el-button>
          <el-button v-permission="[DictTypeAPI.DELETE.permission]"
                     :circle="device === DeviceEnum.MOBILE"
                     :disabled="dictTypeIds.length === 0"
                     plain
                     type="danger"
                     @click="handleDeleteOrStatusChange(dictTypeIds)">
            <el-icon>
              <delete/>
            </el-icon>
            <span v-show="device !== DeviceEnum.MOBILE"> 删除 </span>
          </el-button>
        </div>
        <div>

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
        <el-table-column align="center" label="字典名称" min-width="120" prop="name"/>
        <el-table-column align="center" label="字典类型" min-width="120" prop="type"/>
        <el-table-column v-permission="[DictTypeAPI.UPDATE_STATUS.permission]" align="center" label="状态"
                         min-width="80">
          <template #default="scope">
            <el-tag v-show="scope.row.status !== null"
                    :type="EnableStatusEnum.TAG_STYLE[scope.row.status % 2]"
                    @click="handleDeleteOrStatusChange([scope.row.id],scope.row.name,!scope.row.status)">
              {{ EnableStatusEnum.OPTIONS[scope.row.status] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" label="备注" min-width="180" prop="remark"/>
        <el-table-column align="center" label="创建时间" min-width="120" prop="createTime"/>
        <el-table-column align="center" label="修改时间" min-width="120" prop="updateTime"/>
        <el-table-column v-permission="[DictTypeAPI.UPDATE.permission, DictTypeAPI.DELETE.permission]"
                         :fixed="device === DeviceEnum.MOBILE ? false : 'right'" align="center" label="操作"
                         width="220">
          <template #default="scope">
            <el-button
                v-permission="[DictDataAPI.PAGE.permission]"
                link
                size="small"
                type="warning"
                @click="goToDictDataPage(scope.row.id)"
            >
              <el-icon>
                <Position/>
              </el-icon>
              字典详情
            </el-button>
            <el-button
                v-permission="[DictTypeAPI.UPDATE.permission]"
                link
                size="small"
                type="warning"
                @click="openDictTypeDialog(scope.row.id)"
            >
              <el-icon>
                <edit/>
              </el-icon>
              编辑
            </el-button
            >
            <el-button
                v-permission="[DictTypeAPI.DELETE.permission]"
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
      <!--  底部分页    -->
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
        @close="closeDictTypeDialog"
    >
      <el-form
          ref="dictTypeFormRef"
          :model="form"
          :rules="rules"
          label-width="100px"
      >
        <el-form-item label="字典名称 :" prop="name">
          <el-input v-model="form.name" placeholder="请输入字典名称"/>
        </el-form-item>
        <el-form-item label="字典类型 :" prop="type">
          <el-input v-model="form.type" placeholder="请输入字典类型"/>
        </el-form-item>
        <el-form-item label="字典状态 :" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="optionType in EnableStatusEnum.OPTIONS_RADIO" :value="Number(optionType.value)">{{
                optionType.label
              }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注 :" prop="remark">
          <el-input v-model="form.remark" :autosize="{ minRows: 3, maxRows: 5 }" maxlength="255"
                    placeholder="请输入字典备注"
                    show-word-limit type="textarea"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="closeDictTypeDialog">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>

import {FormInstance, FormRules, TableInstance} from "element-plus";
import {DictTypeForm, DictTypePageQuery} from "@/api/system/dict/type/type";
import {DictTypeAPI} from "@/api/system/dict/type";
import {usePageDataLoader} from "@/hooks/useDataLoader";
import {useTableManagement} from "@/hooks/useTableManagement";
import {useHandleDeleteOrStatusChange} from "@/hooks/handleDeleteOrStatusChange";
import {useFormManagement} from "@/hooks/useFormManagement";
import {useDialogManagement} from "@/hooks/useDialogManagement";
import {EnableStatusEnum} from "@/constants/system";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {useSystemStore} from "@/store/modules/system";
import {DictDataAPI} from "@/api/system/dict/data";

defineOptions({
  name: "Dict",
  inheritAttrs: false,
});
// 数据
const router = useRouter();
const queryFormRef = ref<FormInstance | null>(null);                  // 查询表单
const dataTableRef = ref<TableInstance | null>(null);                 // 数据Table
const dictTypeFormRef = ref<FormInstance | null>(null);
const device = computed(() => useSystemStore().app.device)            // 设备类型

// 初始查询数据
const initialQuery: DictTypePageQuery = {
  pageNum: 1,
  pageSize: 10
}
// 初始表单数据
const initialForm: DictTypeForm = {
  id: undefined,
  name: undefined,
  remark: undefined,
  status: 1,
  type: undefined
}
// 初始校验规则
const initialRules: FormRules = {
  name: [{required: true, message: "请输入字典类型名称", trigger: "blur"}],
  type: [{required: true, message: "请输入字典类型", trigger: "blur"}]
}

const pageQueryApi = (query: DictTypePageQuery) => DictTypeAPI.PAGE.request(query)  // hooks => 查询API
const updateStatusApi = (id: number, status: boolean) => DictTypeAPI.UPDATE_STATUS.request(id, status) // hooks => 修改状态API
const handleDeleteApi = (ids: string) => DictTypeAPI.DELETE.request(ids)        // hooks => 删除API

const {
  dataList,
  total,
  query,
  loading,
  resetQuery,
  loadData
} = usePageDataLoader(pageQueryApi, initialQuery, queryFormRef);
const {selectedIds: dictTypeIds, handleCellDblclick, handleSelectionChange} = useTableManagement<number>(dataTableRef);
const {handleDeleteOrStatusChange} = useHandleDeleteOrStatusChange(updateStatusApi, handleDeleteApi, loadData);
const {form, rules, resetForm, handleSubmit} = useFormManagement(initialForm, initialRules, dictTypeFormRef);
const {dialog, openDialog, closeDialog} = useDialogManagement();

// 方法
function openDictTypeDialog(dictTypeId?: number) {
  if (dictTypeId) {
    openDialog("修改字典类型", () => {
      DictTypeAPI.FORM.request(dictTypeId).then(({data}) => {
        form.id = dictTypeId;
        Object.assign(form, data);
      });
    });
  } else {
    openDialog("新增字典类型");
  }
}

/**
 * 关闭模态框
 */
function closeDictTypeDialog() {
  closeDialog(() => resetForm())
}

function submitForm() {
  handleSubmit((form) => {
    const dictTypeId = form.id;
    // 根据 userId 判断新增还是修改 (注 : 操作完成重置查询)
    if (dictTypeId) {
      DictTypeAPI.UPDATE.request(dictTypeId, form).then(() => {
        ElMessage.success("修改成功");
        closeDictTypeDialog();
        resetQuery();
      });
    } else {
      DictTypeAPI.SAVE.request(form).then(() => {
        ElMessage.success("新增成功");
        closeDictTypeDialog();
        resetQuery();
      });
    }
  })
}

/**
 * 跳转到字典详情页
 * @param dictTypeId 字典类型 Id
 */
function goToDictDataPage(dictTypeId: number) {
  router.push("/system/dict-detail/" + dictTypeId);
}

// 生命周期
onMounted(() => {
  loadData();
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
