<template>
  <el-dialog
      v-model="visible"
      :title="props.title"
      :width="props.device === DeviceEnum.MOBILE ? '92%' : '74%'"
      draggable
      overflow
      top="40px"
      @close="closeOperationLogDialog"
  >
    <div class="operation-log-detail">
      <el-descriptions :column="4" border>
        <el-descriptions-item :min-width="88">
          <template #label>
            请求方式:
          </template>
          {{ props.operationLogDetail.requestMethod }}
        </el-descriptions-item>
        <el-descriptions-item :span="3">
          <template #label>
            请求路径:
          </template>
          {{ props.operationLogDetail.operatorUrl }}
        </el-descriptions-item>
        <el-descriptions-item :span="2">
          <template #label>
            操作模块:
          </template>
          {{ props.businessDict[props.operationLogDetail.businessType || 0] }} /
          {{ props.operationLogDetail.title }}
        </el-descriptions-item>
        <el-descriptions-item :span="2">
          <template #label>
            操作信息:
          </template>
          {{ props.operationLogDetail.operatorName }} /
          {{ props.operationLogDetail.operatorIp }} /
          {{ props.operationLogDetail.operatorLocation }} /
          {{ props.operationLogDetail.operatorBrowser }} /
          {{ props.operationLogDetail.operatorOs }}
        </el-descriptions-item>
        <el-descriptions-item :span="4">
          <template #label>
            操作方法:
          </template>
          {{ props.operationLogDetail.method }}
        </el-descriptions-item>
        <el-descriptions-item :span="4">
          <template #label>
            请求参数:
          </template>
          {{ props.operationLogDetail.operatorParam }}
        </el-descriptions-item>
        <el-descriptions-item :span="4">
          <template #label>
            响应结果:
          </template>
          {{ props.operationLogDetail.jsonResult }}
        </el-descriptions-item>
        <el-descriptions-item :span="4">
          <template #label>
            异常结果:
          </template>
          {{ props.operationLogDetail.errorMsg }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            操作状态:
          </template>
          <el-tag :type="OperatorLogStatusEnum.TAG_STYLE[(props.operationLogDetail.status || 0) % 2]">
            {{ OperatorLogStatusEnum.OPTIONS[props.operationLogDetail.status || 0] }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            操作耗时:
          </template>
          <el-text :type="(props.operationLogDetail.costTime || 0) > 1000 ? 'danger' : 'success' ">{{
              props.operationLogDetail.costTime + 'ms'
            }}
          </el-text>
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            操作时间:
          </template>
          {{ props.operationLogDetail.createTime }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            对接客户端:
          </template>
          <el-tag>{{ props.operatorTypeDict[props.operationLogDetail.operatorType || 0] }}</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </div>
    <template #footer>
      <el-button @click="closeOperationLogDialog()"> 关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
// 组件定义
import {DeviceEnum} from "@/enums/DeviceEnum";
import {OperatorLogStatusEnum} from "@/constants/system";
import {OperationLogVO} from "@/api/monitor/operation-log/type";

defineOptions({
  name: "DeptManageDialog",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  operationLogDetail: OperationLogVO;
  businessDict: Record<number | string, string>;                  // 日志业务类型字典数据
  operatorTypeDict: Record<number | string, string>;              // 日志操作类型字典数据
  // dialog-visible
  visible: boolean;
  // dialog-title
  title: string;
  // 设备
  device?: DeviceEnum;
}>(), {device: DeviceEnum.DESKTOP});
const emits = defineEmits<{
  (event: "update:visible", visible: boolean): void
}>()
// hooks
const visible = useVModel(props, 'visible', emits)

async function closeOperationLogDialog() {
  visible.value = false;
}
</script>

<style lang="scss" scoped>
/* 样式 */
.operation-log-detail {
  width: 100%;
  overflow: auto;
}
</style>
