<template>
  <!--  显示未隐藏菜单  -->
  <template v-if="!menuItem.meta || !menuItem.meta.hidden">
    <!-- 显示具有单个子路由的菜单项或没有子路由的父路由 -->
    <template
        v-if="isLeafRoute(menuItem.children!) && !menuItem.meta!.showSingleChildren"
    >
      <AppLink v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path)">
        <el-menu-item
            :index="resolvePath(onlyOneChild.path)"
        >
          <SystemIcon
              :icon="onlyOneChild.meta.icon"
          />
          <template #title>
            <span>{{ onlyOneChild.meta.title }}</span>
          </template>
        </el-menu-item>
      </AppLink>
    </template>

    <!-- 显示具有多个子路由的父菜单项 -->
    <el-sub-menu v-else :index="resolvePath(menuItem.path)">

      <template #title>
        <SystemIcon :icon="menuItem.meta && menuItem.meta.icon"/>
        <span>{{ menuItem.meta?.title }}</span>
      </template>
      <SidebarMenuItem
          v-for="child in menuItem.children"
          :key="child.path"
          :menu-item="child"
          :base-path="resolvePath(child.path)"
      />
    </el-sub-menu>
  </template>
</template>

<script setup lang="ts">
// 数据
import {RouteRecordRaw} from "vue-router";
import {validateURL} from "@/utils/validation";
import {Ref} from "vue";

const props = withDefaults(defineProps<{
  menuItem: RouteRecordRaw,
  basePath: string
}>(), {});

const onlyOneChild: Ref<RouteRecordRaw> = ref<RouteRecordRaw>({...props.menuItem, path: '', children: []});

// 方法
/**
 * 判断是否是叶子节点
 * 判断条件 ： 无子节点或只有一个子节点，表示为叶子节点
 * @param childrenRoute 子节点
 */
function isLeafRoute(childrenRoute: RouteRecordRaw[]): boolean {
  // !childrenRoute 表示传入 undefined
  if (!childrenRoute || childrenRoute.length === 0) {
    return true;
  }
  // 过滤不展示的子节点
  const filterRoute = childrenRoute.filter((item) => !item.meta || !item.meta.hidden);
  // 判断子节点的子节点是否只有一个
  const filterRouteChildren = filterRoute.filter((item) => item.children && item.children.length > 0);
  if (filterRouteChildren.length > 0) {
    return false;
  }
  // 将 onlyOneChild 赋值为节点中的 [0]
  onlyOneChild.value = {...filterRoute[0], children: []}
  return filterRoute.length <= 1;
}

/**
 * 解析路径
 * @param routePath 路由路径
 */
function resolvePath(routePath: string): string {
  if (validateURL(routePath)) {
    return routePath;
  }
  if (validateURL(props.basePath)) {
    return props.basePath;
  }
  // 完整路径(/system/user) = 父级路径(/system) + 路由路径(user)
  if (routePath.startsWith('/')) {
    return routePath;
  }
  if (routePath === "") {
    return props.basePath.replace(/\/$/, '');
  }
  // 否则，将 basePath 与 routePath 拼接成绝对路径
  return props.basePath.replace(/\/$/, '') + '/' + routePath;
}

// 生命周期
</script>

<style scoped lang="scss">
/* 样式 */
// .el-sub-menu__title 会导致 padding-right 多 20px
:deep(.el-sub-menu__title) {
  padding-right: 0;
}
</style>
