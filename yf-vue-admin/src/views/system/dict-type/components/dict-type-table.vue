<template>
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
                   @click="deleteData(dictTypeIds,undefined, props.loadData)">
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
        v-loading="props.loading"
        :data="props.dataList"
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
                  @click="updateDataStatus(scope.row.id,scope.row.name,!scope.row.status,props.loadData)">
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
              v-permission="[DictTypeAPI.PAGE.permission]"
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
    <!--  底部分页    -->
    <template #footer>
      <el-scrollbar>
        <Pagination v-model:current-page="query.pageNum"
                    v-model:page-size="query.pageSize"
                    :total="total" @handle-page-change="props.loadData()"/>
      </el-scrollbar>
    </template>
  </el-card>

  <!-- 交互模态框 -->
  <dict-type-manage-dialog
      v-if="visible"
      v-model:visible="visible"
      :close-dialog="closeDialog"
      :current-click-row-id="currentClickRowId"
      :device="device"
      :load-data="props.loadData"
      :title="title"
  />
</template>

<script lang="ts" setup>
// 组件定义
import {DictTypeAPI} from "@/api/system/dict-type";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {EnableStatusEnum} from "@/constants/system";
import {DictTypeForm, DictTypePageQuery, DictTypePageVO} from "@/api/system/dict-type/type";
import {useTableManagement} from "@/hooks/useTableManagement";
import {TableInstance} from "element-plus";
import {useSystemStore} from "@/store/modules/system";
import {useCrudActions} from "@/hooks/useCrudActions";
import {useDialogManage} from "@/hooks/useDialogManage";

defineOptions({
  name: "DeptTypeTable",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  query: DictTypePageQuery;
  dataList: DictTypePageVO[];
  total: number;
  loading: boolean;
  loadData: (callback?: () => void) => Promise<void>;   // 加载数据函数
}>(), {});
const emits = defineEmits<{
  (event: "update:query", query: DictTypePageQuery): void
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
  saveData,
  updateData,
  deleteData,
  updateDataStatus
} = useCrudActions<number, DictTypeForm>(DictTypeAPI.SAVE.request, DictTypeAPI.UPDATE.request, DictTypeAPI.DELETE.request, DictTypeAPI.UPDATE_STATUS.request);
const dataTableRef = ref<TableInstance | null>(null);
const {selectedIds: dictTypeIds, handleCellDblclick, handleSelectionChange} = useTableManagement<number>(dataTableRef);

// 数据
const device = computed(() => useSystemStore().app.device)            // 设备类型
const router = useRouter();
const currentClickRowId = ref<number | undefined>();                  // 打开 dialog 点击的 row

// 方法
/**
 * 打开字典类型模态框
 * @param dictTypeId
 */
function openDictTypeDialog(dictTypeId?: number) {
  currentClickRowId.value = dictTypeId;
  if (dictTypeId) {
    openDialog("修改字典类型");
  } else {
    openDialog("新增字典类型");
  }
}

/**
 * 跳转到字典详情页
 * @param dictTypeId 字典类型 Id
 */
function goToDictDataPage(dictTypeId: number) {
  router.push("/system/dict-data/" + dictTypeId);
}
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
