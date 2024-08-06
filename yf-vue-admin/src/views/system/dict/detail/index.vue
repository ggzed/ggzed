<template>
  <div class="app-container">
    <!--  搜索框  -->
    <div class="search-container">
      <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
        <el-form-item label="名称 :" prop="name">
          <el-input
              v-model="query.name"
              clearable
              placeholder="字典数据名称搜索"
              @keyup.enter="loadData"
          />
        </el-form-item>
        <el-form-item label="状态 :" prop="status">
          <el-select
              v-model="query.status"
              clearable
              placeholder="字典数据使用状态"
          >
            <el-option v-for="(value,key) in EnableStatusEnum.OPTIONS" :label="value" :value="Number(key)"/>
          </el-select>
        </el-form-item>
        <el-form-item label="是否默认 :" prop="defaulted">
          <el-select
              v-model="query.defaulted"
              clearable
              placeholder="字典数据是否默认"
          >
            <el-option v-for="(value,key) in DefaultedStatusEnum.OPTIONS" :label="value" :value="Number(key)"/>
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
          <el-button v-permission="[DictDataAPI.SAVE.permission]"
                     :circle="device === DeviceEnum.MOBILE"
                     plain
                     type="success"
                     @click="openDictDataDialog()">
            <el-icon>
              <plus/>
            </el-icon>
            <span v-show="device !== DeviceEnum.MOBILE"> 新增 </span>
          </el-button>
          <el-button v-permission="[DictDataAPI.UPDATE.permission]"
                     :circle="device === DeviceEnum.MOBILE"
                     :disabled="dictDataIds.length !== 1"
                     plain
                     type="warning"
                     @click="openDictDataDialog(dictDataIds[0])">
            <el-icon>
              <edit/>
            </el-icon>
            <span v-show="device !== DeviceEnum.MOBILE"> 修改 </span>
          </el-button>
          <el-button v-permission="[DictDataAPI.DELETE.permission]"
                     :circle="device === DeviceEnum.MOBILE"
                     :disabled="dictDataIds.length === 0"
                     plain
                     type="danger"
                     @click="handleDeleteOrStatusChange(dictDataIds)">
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
        <el-table-column align="center" label="字典项名称" min-width="120" prop="name"/>
        <el-table-column align="center" label="字典项值" min-width="70" prop="value"/>
        <el-table-column align="center" label="顺序" min-width="60" prop="sort"/>
        <el-table-column align="center" label="是否默认" min-width="100">
          <template #default="scope">
            <el-tag v-show="scope.row.defaulted !== null"
                    :type="DefaultedStatusEnum.TAG_STYLE[scope.row.defaulted % 2]">
              {{ DefaultedStatusEnum.OPTIONS[scope.row.defaulted] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column v-permission="[DictDataAPI.UPDATE_STATUS.permission]" align="center" label="状态"
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
        <el-table-column v-permission="[DictDataAPI.UPDATE.permission, DictDataAPI.DELETE.permission]"
                         :fixed="device === DeviceEnum.MOBILE ? false : 'right'" align="center" label="操作"
                         width="180">
          <template #default="scope">
            <el-button
                v-permission="[DictDataAPI.UPDATE.permission]"
                link
                size="small"
                type="warning"
                @click="openDictDataDialog(scope.row.id)"
            >
              <el-icon>
                <edit/>
              </el-icon>
              编辑
            </el-button
            >
            <el-button
                v-permission="[DictDataAPI.DELETE.permission]"
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
        @close="closeDictDataDialog"
    >
      <el-form
          ref="dictDataFormRef"
          :model="form"
          :rules="rules"
          label-width="100px"
      >
        <el-form-item label="字典项名称 :" prop="name">
          <el-input v-model="form.name" placeholder="请输入字典名称"/>
        </el-form-item>
        <el-form-item label="字典项值 :" prop="value">
          <el-input v-model="form.value" placeholder="字典项值"></el-input>
        </el-form-item>
        <el-form-item label="字典顺序" prop="sort">
          <el-input-number
              v-model="form.sort"
              :min="0"
              controls-position="right"
              style="width: 100px"
          />
        </el-form-item>
        <el-form-item label="字典状态 :" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="optionType in EnableStatusEnum.OPTIONS_RADIO" :value="Number(optionType.value)">{{
                optionType.label
              }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <!--        <el-form-item label="是否默认 :" prop="defaulted">-->
        <!--          <el-radio-group v-model="form.defaulted">-->
        <!--            <el-radio v-for="optionType in DefaultedStatusEnum.OPTIONS_RADIO" :value="Number(optionType.value)">{{-->
        <!--                optionType.label-->
        <!--              }}-->
        <!--            </el-radio>-->
        <!--          </el-radio-group>-->
        <!--        </el-form-item>-->
        <el-form-item label="备注 :" prop="remark">
          <el-input v-model="form.remark" :autosize="{ minRows: 3, maxRows: 5 }" maxlength="255"
                    placeholder="请输入字典备注"
                    show-word-limit type="textarea"/>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="closeDictDataDialog">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>


import {TagView, useTagsViewStore} from "@/store/modules/tagsView";
import {FormInstance, FormRules, TableInstance} from "element-plus";
import {useSystemStore} from "@/store/modules/system";
import {DictTypeAPI} from "@/api/system/dict/type";
import {usePageDataLoader} from "@/hooks/useDataLoader";
import {useTableManagement} from "@/hooks/useTableManagement";
import {useHandleDeleteOrStatusChange} from "@/hooks/handleDeleteOrStatusChange";
import {useFormManagement} from "@/hooks/useFormManagement";
import {useDialogManagement} from "@/hooks/useDialogManagement";
import {DictDataForm, DictDataPageQuery} from "@/api/system/dict/data/type";
import {DictDataAPI} from "@/api/system/dict/data";
import {ComputedRef} from "vue";
import {DefaultedStatusEnum, EnableStatusEnum} from "@/constants/system";
import {DeviceEnum} from "@/enums/DeviceEnum";

defineOptions({
  name: "DictDetail/:id",
  inheritAttrs: false,
});
// 数据
const route = useRoute();
const router = useRouter();
const tagsViewStore = useTagsViewStore();
const queryFormRef = ref<FormInstance | null>(null);                  // 查询表单
const dataTableRef = ref<TableInstance | null>(null);                 // 数据Table
const dictDataFormRef = ref<FormInstance | null>(null);
const device = computed(() => useSystemStore().app.device)            // 设备类型
const dictTypeId = computed(() => (route.params.id || -1) as number)  // 字典类型ID

// 初始查询数据
const initialQuery: DictDataPageQuery = {
  defaulted: undefined,
  pageNum: 1,
  pageSize: 10
}
// 初始表单数据
const initialForm: DictDataForm = {
  id: undefined,
  name: undefined,
  remark: undefined,
  value: undefined,
  defaulted: 0,
  sort: 1,
  status: 1
}
// 初始校验规则
const initialRules: FormRules = {
  name: [{required: true, message: "请输入字典项名称", trigger: "blur"}],
  value: [{required: true, message: "请输入字典项值", trigger: "blur"}]
}

const pageQueryApi = (query: DictDataPageQuery, dictTypeId: ComputedRef<number>) => DictDataAPI.PAGE.request(dictTypeId.value, query)  // hooks => 查询API
const updateStatusApi = (dictDataId: number, status: boolean) => DictDataAPI.UPDATE_STATUS.request(dictDataId, status) // hooks => 修改状态API
const handleDeleteApi = (ids: string) => DictDataAPI.DELETE.request(ids)        // hooks => 删除API

const {
  dataList,
  total,
  query,
  loading,
  resetQuery,
  loadData
} = usePageDataLoader(pageQueryApi, initialQuery, queryFormRef, dictTypeId);
const {selectedIds: dictDataIds, handleCellDblclick, handleSelectionChange} = useTableManagement<number>(dataTableRef);
const {handleDeleteOrStatusChange} = useHandleDeleteOrStatusChange(updateStatusApi, handleDeleteApi, loadData);
const {form, rules, resetForm, handleSubmit} = useFormManagement(initialForm, initialRules, dictDataFormRef);
const {dialog, openDialog, closeDialog} = useDialogManagement();

// 方法
function openDictDataDialog(dictDataId?: number) {
  if (dictDataId) {
    openDialog("修改字典数据", () => {
      DictDataAPI.FORM.request(dictDataId).then(({data}) => {
        form.id = dictDataId;
        Object.assign(form, data);
      });
    });
  } else {
    openDialog("新增字典数据");
  }
}

/**
 * 关闭模态框
 */
function closeDictDataDialog() {
  closeDialog(() => resetForm())
}

function submitForm() {
  handleSubmit((form) => {
    const dictDataId = form.id;
    // 根据 userId 判断新增还是修改 (注 : 操作完成重置查询)
    if (dictDataId) {
      DictDataAPI.UPDATE.request(dictDataId, form).then(() => {
        ElMessage.success("修改成功");
        closeDictDataDialog();
        resetQuery();
      });
    } else {
      DictDataAPI.SAVE.request(form).then(() => {
        ElMessage.success("新增成功");
        closeDictDataDialog();
        resetQuery();
      });
    }
  })
}

// 生命周期
onMounted(() => {
  DictTypeAPI.FORM.request(dictTypeId.value).then(({data}) => {
    // 正常情况
    // 1. 修改当前标签页的标题
    tagsViewStore.updateViewTitle(route.fullPath, "【" + data.name + "】字典");
    // 2. 加载数据
    loadData();
    // 3. 设置当前 dictTypeId
    form.dictTypeId = dictTypeId.value;
  }).catch(() => {
    // 异常情况
    // 1. 删除标签
    const tagView: TagView = {
      name: route.name as string,
      title: route.meta.title || "",
      path: route.path,
      fullPath: route.fullPath,
    }
    tagsViewStore.removeTagView(tagView);
    tagsViewStore.removeCachedView(tagView);
    // 2. 跳转 404
    router.push('/404');
  })
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
