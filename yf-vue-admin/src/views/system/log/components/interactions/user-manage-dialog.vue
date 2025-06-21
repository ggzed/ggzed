<template>
  <el-dialog
      v-model="visible"
      :title="props.title"
      :width="props.device === DeviceEnum.MOBILE ? '90%' : '50%'"
      draggable
      overflow
      @close="props.closeDialog()"
  >
    <el-form
        ref="userFormRef"
        :model="form"
        :rules="rules"
        label-width="100px"
    >
      <el-form-item label="用户名 :" prop="username">
        <el-input v-model="form.username" placeholder="请输入用户名"/>
      </el-form-item>
      <el-form-item label="用户昵称 :" prop="nickname">
        <el-input v-model="form.nickname" placeholder="请输入用户昵称"/>
      </el-form-item>
      <el-form-item label="性别 :" prop="gender">
        <el-radio-group v-model="form.gender">
          <el-radio v-for="(value,key) in props.userDict[DictType.GENDER]" :value="Number(key)">{{ value }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="身份信息 :" prop="roleIds">
        <el-select v-model="form.roleIds" multiple placeholder="请您选择一个角色">
          <el-option
              v-for="item in props.roleOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所属部门 :" prop="deptId">
        <el-tree-select
            v-model="form.deptId"
            :data="props.deptOptions"
            check-strictly
            clearable
            placeholder="请您选择部门"
        >
        </el-tree-select>
      </el-form-item>
      <el-form-item label="邮箱 :" prop="email">
        <el-input v-model="form.email" placeholder="请输入用户邮箱"/>
      </el-form-item>
      <el-form-item label="手机号 :" prop="phoneNumber">
        <el-input v-model="form.phoneNumber" placeholder="请输入用户手机号"/>
      </el-form-item>
      <el-form-item label="用户状态 :" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio v-for="optionType in EnableStatusEnum.OPTIONS_RADIO" :value="Number(optionType.value)">{{
              optionType.label
            }}
          </el-radio>
        </el-radio-group>
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
import {useCrudActions} from "@/hooks/useCrudActions";
import {UserAPI} from "@/api/system/user";
import {UserForm} from "@/api/system/user/type";
import {DictType} from "@/api/system/dict-data/type";
// 组件定义
defineOptions({
  name: "UserManageDialog",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  // 当前点击节点ID
  currentClickRowId?: string;
  // dialog-visible
  visible: boolean;
  // dialog-title
  title: string;
  // 设备
  device?: DeviceEnum;
  // 部门集合
  deptOptions: OptionType[];
  // 角色集合
  roleOptions: OptionType[];
  // 用户模块所需字典
  userDict: Record<DictType | string, Record<any, string>>;
  // 加载数据
  loadData: (callback?: () => void) => Promise<void>;
  // 关闭弹窗
  closeDialog: (callback?: () => void) => void;
}>(), {device: DeviceEnum.DESKTOP});

const emits = defineEmits<{
  (event: "update:visible", visible: boolean): void
}>()

// hooks
const visible = useVModel(props, 'visible', emits)
const {
  saveData,
  updateData,
} = useCrudActions<string, UserForm>(UserAPI.SAVE.request, UserAPI.UPDATE.request, UserAPI.DELETE.request, UserAPI.UPDATE_STATUS.request);

// 初始校验规则
const initialForm: UserForm = {
  id: undefined,
  deptId: undefined,
  username: "",
  nickname: "",
  gender: 1,
  status: 1,
  roleIds: []
}
const rules: FormRules = {
  username: [
    {required: true, message: "用户名不能为空", trigger: "blur"},
    {max: 24, min: 4, message: "用户名在 3 ~ 24 之间", trigger: "blur"},
  ],
  nickname: [
    {required: true, message: "用户昵称不能为空", trigger: "blur"},
    {max: 24, message: "昵称过长", trigger: "blur"}
  ],
  phoneNumber: [
    {
      pattern: /^$|^1(3\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\d|9[0-35-9])\d{8}$/,
      message: "请输入正确的手机号码",
      trigger: "blur"
    }
  ],
  email: [{type: "email", message: "请输入正确的邮箱地址", trigger: "blur"}],
  deptId: [{required: true, message: "所属部门不能为空", trigger: "blur"}],
  roleIds: [{required: true, message: "用户角色不能为空", trigger: "blur"}]
}
// 数据
const form = ref<UserForm>({...initialForm});
const userFormRef = ref<FormInstance | null>(null);          // 字典数据表单
// 方法
async function submitForm() {
  const isValid = await userFormRef.value?.validate(); // 使用 await 简化验证逻辑
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
    UserAPI.FORM.request(form.value.id).then(({data}) => {
      Object.assign(form.value, data);
    });
  }
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
