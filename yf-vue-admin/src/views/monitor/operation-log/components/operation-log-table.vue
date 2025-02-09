<template>
  <el-card class="table-container">
    <template #header>
      <div>
        <el-button v-permission="[OperationLogAPI.DELETE.permission]"
                   :circle="device === DeviceEnum.MOBILE"
                   :disabled="logIds.length === 0"
                   plain
                   type="danger"
                   @click="deleteData(logIds,'',onSuccess)">
          <el-icon>
            <delete/>
          </el-icon>
          <span v-show="device !== DeviceEnum.MOBILE"> 删除 </span>
        </el-button>
      </div>
      <div></div>
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
      <el-table-column align="center" label="模块标题" min-width="120" prop="title"/>
      <el-table-column align="center" label="操作人员" min-width="140" prop="operatorName"/>
      <el-table-column align="center" label="业务类型" min-width="100" prop="businessType">
        <template #default="scope">
          <el-tag>{{ props.businessDict[scope.row.businessType] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作状态" min-width="100" prop="status">
        <template #default="scope">
          <el-tag v-show="scope.row.status !== null"
                  :type="OperatorLogStatusEnum.TAG_STYLE[scope.row.status % 2]">
            {{ OperatorLogStatusEnum.OPTIONS[scope.row.status] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="对接客户端" min-width="100" prop="operatorType">
        <template #default="scope">
          <el-tag>{{ props.operatorTypeDict[scope.row.operatorType] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作ip" min-width="100" prop="operatorIp"/>
      <el-table-column align="center" label="操作地点" min-width="100" prop="operatorLocation"/>
      <el-table-column align="center" label="操作浏览器" min-width="100" prop="operatorBrowser"/>
      <el-table-column align="center" label="操作系统" min-width="100" prop="operatorOs"/>
      <el-table-column align="center" label="消耗时间" min-width="86" prop="costTime">
        <template #default="scope">
          <el-text :type=" scope.row.costTime > 1000 ? 'danger' : 'success' ">{{
              scope.row.costTime + 'ms'
            }}
          </el-text>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作时间" min-width="120" prop="createTime"/>
      <el-table-column :fixed="device === DeviceEnum.MOBILE ? false : 'right'" align="center" label="操作"
                       width="180">
        <template #default="scope">
          <el-button
              v-permission="[OperationLogAPI.DELETE.permission]"
              link
              size="small"
              type="danger"
              @click.stop="deleteData([scope.row.id],scope.row.title,onSuccess)"
          >
            <el-icon>
              <delete/>
            </el-icon>
            删除
          </el-button>
          <el-button
              link
              size="small"
              type="primary"
              @click="openOperationLogDialog(scope.row)"
          >
            <el-icon>
              <View/>
            </el-icon>
            详情信息
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <template #footer>
      <el-scrollbar>
        <Pagination v-model:current-page="query.pageNum"
                    v-model:page-size="query.pageSize"
                    :total="props.total" @handle-page-change="props.loadData"/>
      </el-scrollbar>
    </template>
  </el-card>
  <!-- 操作日志详情模态框 -->
  <operation-log-detail-dialog
      v-model:visible="visible"
      :business-dict="props.businessDict"
      :device="device"
      :operation-log-detail="operationLogDetail"
      :operator-type-dict="props.operatorTypeDict"
      :title="title"
  />
</template>

<script lang="ts" setup>
import {OperationLogAPI} from "@/api/monitor/operation-log";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {OperatorLogStatusEnum} from "@/constants/system";
import {OperationLogPageQuery, OperationLogVO} from "@/api/monitor/operation-log/type";
import {useDialogManage} from "@/hooks/useDialogManage";
import {TableInstance} from "element-plus";
import {useTableManagement} from "@/hooks/useTableManagement";
import {useCrudActions} from "@/hooks/useCrudActions";
import {DeptForm} from "@/api/system/dept/type";
import {DeptAPI} from "@/api/system/dept";
import {useSystemStore} from "@/store/modules/system";
// 组件定义
defineOptions({
  name: "OperationLogTable",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  query: OperationLogPageQuery;
  dataList: OperationLogVO[];
  businessDict: Record<number | string, string>;         // 日志业务类型字典数据
  operatorTypeDict: Record<number | string, string>;     // 日志操作类型字典数据
  total: number;
  loading: boolean;
  loadData: (callback?: () => void) => Promise<void>;   // 加载数据函数
}>(), {});
const emits = defineEmits<{
  (event: "update:query", query: OperationLogPageQuery): void
}>()
// hooks
const query = useVModel(props, 'query', emits)
const {
  visible,
  title,
  openDialog,
  closeDialog
} = useDialogManage();
const dataTableRef = ref<TableInstance | null>(null);
const {
  deleteData
} = useCrudActions<DeptForm>(null, null, DeptAPI.DELETE.request, null);

const {selectedIds: logIds, handleCellDblclick, handleSelectionChange} = useTableManagement<number>(dataTableRef);

// 数据
const device = computed(() => useSystemStore().app.device)            // 设备类型
const operationLogDetail: OperationLogVO = reactive({});

// 方法
/**
 * 打开操作日志模态框
 * @param operationLogVO
 */
function openOperationLogDialog(operationLogVO: OperationLogVO) {
  openDialog("操作日志详情信息", () => {
    Object.assign(operationLogDetail, operationLogVO)
  })
}

/**
 * 操作成功
 */
async function onSuccess() {
  await props.loadData()
}
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
