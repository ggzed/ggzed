<template>
  <div class="crud-display-step">
    <el-card class="table-container">
      <!--      <div style="height: 50vh;">-->
      <!--        <iframe-->
      <!--            :srcdoc="displayHtml"-->
      <!--            class="crud-iframe"-->
      <!--            style="width: 100%; height: 100%; border: none;"-->
      <!--        ></iframe>-->
      <!--      </div>-->
      部分 HTML 内容仍在优化中，感谢您的理解与支持！<br/><br/>
      可以使用 F12 查看 /crud/code/display 请求响应的内容查看目前的 HTML 内容
      <template #footer>
        <el-button type="primary" @click="goToPreStep">返回上一步</el-button>
        <el-button type="danger" @click="exit">退 出</el-button>
        <el-button type="primary" @click="goToNextStep">进入下一步</el-button>
      </template>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import {GenerateCrudAPI} from "@/api/generate/crud-code";
import {useUserStore} from "@/store/modules/user";
import {SystemConstant} from "@/constants/system";

// 组件定义
defineOptions({
  name: "CrudDisplayStep",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  // 目前展示的 step
  active: number;
  // 当前修改的 id
  curdManageId: number;
  // 删除退出当前页
  quitConfig: () => void;
}>(), {});

const emits = defineEmits<{
  (event: "update:active", active: number): void
}>()

// hooks
const active = useVModel(props, 'active', emits)
// 数据
const displayHtml = ref<string>("");
const {permissions, roles} = useUserStore();

// 方法
/**
 * 保存并退出
 */
async function exit() {
  props.quitConfig();
}

/**
 * 进入上一步
 */
function goToPreStep() {
  active.value = active.value - 1;
}

/**
 * 进入下一步
 */
async function goToNextStep() {
  const hasPermission = permissions?.includes(GenerateCrudAPI.MENU.permission);
  const isAdmin = roles.includes(SystemConstant.ADMIN);

  const step = hasPermission || isAdmin ? 1 : 2;
  active.value += step;
}


// 生命周期
onMounted(async () => {
  await GenerateCrudAPI.DISPLAY_CRUD_CODE.request(props.curdManageId).then(({data}) => {
    // 正常情况
    displayHtml.value = data;
  })

  ElNotification.info({
    title: "温馨提示",
    message: "部分 HTML 内容仍在优化中，感谢您的理解与支持！"
  });

})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
