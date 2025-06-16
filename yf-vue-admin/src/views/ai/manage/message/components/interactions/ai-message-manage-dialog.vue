<template>
  <!--  模态框  -->
  <el-dialog
      v-model="visible"
      :title="props.title"
      :width="props.device === DeviceEnum.MOBILE ? '90%' : '50%'"
      draggable
      overflow
      @close="props.closeDialog()"
  >
    <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
    >
      <el-form-item label="角色" prop="role">
        <el-radio-group v-model="form.role">
          <el-radio
              v-for="(value,key) in props.dictData['ai_role']"
              :key="key"
              :value="value">
            {{ value }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="聊天内容" prop="content">
        <Markdown v-model="form.content"
                  height="400px"
                  mode="edit"
                  placeholder="请输入聊天内容"
                  save-path="demo/markdown"/>
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="props.closeDialog()">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import {AiMessageForm} from "@/api/ai/message/type";
import {AiMessageAPI} from "@/api/ai/message";
import {DictType} from "@/api/system/dict-data/type";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {useCrudActions} from "@/hooks/useCrudActions";
import {FormInstance, FormRules} from "element-plus";

// 组件定义
defineOptions({
  name: "AiMessageManageDialog",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  conversationId: string;
  currentClickRowId: string | undefined;
  visible: boolean;
  title: string;
  dictData?: Record<DictType | string, Record<any, string>>;
  device?: DeviceEnum;
  closeDialog: (callback?: () => void) => void;
  loadData: (callback?: () => void) => Promise<void>;
}>(), {
  device: DeviceEnum.DESKTOP,
  dictData: () => ({})
});

const emits = defineEmits<{
  (event: "update:visible", visible: boolean): void
}>()

// hooks
const visible = useVModel(props, 'visible', emits)
const {
  saveData,
  updateData
} = useCrudActions<string, AiMessageForm>(AiMessageAPI.SAVE.request, AiMessageAPI.UPDATE.request, undefined, undefined);
// 初始校验规则
const initialForm: AiMessageForm = {
  role: undefined,
  content: undefined,
  conversationId: undefined
}
const rules: FormRules = {
  role: [{required: true, message: "请输入角色", trigger: "blur"}],
}
// 数据
const formRef = ref<FormInstance | null>(null);          // 字典数据表单
const form = ref<AiMessageForm>({...initialForm});

// 方法
async function submitForm() {
  const isValid = await formRef.value?.validate(); // 使用 await 简化验证逻辑
  if (!isValid) return; // 验证未通过，直接返回
  // 校验通过后执行 API 请求
  if (props.currentClickRowId) {
    await updateData(props.currentClickRowId, form.value, () => {
      props.closeDialog()
      props.loadData()
    })
  } else {
    await saveData(form.value, () => {
      props.closeDialog()
      props.loadData()
    })
  }
}

// 生命周期
onMounted(() => {
  form.value.conversationId = props.conversationId;
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
