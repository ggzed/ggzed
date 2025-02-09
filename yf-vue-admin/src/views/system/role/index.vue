<template>
  <div class="app-container">
    <!--  搜索框  -->
    <role-search
        v-model:query="query"
        :load-data="loadData"
        :reset-query="resetQuery"
    />
    <!--  表格  -->
    <role-table
        v-model:query="query"
        :data-list="dataList"
        :data-scope-dict="dataScopeDict"
        :load-data="loadData"
        :loading="loading"
        :total="total"
    />
  </div>
</template>

<script lang="ts" setup>
import {useDataLoader} from "@/hooks/useDataLoader";
import {RolePageQuery, RolePageVO} from "@/api/system/role/type";
import {RoleAPI} from "@/api/system/role";
import {useDictionary} from "@/hooks/userDict";
import {DictType} from "@/api/system/dict-data/type";

// 组件定义
defineOptions({
  name: "Role",
  inheritAttrs: false,
});
// 公共数据 & 方法
const initialQuery: RolePageQuery = {
  keywords: undefined,
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
} = useDataLoader<RolePageVO, RolePageQuery>(RoleAPI.PAGE.request, initialQuery);
const dataScopeDict: Record<number | string, string> = await useDictionary(DictType.DATA_PERMISSION)  // 数据权限字典数据
// 数据

// 方法

// 生命周期
onMounted(async () => {
  await loadData()
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
