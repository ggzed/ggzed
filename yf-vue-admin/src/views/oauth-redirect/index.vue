<template>

</template>

<script lang="ts" setup>
import {useRoute} from "vue-router";
import {useOauthStore} from "@/store/modules/oauth";

defineOptions({
  name: "OauthRedirect",
  inheritAttrs: false,
});

const route = useRoute();
const router = useRouter();
const oauthStore = useOauthStore();
/**
 * 注意 第三方登录回调地址需要含有 type=ThirdPartyEnum
 * 例如：http://localhost:3000/oauth/callback?type=GITHUB
 */
const {type, ...params} = route.query as any;
// 设置 type 参数
oauthStore.setType(type)
// 设置 oauth 参数
oauthStore.setParams(params)
// 跳转页面
router.replace({path: oauthStore.oauthRedirectPath})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
