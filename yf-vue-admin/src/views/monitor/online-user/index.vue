<template>
  <div>
    <el-row :gutter="20">
      <!-- 实时数据 -->
      <el-col :lg="12" :md="12" :sm="12" :span="12" :xs="24">
        <el-card>
          <div ref="onlineChat" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <!--   在线用户表格     -->
      <el-col :lg="12" :md="12" :sm="12" :span="12" :xs="24">
        <el-table :data="tableData" border style="height:300px;width: 100%">
          <el-table-column label="用户名" prop="username"/>
          <el-table-column label="昵称" prop="nickname"/>
          <el-table-column v-permission="[OnlineUserAPI.KICK_OUT.permission]" label="操作">
            <template #default="scope">
              <el-button
                  v-if="userId !== scope.row.userId"
                  link
                  size="small"
                  type="primary"
                  @click.stop="kickOutOnlineUser(scope.row.username,scope.row.userId)"
              >
                踢出用户
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import {RequestConstant} from "@/constants/request";
import {useUserStore} from "@/store/modules/user";
import {useECharts} from "@/hooks/useECharts";
import {OnlineUserAPI} from "@/api/monitor/online-user";
import {OnlineUserVO} from "@/api/monitor/online-user/type";

defineOptions({
  name: "OnlineUser",
  inheritAttrs: false,
});

const userStore = useUserStore();
const token = RequestConstant.Header.AuthorizationPrefix + userStore.authInfo.accessToken;
const userId = computed(() => userStore.userInfo.userId);
const {
  data,
  eventSource,
  close
} = useEventSource(OnlineUserAPI.STREAM_SSE.endpoint(token), ['message'] as const, {
  autoReconnect: {
    retries: 3,
    delay: 2000,
    onFailed() {
      ElMessage.error("服务异常，已关闭")
    },
  }
});     // SSE连接

const onlineChat = ref<HTMLDivElement | null>(null);
const {options} = useECharts(onlineChat, OnlineUserAPI.STREAM_SSE.chartOptions()) // ECharts 图表绘画

const loading = ref(false);
const tableData = ref<OnlineUserVO[]>([]);

// 定义事件监听器
function handleMessage(event: MessageEvent) {
  const data = event.data;

  // 确保 options.series 是一个数组并且有数据
  const series = options.series as Array<{ data: any[] }>;
  if (series && series.length > 0) {
    // 将新数据添加到数组末尾
    series[0].data.push(data);

    // 检查数组长度是否超过 7，如果是则移除最早的一个
    if (series[0].data.length > 7) {
      series[0].data.shift();
    }
  }
}

function kickOutOnlineUser(username: string, userId: string) {
  // 踢出在线用户
  ElMessageBox.confirm(`确认踢出用户 【<b>${username}</b>】 ?`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    draggable: true,
    dangerouslyUseHTMLString: true
  }).then(() => {
    OnlineUserAPI.KICK_OUT.request(userId).then(() => {
      handelPage();
    });
  })
}

function handelPage() {
  loading.value = true;
  // 获取在线用户
  OnlineUserAPI.PAGE.request().then(({data}) => {
    tableData.value = data;
  }).finally(() => {
    loading.value = false;
  })
}

watch(data, () => {
  // 数据更新则 , 更新在线用户
  if (!loading.value) {
    handelPage()
  }
})

// 添加事件监听器
eventSource.value?.addEventListener('message', handleMessage);

// 在组件卸载时关闭 EventSource 并移除事件监听器
onUnmounted(() => {
  if (eventSource.value) {
    eventSource.value.removeEventListener('message', handleMessage);
    eventSource.value.close();
    console.log("EventSource 连接已关闭且监听器已移除。");
  }
});

onMounted(() => {
  if (!loading.value) {
    handelPage()
  }
})

</script>

<style lang="scss" scoped>
/* 样式 */
</style>
