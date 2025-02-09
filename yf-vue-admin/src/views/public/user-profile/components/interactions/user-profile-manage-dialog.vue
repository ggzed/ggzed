<template>
  <el-dialog
      v-model="visible"
      :title="props.title"
      :width="device === DeviceEnum.MOBILE ? '90%' : '40%'"
      draggable
      overflow
      @close="props.closeDialog"
  >
    <el-form
        ref="userProfileFormRef"
        :model="form"
        :rules="rules"
        label-width="100px"
    >
      <el-form-item label="昵称 :" prop="nickname">
        <el-input v-model="form.nickname" placeholder="请输入您的昵称"/>
      </el-form-item>
      <el-form-item label="性别 :" prop="gender">
        <el-radio-group v-model="form.gender">
          <el-radio v-for="(value,key) in props.genderDict" :value="Number(key)">{{ value }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="手机号 :" prop="phoneNumber">
        <el-input v-model="form.phoneNumber" disabled placeholder="请输入您的手机号"/>
      </el-form-item>
      <el-form-item label="邮箱 :" prop="email">
        <el-input v-model="form.email" disabled placeholder="请输入您的邮箱"/>
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
import {DeviceEnum} from "@/enums/DeviceEnum";
import {useSystemStore} from "@/store/modules/system";
import {UserProfileForm, UserProfileInfoVO} from "@/api/system/user-profile/type";
import {FormInstance, FormRules} from "element-plus";
import {UserProfileAPI} from "@/api/system/user-profile";
// 组件定义
defineOptions({
  name: "UserProfileManageDialog",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  // dialog-visible
  visible: boolean;
  // dialog-title
  title: string;
  // 用户信息
  userProfileInfo: UserProfileInfoVO;
  // 性别字典
  genderDict: Record<number | string, string>;
  // 加载数据
  loadData: (callback?: () => void) => Promise<void>;
  // 关闭弹窗
  closeDialog: (callback?: () => void) => void;
}>(), {});

const emits = defineEmits<{
  (event: "update:visible", visible: boolean): void
}>()

// hooks
const visible = useVModel(props, 'visible', emits)

// 初始化表单 & 校验规则
const initialForm: UserProfileForm = {
  nickname: "",
  gender: 1,
  phoneNumber: "",
  email: ""
}

const rules: FormRules = {
  nickname: [
    {required: true, message: "用户昵称不能为空", trigger: "blur"},
    {max: 24, min: 3, message: "昵称过长", trigger: "blur"}
  ],
  phoneNumber: [
    {
      pattern: /^$|^1(3\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\d|9[0-35-9])\d{8}$/,
      message: "请输入正确的手机号码",
      trigger: "blur"
    }
  ],
  email: [{type: "email", message: "请输入正确的邮箱地址", trigger: "blur"}],
}

// 数据
const device = computed(() => useSystemStore().app.device)    // 设备类型
const form = ref<UserProfileForm>({...initialForm});
const userProfileFormRef = ref<FormInstance | null>(null);

// 方法
/**
 * 修改个人信息
 */
async function submitForm() {
  const isValid = await userProfileFormRef.value?.validate(); // 使用 await 简化验证逻辑
  if (!isValid) return; // 验证未通过，直接返回
  await UserProfileAPI.UPDATE.request(form.value).then(() => {
    props.loadData();
    props.closeDialog();
  })
}

// 生命周期
onMounted(() => {
  Object.assign(form.value, props.userProfileInfo);
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
