<template>
  <el-card class="table-container">
    <!--  顶部操作    -->
    <template #header>
      <div>
        <el-button v-permission="[GenerateCrudAPI.IMPORT_DB_TABLE.permission]"
                   :circle="device === DeviceEnum.MOBILE"
                   plain
                   type="success"
                   @click="openImportDialog()">
          <el-icon>
            <plus/>
          </el-icon>
          <span v-show="device !== DeviceEnum.MOBILE"> 导入现有数据库 </span>
        </el-button>
        <el-button v-permission="[GenerateCrudAPI.TABLE_UPDATE.permission]"
                   :circle="device === DeviceEnum.MOBILE"
                   :disabled="dataIds.length !== 1"
                   plain
                   type="warning"
                   @click="jumpManagePage(dataIds[0])">
          <el-icon>
            <edit/>
          </el-icon>
          <span v-show="device !== DeviceEnum.MOBILE"> 修改 </span>
        </el-button>
        <el-button v-permission="[GenerateCrudAPI.TABLE_DELETE.permission]"
                   :circle="device === DeviceEnum.MOBILE"
                   :disabled="dataIds.length === 0"
                   plain
                   type="danger"
                   @click="deleteData(dataIds, undefined, props.loadData)">
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
      <el-table-column align="center" label="所属菜单" min-width="120" prop="menuName">
        <template #default="scope">
          <el-tag v-if="scope.row.menuName && scope.row.menuName  !== ''">
            {{ scope.row.menuName }}
          </el-tag>
          <el-tag v-else type="warning">
            未配置菜单
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="作者" min-width="120" prop="functionAuthor"/>
      <el-table-column align="center" label="数据库表名" min-width="120" prop="tableName"/>
      <el-table-column align="center" label="数据库表描述" min-width="120" prop="tableComment"/>
      <el-table-column align="center" label="类名" min-width="120" prop="className"/>
      <el-table-column align="center" label="组件名" min-width="120" prop="componentName"/>
      <el-table-column align="center" label="后端生成类型" min-width="180" prop="backEndType"/>
      <el-table-column align="center" label="前端生成类型" min-width="180" prop="frontEndType"/>
      <el-table-column align="center" label="主包名" min-width="120" prop="packageName"/>
      <el-table-column align="center" label="模块名" min-width="120" prop="moduleName"/>
      <el-table-column align="center" label="业务名" min-width="120" prop="businessName"/>
      <el-table-column align="center" label="备注" min-width="120" prop="remark"/>
      <el-table-column align="center" label="修改时间" min-width="180" prop="updateTime"/>
      <el-table-column v-permission="[GenerateCrudAPI.TABLE_UPDATE.permission, GenerateCrudAPI.TABLE_DELETE.permission]"
                       :fixed="device === DeviceEnum.MOBILE ? false : 'right'" align="center" label="操作"
                       width="200">
        <template #default="scope">
          <el-button
              v-permission="[GenerateCrudAPI.EXPORT_CRUD_CODE.permission]"
              link
              size="small"
              type="success"
              @click="genZip(scope.row.id)"
          >
            <el-icon>
              <Download/>
            </el-icon>
            生成代码
          </el-button
          >
          <el-button
              v-permission="[GenerateCrudAPI.TABLE_UPDATE.permission]"
              link
              size="small"
              type="warning"
              @click="jumpManagePage(scope.row.id)"
          >
            <el-icon>
              <edit/>
            </el-icon>
            编辑
          </el-button
          >
          <el-button
              v-permission="[GenerateCrudAPI.TABLE_DELETE.permission]"
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
                    :total="props.total" @handle-page-change="props.loadData()"/>
      </el-scrollbar>
    </template>
  </el-card>

  <!-- 导入框 -->
  <crud-import-dialog
      v-if="importDialogVisible"
      v-model:visible="importDialogVisible"
      :close-dialog="closeImportDialog"
      :device="device"
      :load-data="props.loadData"
  />
</template>

<script lang="ts" setup>
import {useDialogManage} from "@/hooks/useDialogManage";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {GenerateCrudAPI} from "@/api/generate/crud-code";
import {GenCrudTablePageQuery, GenCrudTableVO, GenTableForm} from "@/api/generate/crud-code/type";
import {useCrudActions} from "@/hooks/useCrudActions";
import {useSystemStore} from "@/store/modules/system";
import {TableInstance} from "element-plus";
import {useTableManagement} from "@/hooks/useTableManagement";

// 组件定义
defineOptions({
  name: "CrudTable",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  query: GenCrudTablePageQuery;
  dataList: GenCrudTableVO[];
  total: number;
  loading: boolean;
  loadData: (callback?: () => void) => Promise<void>;   // 加载数据函数
}>(), {});
const emits = defineEmits<{
  (event: "update:query", query: GenCrudTablePageQuery): void
}>()
// hooks
const query = useVModel(props, 'query', emits)
const {
  visible: importDialogVisible,
  openDialog: openImportDialog,
  closeDialog
} = useDialogManage();
const {
  deleteData
} = useCrudActions<number, GenTableForm>(undefined, undefined, GenerateCrudAPI.TABLE_DELETE.request, undefined);
// 数据
const router = useRouter();
const device = computed(() => useSystemStore().app.device)            // 设备类型
const dataTableRef = ref<TableInstance | null>(null);                 // 数据Table
const {selectedIds: dataIds, handleCellDblclick, handleSelectionChange} = useTableManagement<number>(dataTableRef);

// 方法
/**
 * 打开数据ID模态框
 * @param dataId 数据ID
 */
function jumpManagePage(dataId: number) {
  router.push("/generate/crud-manage/" + dataId);
}

function closeImportDialog() {
  closeDialog(() => {
    props.loadData()
  });
}

/**
 * 生成代码
 */
async function genZip(tableId: number) {
  await GenerateCrudAPI.EXPORT_CRUD_CODE.request(tableId).then((response) => {
    const fileName = decodeURI(
        response.headers["content-disposition"].split(";")[1].split("=")[1]
    );

    const blob = new Blob([response.data], {type: "application/zip"});
    const a = document.createElement("a");
    const url = window.URL.createObjectURL(blob);
    a.href = url;
    a.download = fileName;
    a.click();
    window.URL.revokeObjectURL(url);
  })
}

// 生命周期
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
