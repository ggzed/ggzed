<template>
  <div class="app-container">
    <!--  搜索框  -->
    <user-search
        v-model:query="query"
        :dept-options="deptOptions"
        :load-data="loadData"
        :load-dept-data="loadDeptData"
        :reset-query="resetQuery"
    />
    <!--  表格  -->
    <user-table
        v-model:query="query"
        :data-list="dataList"
        :dept-options="deptOptions"
        :gender-dict="genderDict"
        :load-data="loadData"
        :load-dept-data="loadDeptData"
        :load-role-data="loadRoleData"
        :loading="loading"
        :role-options="roleOptions"
        :total="total"
    />
  </div>
</template>

<script lang="ts" setup>
// 组件定义
import {UserPageQuery, UserPageVO} from "@/api/system/user/type";
import {useDataLoader} from "@/hooks/useDataLoader";
import {UserAPI} from "@/api/system/user";
import {DeptAPI} from "@/api/system/dept";
import {RoleAPI} from "@/api/system/role";
import {useDictionary} from "@/hooks/userDict";
import {DictType} from "@/api/system/dict-data/type";

defineOptions({
  name: "Role",
  inheritAttrs: false,
});
// 公共数据 & 方法
const initialQuery: UserPageQuery = {
  keywords: undefined,
  deptId: undefined,
  startTime: undefined,
  endTime: undefined,
  status: undefined,
  pageNum: 1,
  pageSize: 10
}                       // 初始化查询条件
// 用户数据
const {
  query,
  dataList,
  total,
  loading,
  loadData,
  resetQuery
} = useDataLoader<UserPageVO, UserPageQuery>(UserAPI.PAGE.request, initialQuery);
// 部门数据
const {
  dataList: deptOptions,
  loadData: loadDeptData
} = useDataLoader<OptionType, object>(DeptAPI.OPTIONS.request, {});
// 角色数据
const {
  dataList: roleOptions,
  loadData: loadRoleData
} = useDataLoader<OptionType, object>(RoleAPI.OPTIONS.request, {});
// 性别字典
const genderDict = await useDictionary(DictType.GENDER)        // 性别字典数据
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
