<template>
  <div class="user-base-info">
    <!--  敏感信息修改  -->
    <div class="user-login-info">
      <h2>登录信息</h2>
      <h4>登录密码</h4>
      <el-input
          disabled
          placeholder="********"
          style="width: 220px;"
      />
      <el-button style=" margin-left: 10px" @click="openUpdatePasswordDialog">修改密码</el-button>
    </div>
    <!--  第三方账号绑定/解绑  -->
    <div class="user-third-party">
      <h2>第三方帐号绑定</h2>
      <h4>使用以下任一方式都可以登录到您的帐号，避免由于某个帐号失效导致无法登录</h4>
      <el-table
          :data="userProfileInfo.oauthInfo"
          border
          style="width: 100%"
      >
        <el-table-column align="center" label="序号" min-width="60" type="index"/>
        <el-table-column align="center" label="绑定账号信息" min-width="120" prop="platformName"/>
        <el-table-column align="center" label="详情" min-width="160">
          <template #default="scope">
            <div class="user-oauth__detail">
              <el-avatar :src="scope.row.platformUserAvatar" size="small"/>
              <span>{{ scope.row.platformUsername }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column align="center" label="绑定时间" min-width="120" prop="createTime"/>
        <el-table-column align="center" label="操作" min-width="120">
          <template #default="scope">
            <el-button plain type="danger" @click="handleUnbind(scope.row.id,scope.row.platformName)">解除绑定
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <h4>您还可以绑定以下第三方帐号</h4>
      <div class="third-party__list">
        <div v-for="platform in Object.values(ThirdPartyEnum)" :key="platform">
          <svg-icon v-if="!oauthProviders?.includes(platform)" :icon-class="platform" size="32px"
                    @click="redirectOauth(platform)"></svg-icon>
        </div>
      </div>
    </div>
    <!--  修改密码模态框  -->
    <el-dialog
        v-model="dialog.visible"
        :title="dialog.title"
        :width="systemStore.app.device === DeviceEnum.MOBILE ? '90%' : '30%'"
        draggable
        overflow
        @close="closeUpdatePasswordDialog"
    >
      <el-form
          ref="updatePasswordFormRef"
          :model="form"
          :rules="rules"
          label-width="100px"
      >
        <el-form-item label="旧密码 :" prop="oldPassword">
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
          <el-button @click="closeUpdatePasswordDialog">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import {ThirdPartyEnum} from "@/enums/ThirdPartyEnum";
import {ResetUserPasswordForm, UserProfileInfoVO} from "@/api/system/user/profile/type";
import {LoginType} from "@/api/auth/factory";
import {UserProfileAPI} from "@/api/system/user/profile";
import {useDialogManagement} from "@/hooks/useDialogManagement";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {useSystemStore} from "@/store/modules/system";
import {FormInstance, FormRules} from "element-plus";
import {useFormManagement} from "@/hooks/useFormManagement";
// 数据
const props = withDefaults(defineProps<{
  userProfileInfo: UserProfileInfoVO,
  resetUserProfileInfo: () => void,
  redirectOauth: (type: LoginType) => void
}>(), {});

const systemStore = useSystemStore()
const oauthProviders = computed(() => props.userProfileInfo.oauthInfo?.map(info => info.platformName))

const initialForm: ResetUserPasswordForm = {
  oldPassword: "",
  newPassword: "",
  checkPassword: ""
}

const initialRules: FormRules = {
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

const updatePasswordFormRef = ref<FormInstance | null>(null);

const {dialog, openDialog, closeDialog} = useDialogManagement();

const {form, rules, resetForm, handleSubmit} = useFormManagement(initialForm, initialRules, updatePasswordFormRef);

// 方法
/**
 * 第三方解除绑定
 */
async function handleUnbind(oauthId: string, platform: string) {
  // 1. 二次确认解绑第三方平台
  await ElMessageBox.confirm(`确认解绑【<b>${platform}</b>】平台账户?`, '解绑第三方平台', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    draggable: true,
    dangerouslyUseHTMLString: true
  });
  // 2. 执行解绑操作
  UserProfileAPI.UNBIND_THIRD_PARTY.request(oauthId).then(() => {
    // 3. 解绑成功后,请求页面数据
    props.resetUserProfileInfo();
    ElMessage.success('解绑成功');
  })
}

function validatePassword(rule: any, value: any, callback: any) {
  if (value !== form.newPassword) {
    callback(new Error('两次输入的密码不一致'));
  } else {
    callback();
  }
}

function submitForm() {
  handleSubmit((form) => {
    UserProfileAPI.UPDATE_PASSWORD.request(form).then(() => {
      ElMessage.success("修改密码成功");
      closeUpdatePasswordDialog();
    })
  })
}

function openUpdatePasswordDialog() {
  openDialog("修改个人密码")
}

function closeUpdatePasswordDialog() {
  closeDialog(() => {
    resetForm();
  })
}

// 生命周期

</script>

<style lang="scss" scoped>
/* 样式 */
.user-base-info {
  box-shadow: var(--el-box-shadow-light);
  padding: 10px;

  h2 {
    padding: 24px 0;
    margin: 0;
  }

  h4 {
    margin: 0;
    padding: 16px 0;
  }
}

.user-oauth__detail {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
}

.third-party__list {
  display: flex;
  justify-content: left;
  align-items: center;
  gap: 8px;

  > div {
    cursor: pointer;
  }
}
</style>
