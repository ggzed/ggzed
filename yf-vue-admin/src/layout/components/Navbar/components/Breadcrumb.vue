<template>
  <div class="breadcrumb-container">
    <el-breadcrumb separator="/">
      <template v-for="breadcrumb in breadcrumbs" :key="breadcrumb.path">
        <!--  非 URL 处理    -->
        <el-breadcrumb-item v-if="! validateURL(breadcrumb.redirect as string)" :to="{path : breadcrumb.path}">
          <SystemIcon :icon="breadcrumb.meta.icon"></SystemIcon>
          {{ breadcrumb.meta.title }}
        </el-breadcrumb-item>
        <!--   URL 处理     -->
        <el-breadcrumb-item v-else>
          <a :href="breadcrumb.redirect" target="_blank">
            <SystemIcon :icon="breadcrumb.meta.icon"></SystemIcon>
            {{ breadcrumb.meta.title }}
          </a>
        </el-breadcrumb-item>
      </template>
    </el-breadcrumb>
  </div>
</template>

<script setup lang="ts">
// 数据
import {RouteLocationMatched, RouteMeta} from "vue-router";
import {validateURL} from "@/utils/validation";
import {HOME_ROUTE_RAW} from "@/router/core/ConstantRoutes";

type BreadcrumbType = {
  // 访问路径
  path: string,
  // redirect
  redirect?: string,
  // route 元信息
  meta: RouteMeta
}

const currentRoute = useRoute();
const breadcrumbs = ref<BreadcrumbType[]>([]);

watch(
    () => currentRoute.path,
    (path) => {
      if (path.startsWith("/redirect/")) {
        return;
      }
      breadcrumbs.value = []
      loadBreadcrumb(currentRoute.matched);
    }, {
      immediate: true
    }
);

// 方法
/**
 * 加载面包屑
 * @param matched
 */
function loadBreadcrumb(matched: RouteLocationMatched[]) {
  // 1. 如果起点不是 首页，那么默认添加首页面包屑
  const firstMatched = matched[0]
  if (firstMatched.path !== "/") {
    let homeMeta: RouteMeta = HOME_ROUTE_RAW.meta!;
    // 1. 1 防止 homeMeta 未设置 meta ， 而是在子元素定义
    if (!HOME_ROUTE_RAW.meta?.title || HOME_ROUTE_RAW.meta?.title == "") {
      // 1. 2 不在子元素不在父元素，则显示空即可
      homeMeta = HOME_ROUTE_RAW.children?.[0]?.meta!
    }
    // 1. 3 添加首页到面包屑
    breadcrumbs.value.push({
      path: "/",
      meta: homeMeta
    })
  }
  // 2. 处理当前路由
  matched.forEach((item) => {
    // 2. 1 加入所有可加入面包屑的路由
    if (item.meta && item.meta.title && item.meta.breadcrumb !== false) {
      breadcrumbs.value.push({
        path: item.path,
        redirect: item.redirect as string,
        meta: item.meta
      })
    }
  })
}

// 生命周期


</script>

<style scoped lang="scss">
/* 样式 */
.breadcrumb-container {
  padding-right: 15px;

  :deep(.el-breadcrumb) {
    display: flex;

    a {
      display: flex;
    }
  }

  :deep(.el-breadcrumb__inner) {
    display: flex;
    align-items: center;
    cursor: pointer !important;
  }
}
</style>
