<template>
    <div class="app-container">
        <!--  Search  -->
        <ai-conversation-search
            v-model:query="query"
            :load-data="loadData"
            :reset-query="resetQuery"
        />
        <!--  Table  -->
        <ai-conversation-table
            v-model:query="query"
            :data-list="dataList"
            :load-data="loadData"
            :loading="loading"
            :total="total"
        />
    </div>
</template>

<script lang="ts" setup>
import {
        AiConversationPageQuery,
        AiConversationPageVO
    } from "@/api/ai/conversation/type";
import {AiConversationAPI} from "@/api/ai/conversation";
import {useDataLoader} from "@/hooks/useDataLoader";

// 组件定义
defineOptions({
    name: "AiConversation",
    inheritAttrs: false,
});
// 公共数据 & 方法
const initialQuery: AiConversationPageQuery = {
    title: undefined,
    lastActiveTimeStart: undefined,
    lastActiveTimeEnd: undefined,
    createTimeStart: undefined,
    createTimeEnd: undefined,
    pageNum: 1,
    pageSize: 10
}                       // 初始化查询条件
// hooks
const {
    query,
    dataList,
    total,
    loading,
    loadData,
    resetQuery
} = useDataLoader<AiConversationPageVO, AiConversationPageQuery>(AiConversationAPI.PAGE.request, initialQuery);
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
