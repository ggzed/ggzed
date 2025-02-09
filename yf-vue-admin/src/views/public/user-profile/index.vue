<template>
  <div v-loading="loading" class="app-container">
    <el-row :gutter="20">
      <el-col :lg="8" :md="10" :sm="12" :xs="24">
        <user-profile-card
            v-model:user-profile-info="userProfileInfo"
            :loadData="loadData"
        />
      </el-col>

      <el-col :lg="16" :md="14" :sm="12" :xs="24">
        <user-profile-auth-info
            v-model:user-profile-info="userProfileInfo"
            :loadData="loadData"
        />
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import {UserProfileAPI} from "@/api/system/user-profile";
import {UserProfileInfoVO} from "@/api/system/user-profile/type";
import {useRoute} from "vue-router";
import {useOauthStore} from "@/store/modules/oauth";
// 组件定义
defineOptions({
  name: "UserProfile",
  inheritAttrs: false,
});
// 数据
const route = useRoute()
const userProfileInfo = ref<UserProfileInfoVO>({});   // 个人中心信息
const oauthStore = useOauthStore();                   // oauth 信息
const loading = ref(false);                           // 加载状态

// 方法
async function loadData(callback?: () => void): Promise<void> {
  loading.value = true;
  try {
    await UserProfileAPI.INFO.request().then(({data}) => {
      userProfileInfo.value = data
    })
  } finally {
    loading.value = false;
  }
}

onMounted(async () => {
  loading.value = true;
  try {
    // 判断是否是第三方账号绑定
    if (oauthStore.type && oauthStore.params) {
      // 1. 绑定第三方账户
      await UserProfileAPI.BIND_THIRD_PARTY.request(oauthStore.type, oauthStore.params).then(() => {
        // 2. 绑定完后获取用户个人信息
        loadData();
      }).finally(() => {
        // 3. 绑定完后清除 oauth 信息
        oauthStore.resetOauth();
      })
    } else {
      // 1. 获取用户个人信息
      await loadData();
    }
  } finally {
    loading.value = false;
  }
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
