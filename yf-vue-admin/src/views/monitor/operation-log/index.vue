<template>
  <div class="app-container">
    <!--  搜索框  -->
    <div class="search-container">
      <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
        <el-form-item label="模块标题 :" prop="title">
          <el-input
              v-model="query.title"
              clearable
              placeholder="模块标题"
              @keyup.enter="loadData"
          />
        </el-form-item>
        <el-form-item label="业务类型 :" prop="businessType">
          <el-select v-model="query.businessType" clearable placeholder="业务类型选择">
            <el-option v-for="(value,key) in businessDictOptions" :label="value" :value="Number(key)"/>
          </el-select>
        </el-form-item>

        <el-form-item label="客户端 :" prop="operatorType">
          <el-select v-model="query.operatorType" clearable placeholder="对接客户端选择">
            <el-option v-for="(value,key) in operatorTypeDictOptions" :label="value" :value="Number(key)"/>
          </el-select>
        </el-form-item>

        <el-form-item label="方法名 :" prop="method">
          <el-input
              v-model="query.method"
              clearable
              placeholder="操作方法名"
              @keyup.enter="loadData"
          />
        </el-form-item>

        <el-form-item label="操作人 :" prop="method">
          <el-input
              v-model="query.operatorName"
              clearable
              placeholder="操作人用户名搜索"
              @keyup.enter="loadData"
          />
        </el-form-item>

        <el-form-item label="请求路径 :" prop="operatorUrl">
          <el-input
              v-model="query.operatorUrl"
              clearable
              placeholder="请求路径搜索"
              @keyup.enter="loadData"
          />
        </el-form-item>

        <el-form-item label="请求ip :" prop="operatorIp">
          <el-input
              v-model="query.operatorIp"
              clearable
              placeholder="请求ip精确搜索"
              @keyup.enter="loadData"
          />
        </el-form-item>

        <el-form-item label="响应状态 :" prop="status">
          <el-select
              v-model="query.status"
              clearable
              placeholder="响应状态搜索"
          >
            <el-option v-for="(value,key) in OperatorLogStatusEnum.OPTIONS" :label="value" :value="Number(key)"/>
          </el-select>
        </el-form-item>

        <el-form-item label="接口耗时 :" prop="operatorIp">
          <el-input
              v-model="query.costTime"
              clearable
              placeholder="毫秒,搜索大于您输入的耗时"
              @keyup.enter="loadData"
          />
        </el-form-item>

        <el-form-item label="开始时间 :" prop="startTime">
          <el-date-picker
              v-model="query.startTime"
              format="YYYY/MM/DD HH:mm:ss"
              placeholder="操作开始时间"
              type="datetime"
              value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>

        <el-form-item label="截止时间 :" prop="endTime">
          <el-date-picker
              v-model="query.endTime"
              format="YYYY/MM/DD HH:mm:ss"
              placeholder="操作截止时间"
              type="datetime"
              value-format="YYYY-MM-DD HH:mm:ss"
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

    <!--  主体 ( 左侧操作框 / 右侧操作框 )  -->
    <el-card class="table-container">
      <template #header>
        <div>
          <el-button v-permission="[OperationLogAPI.DELETE.permission]"
                     :circle="device === DeviceEnum.MOBILE"
                     :disabled="logIds.length === 0"
                     plain
                     type="danger"
                     @click="handleDeleteOrStatusChange(logIds)">
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
          v-loading="loading"
          :data="dataList"
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
            <el-tag>{{ businessDictOptions[scope.row.businessType] }}</el-tag>
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
            <el-tag>{{ operatorTypeDictOptions[scope.row.operatorType] }}</el-tag>
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
                @click.stop="handleDeleteOrStatusChange([scope.row.id],scope.row.title)"
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
                      :total="total" @handle-page-change="loadData"/>
        </el-scrollbar>
      </template>
    </el-card>
    <!--  模态框  -->
    <el-dialog
        v-model="dialog.visible"
        :title="dialog.title"
        :width="device === DeviceEnum.MOBILE ? '92%' : '74%'"
        draggable
        overflow
        top="40px"
        @close="closeDialog"
    >
      <div class="operation-log-detail">
        <el-descriptions :column="4" border>
          <el-descriptions-item :min-width="88">
            <template #label>
              请求方式:
            </template>
            {{ operationLogDetail.requestMethod }}
          </el-descriptions-item>
          <el-descriptions-item :span="3">
            <template #label>
              请求路径:
            </template>
            {{ operationLogDetail.operatorUrl }}
          </el-descriptions-item>
          <el-descriptions-item :span="2">
            <template #label>
              操作模块:
            </template>
            {{ businessDictOptions[operationLogDetail.businessType || 0] }} / {{ operationLogDetail.title }}
          </el-descriptions-item>
          <el-descriptions-item :span="2">
            <template #label>
              操作信息:
            </template>
            {{ operationLogDetail.operatorName }} /
            {{ operationLogDetail.operatorIp }} /
            {{ operationLogDetail.operatorLocation }} /
            {{ operationLogDetail.operatorBrowser }} /
            {{ operationLogDetail.operatorOs }}
          </el-descriptions-item>
          <el-descriptions-item :span="4">
            <template #label>
              操作方法:
            </template>
            {{ operationLogDetail.method }}
          </el-descriptions-item>
          <el-descriptions-item :span="4">
            <template #label>
              请求参数:
            </template>
            {{ operationLogDetail.operatorParam }}
          </el-descriptions-item>
          <el-descriptions-item :span="4">
            <template #label>
              响应结果:
            </template>
            {{ operationLogDetail.jsonResult }}
          </el-descriptions-item>
          <el-descriptions-item :span="4">
            <template #label>
              异常结果:
            </template>
            {{ operationLogDetail.errorMsg }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              操作状态:
            </template>
            <el-tag :type="OperatorLogStatusEnum.TAG_STYLE[(operationLogDetail.status || 0) % 2]">
              {{ OperatorLogStatusEnum.OPTIONS[operationLogDetail.status || 0] }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              操作耗时:
            </template>
            <el-text :type="(operationLogDetail.costTime || 0) > 1000 ? 'danger' : 'success' ">{{
                operationLogDetail.costTime + 'ms'
              }}
            </el-text>
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              操作时间:
            </template>
            {{ operationLogDetail.createTime }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              对接客户端:
            </template>
            <el-tag>{{ operatorTypeDictOptions[operationLogDetail.operatorType || 0] }}</el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <el-button @click="closeDialog()"> 关 闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import {FormInstance, TableInstance} from "element-plus";
import {usePageDataLoader} from "@/hooks/useDataLoader";
import {useTableManagement} from "@/hooks/useTableManagement";
import {OperationLogAPI} from "@/api/monitor/operation-log";
import {OperationLogQuery, OperationLogVO} from "@/api/monitor/operation-log/type";
import {OperatorLogStatusEnum} from "@/constants/system";
import {useDictionary} from "@/hooks/userDict";
import {DictType} from "@/api/system/dict/data/type";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {useSystemStore} from "@/store/modules/system";
import {useDialogManagement} from "@/hooks/useDialogManagement";
import {useHandleDeleteOrStatusChange} from "@/hooks/handleDeleteOrStatusChange";

defineOptions({
  name: "OperationLog",
  inheritAttrs: false,
});
// 数据
const operationLogDetail: OperationLogVO = reactive({});
const queryFormRef = ref<FormInstance | null>(null);          // 查询表单
const dataTableRef = ref<TableInstance | null>(null);         // 数据Table
const businessDictOptions = await useDictionary(DictType.BUSINESS)                // 日志业务类型字典数据
const operatorTypeDictOptions = await useDictionary(DictType.OPERATOR_TYPE)       // 日志操作类型字典数据
const device = computed(() => useSystemStore().app.device)            // 设备类型

const initialQuery: OperationLogQuery = {
  pageNum: 1,
  pageSize: 10,
  startTime: undefined,
}


const pageQueryApi = (query: OperationLogQuery) => OperationLogAPI.PAGE.request(query)  // hooks => 查询API
const handleDeleteApi = (ids: string) => OperationLogAPI.DELETE.request(ids)        // hooks => 删除API
const {selectedIds: logIds, handleCellDblclick, handleSelectionChange} = useTableManagement<string>(dataTableRef);
const {
  dataList,
  total,
  query,
  loading,
  resetQuery,
  loadData
} = usePageDataLoader(pageQueryApi, initialQuery, queryFormRef);
const {dialog, openDialog, closeDialog} = useDialogManagement();

const {handleDeleteOrStatusChange} = useHandleDeleteOrStatusChange(undefined, handleDeleteApi, loadData);

// 方法
function openOperationLogDialog(operationLogVO: OperationLogVO) {
  openDialog("操作日志详情信息", () => {
    Object.assign(operationLogDetail, operationLogVO)
  })
}

// 生命周期
onMounted(() => {
  loadData();
})
</script>

<style lang="scss" scoped>
/* 样式 */
.operation-log-detail {
  width: 100%;
  overflow: auto;
}
</style>
