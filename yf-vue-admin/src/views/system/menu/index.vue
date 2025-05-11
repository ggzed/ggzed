<template>
  <div class="app-container">
    <menu-search
        v-model:query="query"
        :load-data="loadData"
        :menu-dict="menuDict"
        :reset-query="resetQuery"
    />

    <menu-table
        :data-list="dataList"
        :load-data="loadData"
        :loading="loading"
        :menu-dict="menuDict"
    />
  </div>
</template>

<script lang="ts" setup>
import {useDataLoader} from "@/hooks/useDataLoader";
import {MenuAPI} from "@/api/system/menu";
import {MenuPageQuery, MenuPageVO} from "@/api/system/menu/type";
import {useDictionary} from "@/hooks/userDict";
import {DictType} from "@/api/system/dict-data/type";

// 组件定义
defineOptions({
  name: "Menu",
  inheritAttrs: false,
});
// 公共数据 & 方法
const initialQuery: MenuPageQuery = {
  keywords: undefined,
  hidden: undefined,
  type: undefined
}                       // 初始化查询条件
const {
  query,
  dataList,
  loading,
  loadData,
  resetQuery
} = useDataLoader<MenuPageVO, MenuPageQuery>(MenuAPI.PAGE.request, initialQuery);
const menuDict = await useDictionary([DictType.MENU])           // 菜单类型字典

// 生命周期
onMounted(async () => {
  await loadData()
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
