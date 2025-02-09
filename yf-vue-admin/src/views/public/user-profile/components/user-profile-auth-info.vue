<template>
  <div class="user-base-info">
    <!--  敏感信息修改  -->
    <div class="user-login-info">
      <h2>登录信息</h2>
      <h4>登录用户名</h4>
      <el-input
          :placeholder="userProfileInfo.username"
          disabled
          style="width: 220px;"
      />
      <el-button style=" margin-left: 10px" @click="openUpdateUsername">修改用户名</el-button>
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
        <div v-for="platform in supportOauthPlatforms" :key="platform">
          <svg-icon v-if="!oauthProviders?.includes(platform)" :icon-class="platform" size="32px"
                    @click="redirectOauth(platform)"></svg-icon>
        </div>
      </div>
    </div>
  </div>
  <reset-password-dialog
      v-if="visible"
      v-model:visible="visible"
      :close-dialog="closeDialog"
      :title="title"
  />
</template>

<script lang="ts" setup>
import {OauthAPI} from "@/api/system/oauth";
import {AuthAPI} from "@/api/auth";
import {UserProfileInfoVO} from "@/api/system/user-profile/type";
import {useDialogManage} from "@/hooks/useDialogManage";
import {UserProfileAPI} from "@/api/system/user-profile";
// 组件定义
defineOptions({
  name: "UserProfileAuthInfo",
  inheritAttrs: false,
});
// props & emits
const props = withDefaults(defineProps<{
  userProfileInfo: UserProfileInfoVO,
  loadData: () => void,
}>(), {});

const emits = defineEmits<{
  (event: 'update:userProfileInfo', value: UserProfileInfoVO): void;
}>();

// hooks
const userProfileInfo = useVModel(props, 'userProfileInfo', emits)
const {
  visible,
  title,
  openDialog,
  closeDialog
} = useDialogManage();
// 数据
const supportOauthPlatforms = ref<string[]>([]) // 支持的 oauth 平台
const oauthProviders = computed(() => userProfileInfo.oauthInfo?.map(info => info.platformName))

// 方法
/**
 * 跳转第三方进行绑定
 * @param type
 */
function redirectOauth(type: string) {
  // 1. 设置回调地址为当前地址
  oauthStore.setOauthRedirectUri(route.path)
  AuthAPI.REDIRECT_LOGIN.request(type).then(({data}) => {
    // 2. 跳转到第三方登录
    window.location.href = data;
  })
}

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
    ElMessage.success('解绑成功');
    props.loadData();
  })
}

function openUpdatePasswordDialog() {
  openDialog("修改账号密码")
}

function openUpdateUsername() {
  ElMessageBox.prompt(`您正在修改您的登录用户名 , 7天内只能修改一次`, '⚠️ 警告', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    inputPattern: /^.{4,24}$/,
    inputErrorMessage: '登录用户名长度为 4 ~ 24',
    dangerouslyUseHTMLString: true,
  }).then(({value}) => {
    // /^(?=.{8,16}$)(?![0-9]*$)(?![a-zA-Z]*$)[\w!#$%&'*+/=?^_`{|}~-]+$/, // 正则表达式：长度在8到16之间，包含字母和数字
    UserProfileAPI.UPDATE_USERNAME.request(value).then(() => {
      userProfileInfo.value.username = value;
      ElMessage.success(`重置登录用户名为 : ${value}`);
    })
  })
}

// 生命周期
onMounted(() => {
  OauthAPI.SUPPORT_PLATFORMS.request().then(({data}) => {
    supportOauthPlatforms.value = data
  })
})
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
