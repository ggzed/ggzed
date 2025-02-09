<template>
  <div class="app-container">
    <!--  搜索框  -->
    <oauth-search
        v-model:query="query"
        :load-data="loadData"
        :reset-query="resetQuery"
    />
    <!--  Table  -->
    <oauth-table
        v-model:query="query"
        :data-list="dataList"
        :load-data="loadData"
        :loading="loading"
        :total="total"
    />
  </div>
</template>

<script lang="ts" setup>
import {OauthAPI} from "@/api/system/oauth";
import {OauthPageQuery, OauthPageVO} from "@/api/system/oauth/type";
import {useDataLoader} from "@/hooks/useDataLoader";
// 组件定义
defineOptions({
  name: "Oauth",
  inheritAttrs: false,
});
// 公共数据 & 方法
const initialQuery: OauthPageQuery = {
  username: undefined,
  platformName: undefined,
  startTime: undefined,
  endTime: undefined,
  pageNum: 1,
  pageSize: 10
}                       // 初始化查询条件
const {
  query,
  dataList,
  total,
  loading,
  loadData,
  resetQuery
} = useDataLoader<OauthPageVO, OauthPageQuery>(OauthAPI.PAGE.request, initialQuery);

// 生命周期
onMounted(async () => {
  await loadData()
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
