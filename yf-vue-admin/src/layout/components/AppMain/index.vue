<template>
  <div :class="{
    'main-tags-view' :systemStore.settings.tagsView
  }" class="main-container">
    <!--    <router-view #default="{ Component, route }">-->
    <!--      &lt;!&ndash;      https://vuejs.org/guide/built-ins/transition.html  &ndash;&gt;-->
    <!--      <transition :enter-active-class="`animate__animated ${systemStore.settings.animateCss}`"-->
    <!--                  :duration="{enter:260,leave:0}">-->
    <!--        <keep-alive :include="cachedViews">-->
    <!--          <component :is="Component" :key="route.path"/>-->
    <!--        </keep-alive>-->
    <!--      </transition>-->
    <!--    </router-view>-->
    <!--        使用异步组件 : 1 . 只会在第一次访问的时候加载 fallback 2. 支持顶部await
                // 写法举例 1 :
                const menuDict = await useDictionary('menu')
                // 写法举例 2 :
                const menuDict = ref<Record<number | string, string>>({});
                onBeforeMount(async () => {
                    menuDict.value = await useDictionary("menu");
                })
     -->
    <Suspense>
      <template #default>
        <!-- 具体内容   -->
        <router-view #default="{ Component, route }">
          <!--      https://vuejs.org/guide/built-ins/transition.html  -->
          <transition :enter-active-class="`animate__animated ${systemStore.settings.animateCss}`">
            <keep-alive :include="cachedViews">
              <component :is="Component" :key="route.path"/>
            </keep-alive>
          </transition>
        </router-view>
      </template>
      <template #fallback>
        <el-skeleton :rows="14" animated/>
      </template>
    </Suspense>
  </div>
</template>

<script lang="ts" setup>
import {useTagsViewStore} from "@/store/modules/tagsView";
import {useSystemStore} from "@/store/modules/system";

const systemStore = useSystemStore();
const cachedViews = computed(() => useTagsViewStore().cachedViews); // 缓存页面集合
</script>

<style lang="scss" scoped>
/* 样式 */
.main-container {
  padding: 10px;
  min-height: calc(100vh - $navbar-height);

  .app-container {
    min-height: calc(100vh - $navbar-height - 20px);
  }
}

.main-tags-view {
  height: calc(100vh - $navbar-height - $tags-view-height);

  .app-container {
    height: calc(100vh - $navbar-height - $tags-view-height - 20px);
  }
}
</style>
