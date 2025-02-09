<template>
  <!--  主体 ( 左侧操作框 / 右侧操作框 )  -->
  <el-card class="table-container">
    <template #header>
      <div>
        <el-button v-permission="[OauthAPI.DELETE.permission]"
                   :disabled="oauthIds.length === 0"
                   plain
                   type="danger"
                   @click="deleteData(oauthIds,null,props.loadData)">
          <el-icon>
            <Link/>
          </el-icon>
          <span> 批量解绑 </span>
        </el-button>
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
      <el-table-column align="center" label="用户名" min-width="80" prop="username"/>
      <el-table-column align="center" label="平台名" min-width="80" prop="platformName"/>
      <el-table-column align="center" label="平台授权信息" min-width="180" prop="platformName">
        <template #default="scope">
          <div class="user-oauth__detail">
            <el-avatar :src="scope.row.platformUserAvatar" size="small"/>
            <span>{{ scope.row.platformUsername }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="绑定时间" min-width="120" prop="createTime"/>
      <el-table-column v-permission="[UserAPI.DELETE.permission]" align="center"
                       label="操作" width="120">
        <template #default="scope">
          <el-button
              v-permission="[UserAPI.DELETE.permission]"
              link
              size="small"
              type="danger"
              @click="deleteData([scope.row.id],scope.row.username,props.loadData)"
          >
            <el-icon>
              <delete/>
            </el-icon>
            解绑
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
</template>

<script lang="ts" setup>
import {OauthAPI} from "@/api/system/oauth";
import {UserAPI} from "@/api/system/user";
import {useCrudActions} from "@/hooks/useCrudActions";
import {OauthPageQuery, OauthPageVO} from "@/api/system/oauth/type";
import {TableInstance} from "element-plus";
import {useTableManagement} from "@/hooks/useTableManagement";
// 组件 props & emits
const props = withDefaults(defineProps<{
  query: OauthPageQuery;
  dataList: OauthPageVO[];
  total: number;
  loading: boolean;
  loadData: (callback?: () => void) => Promise<void>;   // 加载数据函数
}>(), {});
const emits = defineEmits<{
  (event: "update:query", query: OauthPageQuery): void
}>()
// hooks
const query = useVModel(props, 'query', emits)

const {
  deleteData,
} = useCrudActions(null, null, OauthAPI.DELETE.request, null);

const dataTableRef = ref<TableInstance | null>(null);                 // 数据Table
const {selectedIds: oauthIds, handleCellDblclick, handleSelectionChange} = useTableManagement<number>(dataTableRef);
</script>

<style lang="scss" scoped>
/* 样式 */
.user-oauth__detail {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
}
</style>
