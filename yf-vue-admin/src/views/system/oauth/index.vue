<template>
  <div class="app-container">
    <!--  搜索框  -->
    <div class="search-container">
      <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
        <el-form-item label="用户名 :" prop="username">
          <el-input
              v-model="query.username"
              clearable
              placeholder="用户名"
              @keyup.enter="loadData"
          />
        </el-form-item>
        <el-form-item label="平台名 :" prop="platformName">
          <el-input
              v-model="query.platformName"
              clearable
              placeholder="平台名"
              @keyup.enter="loadData"
          />
        </el-form-item>
        <el-form-item label="创建时间 :" prop="startTime">
          <el-date-picker
              v-model="query.startTime"
              format="YYYY/MM/DD hh:mm:ss"
              placeholder="创建时间"
              type="datetime"
              value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>

        <el-form-item label="截至时间 :" prop="endTime">
          <el-date-picker
              v-model="query.endTime"
              format="YYYY/MM/DD hh:mm:ss"
              placeholder="创建时间"
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
          <el-button v-permission="[OauthAPI.DELETE.permission]"
                     :disabled="oauthIds.length === 0"
                     plain
                     type="danger"
                     @click="handleDelete(oauthIds)">
            <el-icon>
              <Link/>
            </el-icon>
            <span> 批量解绑 </span>
          </el-button>
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
                @click="handleDelete([scope.row.id])"
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
                      :total="total" @handle-page-change="loadData"/>
        </el-scrollbar>
      </template>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import {OauthPageQuery} from "@/api/system/oauth/type";
import {FormInstance, TableInstance} from "element-plus";
import {OauthAPI} from "@/api/system/oauth";
import {usePageDataLoader} from "@/hooks/useDataLoader";
import {useSystemStore} from "@/store/modules/system";
import {useTableManagement} from "@/hooks/useTableManagement";
import {UserAPI} from "@/api/system/user";

defineOptions({
  name: "Oauth",
  inheritAttrs: false,
});
// 数据
const queryFormRef = ref<FormInstance | null>(null);          // 查询表单
const dataTableRef = ref<TableInstance | null>(null);         // 数据Table
const device = computed(() => useSystemStore().app.device)    // 设备类型

const initialQuery: OauthPageQuery = {
  pageNum: 1,
  pageSize: 10
}

const pageQueryApi = (query: OauthPageQuery) => OauthAPI.PAGE.request(query)  // hooks => 查询API

const {selectedIds: oauthIds, handleCellDblclick, handleSelectionChange} = useTableManagement<string>(dataTableRef);
const {
  dataList,
  total,
  query,
  loading,
  resetQuery,
  loadData
} = usePageDataLoader(pageQueryApi, initialQuery, queryFormRef);

// 方法
async function handleDelete(oauthIds: string[]) {
  // 1. 二次确认删除
  await ElMessageBox.confirm("是否确认解绑用户授权信息", '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    draggable: true
  });
  // 2. 确认后执行删除API
  OauthAPI.DELETE.request(oauthIds.join(',')).then(() => {
    ElMessage.success("解绑授权信息成功");
    loadData();
  });
}

// 生命周期
onMounted(() => {
  loadData();
})
</script>

<style lang="scss" scoped>
/* 样式 */
.search-container .el-input {
  --el-input-width: 120px;
}

.user-oauth__detail {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
}
</style>
