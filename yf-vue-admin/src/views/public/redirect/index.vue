<template>
  <div></div>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
// 数据
defineOptions({
  name: "Redirect",
  inheritAttrs: false,
});

const route = useRoute();
const router = useRouter();

const {params, query} = route;
const {path} = params;
// 方法
router.replace({path: "/" + path, query});
</script>

<style scoped lang="scss">
/* 样式 */
</style>
