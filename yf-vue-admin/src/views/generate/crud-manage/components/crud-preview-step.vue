<template>
  <div class="crud-preview-step">
    <el-card class="table-container">
      <el-row :gutter="10">
        <!-- 左侧目录树 -->
        <el-col :span="8">
          <el-scrollbar max-height="72vh">
            <el-tree
                :data="[fileTree]"

                default-expand-all
                highlight-current
                @node-click="handleNodeClick"
            >
              <template #default="{ data }">
                <svg-icon v-if="data.isFile" :icon-class="data.codeLanguage"/>
                <svg-icon v-else icon-class="dir"/>
                <span style="margin-left: 4px">{{ data.name }}</span>
              </template>
            </el-tree>
          </el-scrollbar>
        </el-col>

        <!-- 右侧代码预览 -->
        <el-col :span="16">
          <el-scrollbar max-height="72vh">
            <markdown v-if="currentCode" v-model="currentCode" mode="preview"></markdown>
            <el-text v-else size="large">请从左侧选择要预览的文件</el-text>
          </el-scrollbar>
        </el-col>
      </el-row>
      <template #footer>
        <el-button type="danger" @click="props.quitConfig">退 出</el-button>
        <el-button type="primary" @click="goToPreStep">返回上一步</el-button>
        <el-button type="primary" @click="genZip">生成代码</el-button>
      </template>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import {PreviewGenCodeTreeVO} from "@/api/generate/crud-code/type";
import {GenerateCrudAPI} from "@/api/generate/crud-code";
import {useUserStore} from "@/store/modules/user";
import {SystemConstant} from "@/constants/system";
// 组件定义
defineOptions({
  name: "CrudPreviewStep",
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
const {permissions, roles} = useUserStore();
const fileTree = ref<PreviewGenCodeTreeVO>({});
const currentCode = ref('')

// 方法
function handleNodeClick(node) {
  if (node.isFile) {
    currentCode.value = "``` " + node.codeLanguage + "\n" + node.content + "```";
  }
}

function goToPreStep() {
  const hasPermission = permissions?.includes(GenerateCrudAPI.MENU.permission);
  const isAdmin = roles.includes(SystemConstant.ADMIN);

  active.value -= hasPermission || isAdmin ? 1 : 2;
}

async function genZip() {
  await GenerateCrudAPI.EXPORT_CRUD_CODE.request(props.curdManageId).then((response) => {
    const fileName = decodeURI(
        response.headers["content-disposition"].split(";")[1].split("=")[1]
    );

    const blob = new Blob([response.data], {type: "application/zip"});
    const a = document.createElement("a");
    const url = window.URL.createObjectURL(blob);
    a.href = url;
    a.download = fileName;
    a.click();
    window.URL.revokeObjectURL(url);
  })
}

// 生命周期
onMounted(async () => {
  await GenerateCrudAPI.PREVIEW_CRUD_CODE.request(props.curdManageId).then(({data}) => {
    fileTree.value = data
  })
})
</script>

<style lang="scss" scoped>
/* 样式 */
:deep(.vuepress-markdown-body:not(.custom)) {
  padding: 0;
}
</style>
