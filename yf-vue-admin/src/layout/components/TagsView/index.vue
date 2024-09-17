<template>
  <div ref="tagsViewContainer" class="tags-view-container">
    <!--
        @click.middle         鼠标中键删除当前tag
        @contextmenu.prevent  右键打开菜单
      -->
    <vue-draggable
        :list="visitedViews"
        class="tags-view-container-draggable"
        item-key="fullPath"
    >
      <template #item="{ element }">
        <router-link
            :key="element.fullPath"
            :class="{'active' : element.path === route.path}"
            :to="{ path: element.path, query: element.query }"
            class="tags-view-container-item"
            @click.middle="!element?.affix ? closeSelectedTag(element) : ''"
            @contextmenu.prevent="openContentMenu(element, $event)"
        >
          <span>{{ element.title }}</span>
          <el-icon v-if="!element?.affix" :size="12">
            <Close
                v-if="!element?.affix"
                class="close-icon"
                @click.prevent.stop="closeSelectedTag(element)"
            />
          </el-icon>
        </router-link>
      </template>
    </vue-draggable>

    <!-- tag标签操作菜单 -->
    <ul
        v-show="contentMenuVisible"
        :style="{ left: left + 'px', top: top + 'px' }"
        class="contextmenu"
    >
      <li @click="refreshSelectedTag(selectedTag)">
        <el-icon>
          <RefreshRight/>
        </el-icon>
        <span>刷新</span>
      </li>
      <li v-if="!selectedTag?.affix" @click="closeSelectedTag(selectedTag)">
        <el-icon>
          <Close/>
        </el-icon>
        <span>关闭</span>
      </li>
      <li @click="closeOtherTags">
        <el-icon>
          <ScaleToOriginal/>
        </el-icon>
        <span>关闭其它</span>
      </li>
      <li @click="closeAllTags(selectedTag)">
        <el-icon>
          <Postcard/>
        </el-icon>
        <span>关闭所有</span>
      </li>
    </ul>
  </div>
</template>

<script lang="ts" setup>
// 数据
import {TagView, useTagsViewStore} from "@/store/modules/tagsView";
import {RouteRecordRaw} from "vue-router";
import {useUserStore} from "@/store/modules/user";
import {Close, Postcard, RefreshRight, ScaleToOriginal} from "@element-plus/icons-vue";
import {RouteConstant} from "@/constants/route";


const {proxy} = getCurrentInstance()!;        // 获取当前组件实例 , $el: 一个指向组件根元素的引用

const router = useRouter();
const route = useRoute();
const tagsViewStore = useTagsViewStore();
const userStore = useUserStore();
const {visitedViews} = storeToRefs(tagsViewStore);
const contentMenuVisible = ref(false);        // 右键菜单是否显示
const tagsViewContainer = ref();              // 当前 tagsContainer 元素

const selectedTag = ref<TagView>({
  path: "",
  fullPath: "",
  name: "",
  title: "",
  affix: false,
  keepAlive: false,
});                                           // 标记当前选中的tag

const affixTags = ref<TagView[]>([]);         // 标记固定的tag
const left = ref(0);                          // 右键菜单定位
const top = ref(0);                           // 右键菜单定位

// 方法
// watch => 当打开右键菜单时，点击屏幕任何地方都会关闭菜单
watch(contentMenuVisible, (value) => {
  if (value) {
    document.body.addEventListener("click", closeContentMenu);
  } else {
    document.body.removeEventListener("click", closeContentMenu);
  }
});

watch(
    route,
    () => {
      if (route.meta.title) {
        const tagView: TagView = {
          name: route.name as string,
          title: route.meta.title,
          path: route.path,
          fullPath: route.fullPath,
          affix: route.meta?.affix,
          keepAlive: route.meta?.keepAlive,
          query: route.query
        }
        // 滑动滚动条
        if (!visitedViews.value.some((item) => item.path === tagView.path)) {
          nextTick(() => {
            tagsViewContainer.value.scrollLeft = tagsViewContainer.value?.scrollWidth | 0
          })
        }
        // 处理页签
        tagsViewStore.saveVisitedView(tagView)
        tagsViewStore.saveCachedView(tagView)
        updateCurrentTag();

      }
    },
    {
      immediate: true, //初始化立即执行
    }
);

/**
 * 初始化 tags
 */
function initTags() {
  const tags: TagView[] = filterAffixTags(userStore.routes);
  affixTags.value = tags;
  for (const tag of tags) {
    // 菜单有名字才添加
    if (tag.name) {
      tagsViewStore.saveVisitedView(tag);
    }
  }
}

/**
 * 打开右键菜单
 * @param tag
 * @param e
 */
function openContentMenu(tag: TagView, e: MouseEvent) {
  const menuMinWidth = 105;                                   // 右键菜单最小宽度
  const offsetLeft = proxy?.$el.getBoundingClientRect().left; // 获取容器左边距
  const offsetWidth = proxy?.$el.offsetWidth;                 // 获取容器宽度
  const maxLeft = offsetWidth - menuMinWidth;                 // 计算左边界
  const l = e.clientX - offsetLeft + 15;                      // 计算菜单左侧位置（加上右边距）

  if (l > maxLeft) {
    left.value = maxLeft;                                     // left 定位
  } else {
    left.value = l;                                           // left 定位
  }
  top.value = e.clientY                                       // top定位
  contentMenuVisible.value = true;                            // 显示内容菜单
  selectedTag.value = tag;                                    // 设置选定的标签
}

/**
 * 关闭菜单
 */
function closeContentMenu() {
  contentMenuVisible.value = false;
}

/**
 * 更新当前页签信息
 * 使用 nextTick() 方法确保在页面更新后执行
 */
function updateCurrentTag() {
  nextTick(() => {
    // 遍历已访问的页签
    for (const tag of visitedViews.value) {
      // 如果页签的路径与当前路由路径相同更新
      if (tag.path === route.path) {
        // fullPath 相同则不更新
        if (tag.fullPath !== route.fullPath) {
          // 更新已访问页签信息
          tagsViewStore.updateVisitedView({
            name: route.name as string,
            title: route.meta.title || "",
            path: route.path,
            fullPath: route.fullPath,
            affix: route.meta?.affix,
            keepAlive: route.meta?.keepAlive,
            query: route.query
          });
        }
      }
    }
  });
}

/**
 * 删除当前选中的 tag
 * @param tag 当前 tag
 */
function closeSelectedTag(tag: TagView) {
  // 最后一个页签无法删除
  if (visitedViews.value.length > 1) {
    // 1. 存储临时 tags
    const tempTags = [...visitedViews.value];
    // 2. 删除 tag
    tagsViewStore.removeTagView(tag);
    // 3. 删除 缓存tag
    tagsViewStore.removeCachedView(tag);
    // 4. 移动 tag
    nextTick(() => {
      moveToPervTag(tempTags, tag);
    })
  }
}

/**
 * 刷新当前标签
 * @param tag 当前标签
 */
function refreshSelectedTag(tag: TagView) {
  // 删除缓存的页签
  tagsViewStore.removeCachedView(tag)
  // 刷新当前标签
  const {fullPath} = tag
  nextTick(() => {
    router.replace({path: "/redirect" + fullPath});
  });
}

/**
 * 关闭其他标签
 */
function closeOtherTags() {
  // 关闭其他逻辑 : 清除所有缓存，找出除固定页签以外的页签和当前页签不删除，其余删除
  const affixFilter = affixTags.value.filter(item => item.path !== selectedTag.value.path);
  tagsViewStore.setVisitedView(affixFilter.concat([selectedTag.value]))
  tagsViewStore.setCachedView([selectedTag.value.name])
  // 跳转页面
  nextTick(() => {
    router.replace({path: selectedTag.value.path});
  })
}

/**
 * 关闭所有 tags
 */
function closeAllTags(tag: TagView) {
  // 清除缓存和添加固定页签
  tagsViewStore.setVisitedView([...affixTags.value])
  tagsViewStore.setCachedView([])
  // 跳转页面
  nextTick(() => {
    moveToPervTag(visitedViews.value, tag)
  })
}

/**
 * 移动到上一个标签
 * @param currentTags 当前的 tags 集合
 * @param tag 页签
 */
function moveToPervTag(currentTags: TagView[], tag?: TagView) {
  const index = currentTags.findIndex((item) => item.path === tag?.path);
  // 异常情况 , 比如 index 找不到
  if (index === -1) {
    router.push(RouteConstant.HOME_PATH)
    return
  }
  // 异常情况 , 比如 index === 0 但是无法前进一个页签
  if (index === 0 && currentTags.length === 1) {
    router.push(currentTags[0].path)
    return;
  }
  // 正常情况下向前推进
  if (index === 0) {
    // 当前标签是最后一个标签
    router.push(currentTags[index + 1].path);
  } else {
    // 移动到下一个标签
    router.push(currentTags[index - 1].path);
  }
  /*
  moveToNextTag
  if (index === currentTags.length - 1) {
    // 当前标签是最后一个标签
    router.push(currentTags[index - 1].path);
  } else {
    // 移动到下一个标签
    router.push(currentTags[index + 1].path);
  }
   */
}

/**
 * 过滤出需要固定的标签
 * @param menus   所有菜单
 * @param basePath basePath
 */
function filterAffixTags(menus: RouteRecordRaw[], basePath = "") {
  let tags: TagView[] = [];
  menus.forEach((menu: RouteRecordRaw) => {
    // 解析出 tag 路径
    const tagPath: string = (basePath + '/' + menu.path).replace(/\/\//g, "/");
    if (menu.meta?.affix) {
      tags.push({
        path: tagPath,
        fullPath: tagPath,
        name: String(menu.name),
        title: menu.meta?.title || "no-name",
        affix: menu.meta?.affix,
        keepAlive: menu.meta?.keepAlive,
      });
    }
    if (menu.children) {
      // 递归
      const tempTags = filterAffixTags(menu.children, basePath + menu.path);
      if (tempTags.length >= 1) {
        // ...tempTags,...tags  先添加的在前面 , ...tags,...tempTags 后添加的在前面
        tags = [...tempTags, ...tags];
      }
    }
  });
  return tags;
}

// 生命周期
onMounted(() => {
  initTags();
});
</script>

<style lang="scss" scoped>
/* 样式 */

$tags-view__shadow: inset 0 0 4px rgba(0, 0, 0, 0.1);
.tags-view-container {
  display: flex;
  justify-content: left;
  align-items: center;
  padding: 0 10px 0 15px;
  width: 100%;
  height: $tags-view-height;
  overflow-x: auto;
  overflow-y: hidden;
  box-shadow: $tags-view__shadow;

  &-draggable {
    display: flex;
  }

  &-item {
    display: flex;
    align-items: center;
    padding: 3px 8px;
    margin: 4px 0 0 5px;
    font-size: 12px;
    cursor: pointer;
    border: 1px solid var(--el-border-color-light);
    white-space: nowrap;

    > span {
      margin-right: 4px;
    }

    &:hover {
      color: var(--el-color-primary);
    }

    .close-icon {
      border-radius: 50%;

      &:hover {
        color: #fff;
        background-color: var(--el-color-primary);
      }
    }

    &.active {
      color: #fff;
      background-color: var(--el-color-primary);

      &::before {
        display: inline-block;
        width: 8px;
        height: 8px;
        margin-right: 5px;
        content: "";
        background: #fff;
        border-radius: 50%;
      }

      .close-icon:hover {
        color: var(--el-color-primary);
        background-color: var(--el-fill-color-light);
      }
    }
  }
}

.contextmenu {
  position: absolute;
  z-index: 99;
  font-size: 12px;
  background: var(--el-bg-color-overlay);
  border-radius: 4px;
  box-shadow: var(--el-box-shadow-light);
  overflow: hidden;

  li {
    display: flex;
    justify-content: left;
    align-items: center;
    padding: 8px 16px;
    cursor: pointer;

    .el-icon {
      font-size: 18px;
      margin-right: 4px;
    }

    &:hover {
      background: var(--el-fill-color-light);
    }
  }
}
</style>
