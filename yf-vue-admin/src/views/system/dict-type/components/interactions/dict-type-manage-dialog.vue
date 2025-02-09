<template>
  <el-dialog
      v-model="visible"
      :title="props.title"
      :width="props.device === DeviceEnum.MOBILE ? '90%' : '50%'"
      draggable
      overflow
      @close="props.closeDialog"
  >
    <el-form
        ref="dictTypeFormRef"
        :model="form"
        :rules="rules"
        label-width="100px"
    >
      <el-form-item label="字典名称 :" prop="name">
        <el-input v-model="form.name" placeholder="请输入字典名称"/>
      </el-form-item>
      <el-form-item label="字典类型 :" prop="type">
        <el-input v-model="form.type" placeholder="请输入字典类型"/>
      </el-form-item>
      <el-form-item label="字典状态 :" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio v-for="optionType in EnableStatusEnum.OPTIONS_RADIO" :value="Number(optionType.value)">{{
              optionType.label
            }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="备注 :" prop="remark">
        <el-input v-model="form.remark" :autosize="{ minRows: 3, maxRows: 5 }" maxlength="255"
                  placeholder="请输入字典备注"
                  show-word-limit type="textarea"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="props.closeDialog">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
// 组件定义
import {DeviceEnum} from "@/enums/DeviceEnum";
import {EnableStatusEnum} from "@/constants/system";
import {FormInstance, FormRules} from "element-plus";
import {DictTypeForm} from "@/api/system/dict-type/type";
import {DictTypeAPI} from "@/api/system/dict-type";
import {useCrudActions} from "@/hooks/useCrudActions";

defineOptions({
  name: "DictTypeManageDialog",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  // dialog-visible
  visible: boolean;
  // dialog-title
  title: string;
  // 当前点击的 id
  currentClickRowId: number | undefined;
  // 设备
  device: DeviceEnum;
  // 关闭弹窗
  closeDialog: (callback?: () => void) => void;
  // 加载数据
  loadData: (callback?: () => void) => Promise<void>;
}>(), {device: DeviceEnum.PC});

const emits = defineEmits<{
  (event: "update:visible", visible: boolean): void
}>()

// hooks
const visible = useVModel(props, 'visible', emits)
const {
  saveData,
  updateData
} = useCrudActions<DictTypeForm>(DictTypeAPI.SAVE.request, DictTypeAPI.UPDATE.request, DictTypeAPI.DELETE.request, DictTypeAPI.UPDATE_STATUS.request);
// 初始校验规则 & 表单
const initialForm: DictTypeForm = {
  id: undefined,
  name: undefined,
  remark: undefined,
  status: 1,
  type: undefined
}
const rules: FormRules = {
  name: [{required: true, message: "请输入字典类型名称", trigger: "blur"}],
  type: [{required: true, message: "请输入字典类型", trigger: "blur"}]
}
// 数据
const dictTypeFormRef = ref<FormInstance | null>(null);          // 字典数据表单
const form = ref<DictTypeForm>({...initialForm});

// 方法
async function submitForm() {
  const isValid = await dictTypeFormRef.value?.validate(); // 使用 await 简化验证逻辑
  if (!isValid) return; // 验证未通过，直接返回
  // 校验通过后执行 API 请求
  if (form.value.id) {
    await updateData(form.value.id, form.value, () => {
      props.closeDialog();
      props.loadData()
    })
  } else {
    await saveData(form.value, () => {
      props.closeDialog();
      props.loadData()
    })
  }
}

// 生命周期
onMounted(async () => {
  form.value.id = props.currentClickRowId;
  if (form.value.id) {
    await DictTypeAPI.FORM.request(form.value.id).then(({data}) => {
      Object.assign(form.value, data);
    });
  }
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
