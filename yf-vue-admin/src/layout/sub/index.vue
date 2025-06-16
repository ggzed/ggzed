<template>
  <router-view #default="{ Component, route }">
    <!--      https://vuejs.org/guide/built-ins/transition.html  -->
    <transition :enter-active-class="`animate__animated ${systemStore.settings.animateCss}`">
      <keep-alive :include="cachedViews">
        <component :is="Component" :key="route.path"/>
      </keep-alive>
    </transition>
  </router-view>
</template>

<script lang="ts" setup>
import {useSystemStore} from "@/store/modules/system";
import {useTagsViewStore} from "@/store/modules/tagsView";

const systemStore = useSystemStore();
const cachedViews = computed(() => useTagsViewStore().cachedViews); // 缓存页面集合
</script>

<style lang="scss" scoped>

</style>