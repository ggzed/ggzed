<template>
  <div v-loading="loading" class="app-container">
    <el-row :gutter="20">
      <el-col :lg="8" :md="10" :sm="12" :xs="24">
        <user-profile v-model:user-profile-info="userProfileInfo"
                      :reset-user-profile-info="getUserProfileInfo"/>
      </el-col>

      <el-col :lg="16" :md="14" :sm="12" :xs="24">
        <user-base-info v-model:user-profile-info="userProfileInfo"
                        :redirect-oauth="redirectOauth"
                        :reset-user-profile-info="getUserProfileInfo"
        />
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import {UserProfileAPI} from "@/api/system/user/profile";
import {UserProfileInfoVO} from "@/api/system/user/profile/type";

import {useOauthStore} from "@/store/modules/oauth";
import {useRoute} from "vue-router";
import {LoginType} from "@/api/auth/factory";
import {AuthAPI} from "@/api/auth";

defineOptions({
  name: "UserProfile",
  inheritAttrs: false,
});

// 数据
const route = useRoute()

const userProfileInfo = ref<UserProfileInfoVO>({});   // 个人中心信息
const oauthStore = useOauthStore();                   // oauth 信息
const loading = ref(false);                            // 加载状态

// 方法
function getUserProfileInfo() {
  UserProfileAPI.INFO.request().then(({data}) => {
    userProfileInfo.value = data
  }).finally(() => {
    loading.value = false;
  })
}

function redirectOauth(type: LoginType) {
  // 1. 设置回调地址为当前地址
  oauthStore.setOauthRedirectUri(route.path)
  AuthAPI.REDIRECT_LOGIN.request(type).then(({data}) => {
    // 2. 跳转到第三方登录
    window.location.href = data;
  })
}

// 生命周期
onMounted(() => {
  loading.value = true;
  // 判断是否是第三方账号绑定
  if (oauthStore.type && oauthStore.params) {
    // 1. 绑定第三方账户
    UserProfileAPI.BIND_THIRD_PARTY.request(oauthStore.type, oauthStore.params).then(() => {
      // 2. 绑定完后获取用户个人信息
      getUserProfileInfo()
    }).finally(() => {
      // 3. 绑定完后清除 oauth 信息
      oauthStore.resetOauth();
    })
  } else {
    // 1. 获取用户个人信息
    getUserProfileInfo()
  }
})
</script>

<style lang="scss" scoped>
/* 样式 */

</style>
