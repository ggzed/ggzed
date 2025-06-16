<template>
  <el-card class="table-container">
    <!--  顶部操作    -->
    <template #header>
      <div>
        <el-button v-permission="[AiConversationAPI.UPDATE.permission]"
                   :circle="device === DeviceEnum.MOBILE"
                   :disabled="ids.length !== 1"
                   plain
                   type="warning"
                   @click="openAiConversationDialog(ids[0])">
          <el-icon>
            <edit/>
          </el-icon>
          <span v-show="device !== DeviceEnum.MOBILE"> 修改 </span>
        </el-button>
        <el-button v-permission="[AiConversationAPI.DELETE.permission]"
                   :circle="device === DeviceEnum.MOBILE"
                   :disabled="ids.length === 0"
                   plain
                   type="danger"
                   @click="deleteData(ids, undefined, props.loadData)">
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
      <el-table-column align="center" label="会话标题" prop="title"/>
      <el-table-column align="center" label="会话活跃时间" prop="lastActiveTime"/>
      <el-table-column v-permission="[AiConversationAPI.UPDATE.permission, AiConversationAPI.DELETE.permission]"
                       :fixed="device === DeviceEnum.MOBILE ? false : 'right'" align="center" label="操作"
                       width="180">
        <template #default="scope">
          <el-button
              v-permission="[AiMessageAPI.PAGE.permission]"
              link
              size="small"
              type="warning"
              @click="goToAiMessagePage(scope.row.id)"
          >
            <el-icon>
              <Position/>
            </el-icon>
            AI 聊天记录
          </el-button>
          <el-button
              v-permission="[AiConversationAPI.UPDATE.permission]"
              link
              size="small"
              type="warning"
              @click="openAiConversationDialog(scope.row.id)"
          >
            <el-icon>
              <edit/>
            </el-icon>
            编辑
          </el-button
          >
          <el-button
              v-permission="[AiConversationAPI.DELETE.permission]"
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

  <!-- 交互模态框 -->
  <ai-conversation-manage-dialog
      v-if="visible"
      v-model:visible="visible"
      :close-dialog="closeDialog"
      :current-click-row-id="currentClickRowId"
      :device="device"
      :dict-data="props.dictData"
      :load-data="props.loadData"
      :title="title"
  />
</template>

<script lang="ts" setup>
import {AiConversationForm, AiConversationPageQuery, AiConversationPageVO} from "@/api/ai/conversation/type";
import {AiConversationAPI} from "@/api/ai/conversation";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {DictType} from "@/api/system/dict-data/type";
import {useDialogManage} from "@/hooks/useDialogManage";
import {useCrudActions} from "@/hooks/useCrudActions";
import {useSystemStore} from "@/store/modules/system";
import {useTableManagement} from "@/hooks/useTableManagement";
import {TableInstance} from "element-plus";
import {AiMessageAPI} from "@/api/ai/message";

// 组件定义
defineOptions({
  name: "AiConversationTable",
  inheritAttrs: false,
});

// 组件 props & emits
const props = withDefaults(defineProps<{
  query: AiConversationPageQuery;
  dataList: AiConversationPageVO[];
  dictData?: Record<DictType | string, Record<any, string>>;
  total: number;
  loading: boolean;
  loadData: (callback?: () => void) => Promise<void>;   // 加载数据函数
}>(), {dictData: () => ({})});

const emits = defineEmits<{
  (event: "update:query", query: AiConversationPageQuery): void
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
  deleteData
} = useCrudActions<string, AiConversationForm>(undefined, undefined, AiConversationAPI.DELETE.request, undefined);
// 数据
const device = computed(() => useSystemStore().app.device)            // 设备类型
const dataTableRef = ref<TableInstance | null>(null);                 // 数据Table
const currentClickRowId = ref<string | undefined>();                  // 打开 dialog 点击的 row
const {selectedIds: ids, handleCellDblclick, handleSelectionChange} = useTableManagement<string>(dataTableRef);

// 方法
/**
 * 打开AI 会话表数据模态框
 * @param id AiConversation主键
 */
function openAiConversationDialog(id?: string) {
  currentClickRowId.value = id;
  if (id) {
    openDialog("修改 AI 会话数据");
  }
}

/**
 * 跳转到 AI 聊天记录页面
 */
function goToAiMessagePage(id: string) {
  useRouter().push("/ai/manage/ai-message/" + id);
}
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
