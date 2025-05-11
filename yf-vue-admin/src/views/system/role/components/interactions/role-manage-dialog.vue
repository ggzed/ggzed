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
        ref="roleFormRef"
        :model="form"
        :rules="rules"
        label-width="100px"
    >
      <el-form-item label="角色名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入角色名称"/>
      </el-form-item>

      <el-form-item label="角色编码" prop="code">
        <el-input v-model="form.code" placeholder="请输入角色编码"/>
      </el-form-item>

      <el-form-item label="数据权限" prop="dataScope">
        <el-select v-model="form.dataScope">
          <el-option v-for="(value,key) in props.roleDict[DictType.DATA_PERMISSION]" :key="Number(key)" :label="value"
                     :value="Number(key)"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio v-for="optionType in EnableStatusEnum.OPTIONS_RADIO" :value="Number(optionType.value)">{{
              optionType.label
            }}
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="排序" prop="sort">
        <el-input-number
            v-model="form.sort"
            :min="0"
            controls-position="right"
            style="width: 100px"
        />
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
import {DeviceEnum} from "@/enums/DeviceEnum";
import {EnableStatusEnum} from "@/constants/system";
import {FormInstance, FormRules} from "element-plus";
import {RoleForm} from "@/api/system/role/type";
import {useCrudActions} from "@/hooks/useCrudActions";
import {RoleAPI} from "@/api/system/role";
import {DictType} from "@/api/system/dict-data/type";

// 组件定义
defineOptions({
  name: "RoleManageDialog",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  // 当前点击节点ID
  currentClickRowId: number | undefined;
  // 数据权限数据
  roleDict: Record<DictType | string, Record<any, string>>;
  // dialog-visible
  visible: boolean;
  // dialog-title
  title: string;
  // 设备
  device?: DeviceEnum;
  // 加载数据
  loadData: (callback?: () => void) => Promise<void>;
  // 关闭弹窗
  closeDialog: (callback?: () => void) => void;
}>(), {device: DeviceEnum.DESKTOP});

const emits = defineEmits<{
  (event: "update:visible", visible: boolean): void
  (event: "update:form", form: RoleForm): void
}>()

// hooks
const visible = useVModel(props, 'visible', emits)
const {
  updateData,
  saveData
} = useCrudActions<number, RoleForm>(RoleAPI.SAVE.request, RoleAPI.UPDATE.request, RoleAPI.DELETE.request, RoleAPI.UPDATE_STATUS.request);
// 初始校验规则
const initialForm: RoleForm = {
  code: undefined,
  dataScope: 0,
  name: undefined,
  sort: 1,
  status: 1
}
const rules: FormRules = {
  name: [{required: true, message: "请输入角色名称", trigger: "blur"}],
  code: [{required: true, message: "请输入角色编码", trigger: "blur"}],
  dataScope: [{required: true, message: "请选择数据权限", trigger: "blur"}],
  status: [{required: true, message: "请选择状态", trigger: "blur"}]
}
// 数据
const roleFormRef = ref<FormInstance | null>(null);          // 字典数据表单
const form = ref<RoleForm>({...initialForm});

// 方法
async function submitForm() {
  const isValid = await roleFormRef.value?.validate(); // 使用 await 简化验证逻辑
  if (!isValid) return; // 验证未通过，直接返回
  // 校验通过后执行 API 请求
  if (form.value.id) {
    await updateData(form.value.id, form.value, () => {
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
  form.value.id = props.currentClickRowId;
  if (form.value.id) {
    RoleAPI.FORM.request(form.value.id).then(({data}) => {
      Object.assign(form.value, data);
    });
  }
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
