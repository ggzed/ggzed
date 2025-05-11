<template>
  <el-dialog
      v-model="visible"
      :title="props.title"
      :width="props.device === DeviceEnum.MOBILE ? '90%' : '50%'"
      destroy-on-close
      draggable
      overflow
      @close="props.closeDialog"
  >
    <div class="app-container">
      <!--  搜索框    -->
      <div class="search-container">
        <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
          <el-form-item label="表名 :" prop="tableName">
            <el-input
                v-model="query.tableName"
                clearable
                placeholder="数据库表名"
                @keyup.enter="props.loadData()"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="loadData()">
              <template #icon>
                <search/>
              </template>
              搜索
            </el-button>
            <el-button plain type="primary" @click="resetQuery()">
              <template #icon>
                <refresh/>
              </template>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <!--  展示表单    -->
      <el-card class="table-container">
        <el-table
            ref="dataTableRef"
            v-loading="loading"
            :data="dataList"
            border
            highlight-current-row
            row-key="tableName"
            @selection-change="handleSelectionChange"
            @cell-dblclick="handleCellDblclick"
        >
          <el-table-column align="center" type="selection" width="50"/>
          <el-table-column align="center" label="数据库表名" min-width="120" prop="tableName"/>
          <el-table-column align="center" label="数据库字段" min-width="120" prop="tableComment"/>
          <el-table-column align="center" label="engine" min-width="120" prop="engine"/>
          <el-table-column align="center" label="数据量" min-width="120" prop="tableRows"/>
          <el-table-column align="center" label="创建时间" min-width="180" prop="createTime"/>
          <el-table-column align="center" label="修改时间" min-width="180" prop="updateTime"/>
        </el-table>
        <!--  底部分页    -->
        <template #footer>
          <el-scrollbar>
            <Pagination v-model:current-page="query.pageNum"
                        v-model:page-size="query.pageSize"
                        :page-sizes="[5, 10, 20, 30, 50, 100]"
                        :total="total" @handle-page-change="loadData()"/>
          </el-scrollbar>
        </template>
      </el-card>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submitForm">导 入</el-button>
        <el-button @click="props.closeDialog()">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import {DeviceEnum} from "@/enums/DeviceEnum";
import {useDataLoader} from "@/hooks/useDataLoader";
import {DBTablePageQuery, DBTableVO} from "@/api/generate/crud-code/type";
import {GenerateCrudAPI} from "@/api/generate/crud-code";
import {TableInstance} from "element-plus";
import {useTableManagement} from "@/hooks/useTableManagement";

// 组件定义
defineOptions({
  name: "CrudImportDialog",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  // dialog-visible
  visible: boolean;
  // 设备
  device?: DeviceEnum;
  // 加载数据
  loadData: (callback?: () => void) => Promise<void>;
  // 关闭弹窗
  closeDialog: (callback?: () => void) => void;
}>(), {device: DeviceEnum.DESKTOP});

const emits = defineEmits<{
  (event: "update:visible", visible: boolean): void
}>()

// hooks
const visible = useVModel(props, 'visible', emits)
const initialQuery: DBTablePageQuery = {
  tableName: undefined,
  pageNum: 1,
  pageSize: 5,
}                       // 初始化查询条件
const {
  query,
  dataList,
  total,
  loading,
  loadData,
  resetQuery
} = useDataLoader<DBTableVO, DBTablePageQuery>(GenerateCrudAPI.DB_PAGE.request, initialQuery);
const dataTableRef = ref<TableInstance | null>(null);                 // 数据Table
const {
  selectedIds: selectedTableNames,
  handleCellDblclick,
  handleSelectionChange
} = useTableManagement<string>(dataTableRef, "tableName");

// 方法
function submitForm() {
  // 提交表单
  if (selectedTableNames.value.length === 0) {
    ElMessage.warning("请选择要导入的表");
    return;
  }
  GenerateCrudAPI.IMPORT_DB_TABLE.request(selectedTableNames.value).then(() => {
    ElMessage.success("导入成功");
    props.closeDialog();
  })
}

onMounted(() => {
  loadData();
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
