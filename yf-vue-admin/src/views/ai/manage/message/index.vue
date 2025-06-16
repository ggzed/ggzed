<template>
  <div class="app-container">
    <!--  Search  -->
    <ai-message-search
        v-model:query="query"
        :dict-data="dictData"
        :load-data="loadAiMessageData"
        :reset-query="resetQuery"
    />
    <!--  Table  -->
    <ai-message-table
        v-model:query="query"
        :conversation-id="conversationId"
        :data-list="dataList"
        :dict-data="dictData"
        :load-data="loadAiMessageData"
        :loading="loading"
        :total="total"
    />
  </div>
</template>

<script lang="ts" setup>
import {AiMessagePageQuery, AiMessagePageVO} from "@/api/ai/message/type";
import {AiMessageAPI} from "@/api/ai/message";
import {useDataLoader} from "@/hooks/useDataLoader";
import {useDictionary} from "@/hooks/userDict";
import {TagView, useTagsViewStore} from "@/store/modules/tagsView";
import {RouteConstant} from "@/constants/route";
import {AiConversationAPI} from "@/api/ai/conversation";
// 组件定义
defineOptions({
  name: "AiMessage/:id",
  inheritAttrs: false,
});
// 公共数据 & 方法
const initialQuery: AiMessagePageQuery = {
  role: undefined,
  content: undefined,
  sendTimeStart: undefined,
  sendTimeEnd: undefined,
  pageNum: 1,
  pageSize: 10
}                       // 初始化查询条件
// hooks
const {
  query,
  dataList,
  total,
  loading
} = useDataLoader<AiMessagePageVO, AiMessagePageQuery>(AiMessageAPI.PAGE.request, initialQuery);
const dictData = await useDictionary(['ai_role']); // 字典数据
// 数据
const route = useRoute();
const router = useRouter();
const conversationId = computed(() => route.params.id.toString() || "-1")        // 会话ID
const tagsViewStore = useTagsViewStore();                             // 页签

// 方法
async function loadAiMessageData(callback?: () => void) {
  loading.value = true;
  try {
    await AiMessageAPI.PAGE.request(conversationId.value, query).then(({data}) => {
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
  await loadAiMessageData(callback);
}

// 生命周期
onMounted(async () => {
  await AiConversationAPI.FORM.request(conversationId.value).then(({data}) => {
    // 正常情况
    // 1. 修改当前标签页的标题
    const displayTitle = (data.title || '').length > 6
        ? `${(data.title || '').substring(0, 6)}...`
        : data.title || '';

    tagsViewStore.updateViewTitle(
        route.fullPath,
        `【${displayTitle}】聊天记录`
    );
    // 2. 加载数据
    loadAiMessageData();
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
