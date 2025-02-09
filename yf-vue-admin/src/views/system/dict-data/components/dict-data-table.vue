<template>
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
                   @click="deleteData(dictDataIds, null, props.loadData)">
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
                  @click="updateDataStatus([scope.row.id],scope.row.name,!scope.row.status,props.loadData)">
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
                    :total="props.total" @handle-page-change="props.loadData"/>
      </el-scrollbar>
    </template>
  </el-card>

  <!-- 交互模态框 -->
  <dict-data-manage-dialog
      v-if="visible"
      v-model:visible="visible"
      :close-dialog="closeDialog"
      :current-click-row-id="currentClickRowId"
      :device="device"
      :dict-type-id="props.dictTypeId"
      :load-data="props.loadData"
      :title="title"
  />
</template>

<script lang="ts" setup>
import {DefaultedStatusEnum, EnableStatusEnum} from "@/constants/system";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {DictDataAPI} from "@/api/system/dict-data";
import {DictDataForm, DictDataPageQuery, DictDataPageVO} from "@/api/system/dict-data/type";
import {useDialogManage} from "@/hooks/useDialogManage";
import {useCrudActions} from "@/hooks/useCrudActions";
import {useSystemStore} from "@/store/modules/system";
import {useTableManagement} from "@/hooks/useTableManagement";
import {TableInstance} from "element-plus";
// 组件定义

defineOptions({
  name: "DeptDataTable",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  query: DictDataPageQuery;
  dictTypeId: number;
  dataList: DictDataPageVO[];
  total: number;
  loading: boolean;
  loadData: (callback?: () => void) => Promise<void>;   // 加载数据函数
}>(), {});
const emits = defineEmits<{
  (event: "update:query", query: DictDataPageQuery): void
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
} = useCrudActions<DictDataForm>(DictDataAPI.SAVE.request, DictDataAPI.UPDATE.request, DictDataAPI.DELETE.request, DictDataAPI.UPDATE_STATUS.request);
// 数据
const device = computed(() => useSystemStore().app.device)            // 设备类型
const dataTableRef = ref<TableInstance | null>(null);                 // 数据Table
const currentClickRowId = ref<number | undefined>();                  // 打开 dialog 点击的 row
const {selectedIds: dictDataIds, handleCellDblclick, handleSelectionChange} = useTableManagement<number>(dataTableRef);

// 方法
/**
 * 打开字典数据模态框
 * @param dictDataId
 */
function openDictDataDialog(dictDataId?: number) {
  currentClickRowId.value = dictDataId;
  if (dictDataId) {
    openDialog("修改字典数据");
  } else {
    openDialog("新增字典数据");
  }
}
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
