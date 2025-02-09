<template>
  <el-dialog
      v-model="visible"
      :title="props.title"
      :width="device === DeviceEnum.MOBILE ? '90%' : '30%'"
      draggable
      overflow
      @close="props.closeDialog()"
  >
    <el-form
        ref="updatePasswordFormRef"
        :model="form"
        :rules="rules"
        label-width="100px"
    >
      <el-form-item v-if="hasPassword" label="旧密码 :" prop="oldPassword">
        <el-input v-model="form.oldPassword" placeholder="请输入您的旧密码" show-password type="password"/>
      </el-form-item>
      <el-form-item label="新密码 :" prop="newPassword">
        <el-input v-model="form.newPassword" placeholder="请输入您的新密码" show-password type="password"/>
      </el-form-item>
      <el-form-item label="确认密码 :" prop="checkPassword">
        <el-input v-model="form.checkPassword" placeholder="请再次输入您的新密码" show-password type="password"/>
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
import {useSystemStore} from "@/store/modules/system";
import {FormInstance, FormRules} from "element-plus";
import {UserProfileAPI} from "@/api/system/user-profile";
import {ResetUserPasswordForm} from "@/api/system/user-profile/type";
// 组件 props & emits
const props = withDefaults(defineProps<{
  // dialog-visible
  visible: boolean;
  // dialog-title
  title: string;
  // 关闭弹窗
  closeDialog: (callback?: () => void) => void;
}>(), {});

const emits = defineEmits<{
  (event: "update:visible", visible: boolean): void
}>()

// hooks
const visible = useVModel(props, 'visible', emits)
// 初始化表单 & 校验规则
const initialForm: ResetUserPasswordForm = {
  oldPassword: "",
  newPassword: "",
  checkPassword: ""
}

const rules: FormRules = {
  oldPassword: [
    {required: true, min: 8, max: 16, message: '密码在8~16位', trigger: 'blur'}
  ],
  newPassword: [
    {required: true, min: 8, max: 16, message: '密码在8~16位', trigger: 'blur'}
  ],
  checkPassword: [
    {required: true, min: 8, max: 16, message: '密码在8~16位', trigger: 'blur'},
    {validator: validatePassword, trigger: 'blur'},
  ]
}
// 数据
const device = computed(() => useSystemStore().app.device)    // 设备类型
const updatePasswordFormRef = ref<FormInstance | null>(null); // 表单实例
const hasPassword = ref(true);                                // 是否有密码
const form = ref<ResetUserPasswordForm>({...initialForm});    // 重置密码表单
// 方法
/**
 * 修改密码后退出登录
 */
function submitForm() {
  UserProfileAPI.UPDATE_PASSWORD.request(form.value).then(() => {
    ElMessage.success("修改密码成功");
    props.closeDialog();
  })
}

/**
 * 校验密码是否一致
 */
function validatePassword(rule: any, value: any, callback: any) {
  if (value !== form.value.newPassword) {
    callback(new Error('两次输入的密码不一致'));
  } else {
    callback();
  }
}


// 生命周期
onMounted(() => {
  UserProfileAPI.CHECK_PASSWORD_EXISTENCE.request().then(({data}) => {
    hasPassword.value = data;
  })
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
