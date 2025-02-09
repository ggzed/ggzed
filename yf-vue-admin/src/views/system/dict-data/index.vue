<template>
  <div class="app-container">
    <!--  搜索框  -->
    <dict-data-search
        v-model:query="query"
        :load-data="loadDictData"
        :reset-query="resetQuery"
    />
    <!--  Table  -->
    <dict-data-table
        v-model:query="query"
        :data-list="dataList"
        :dict-type-id="dictTypeId"
        :load-data="loadDictData"
        :loading="loading"
        :total="total"
    />
  </div>
</template>

<script lang="ts" setup>
import {DictDataAPI} from "@/api/system/dict-data";
import {DictDataPageQuery, DictDataPageVO} from "@/api/system/dict-data/type";
import {useDataLoader} from "@/hooks/useDataLoader";
import {DictTypeAPI} from "@/api/system/dict-type";
import {TagView, useTagsViewStore} from "@/store/modules/tagsView";
import {RouteConstant} from "@/constants/route";
// 组件定义
defineOptions({
  name: "DictData/:id",
  inheritAttrs: false,
});
// 公共数据 & 方法
const initialQuery: DictDataPageQuery = {
  name: undefined,
  status: undefined,
  defaulted: undefined,
  pageNum: 1,
  pageSize: 10
}                       // 初始化查询条件
const {
  query,
  dataList,
  total,
  loading
} = useDataLoader<DictDataPageVO, DictDataPageQuery>(DictDataAPI.PAGE.request, initialQuery);
// 数据
const route = useRoute();
const router = useRouter();
const dictTypeId = computed(() => Number(route.params.id || -1))  // 字典类型ID
const tagsViewStore = useTagsViewStore();                             // 页签

// 方法
async function loadDictData(callback?: () => void) {
  loading.value = true;
  try {
    await DictDataAPI.PAGE.request(dictTypeId.value, query).then(({data}) => {
      dataList.value = data.list;
      total.value = data.total;
      if (callback && typeof callback === 'function') {
        callback();
      }
    });
  } finally {
    loading.value = false;
  }
}

async function resetQuery(callback?: () => void) {
  // 重置查询条件
  Object.assign(query, {...initialQuery});
  // 加载数据
  await loadDictData(callback);
}

// 生命周期
onMounted(async () => {
  await DictTypeAPI.FORM.request(dictTypeId.value).then(({data}) => {
    // 正常情况
    // 1. 修改当前标签页的标题
    tagsViewStore.updateViewTitle(route.fullPath, "【" + data.name + "】字典");
    // 2. 加载数据
    loadDictData();
  }).catch(() => {
    // 异常情况
    // 1. 删除标签
    const tagView: TagView = {
      name: route.name as string,
      title: route.meta.title || "",
      path: route.path,
      fullPath: route.fullPath,
    }
    tagsViewStore.removeTagView(tagView);
    tagsViewStore.removeCachedView(tagView);
    // 2. 跳转 404
    router.push(RouteConstant.NOT_FOUND_PATH);
  })
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
