<template>
  <div class="app-container">
    <Markdown v-model="value" :mode="'preview'" save-path="demo/markdown"/>
    <!--    <div v-html="test"></div>-->
  </div>
</template>

<script lang="ts" setup>
defineOptions({
  name: "Markdown",
  inheritAttrs: false,
});
const value = ref(`[[toc]]

# Markdown

[Gitee 源码 ](https://gitee.com/fateyifei/yf)

使用 v-md-editor 进行实现 ,目前集成 : Tip 提示插件 、Emoji 表情插件 、Katex 插件、Mermaid 插件（流程图等）、Align 内容定位 、TodoList 任务列表 、Highlight Lines 高亮代码行 & Copy Code 快捷复制代码

## Tip 提示插件

::: details
  详细信息 ：调整图片大小
  ![测试](https://foruda.gitee.com/avatar/1677159966647474882/9026884_fateyifei_1638627169.png){{{width="50" height="50"}}}
:::

::: tip
  在此输入内容
:::

::: warning
  在此输入内容
:::

::: danger
  在此输入内容
:::


## Emoji 表情插件
:grinning: :m: :black_nib:
:tired_face: :hotsprings: :scream:
:scream: :sparkles: :airplane:

## Katex 插件
$$\\sum_{i=1}^n a_i=0$$

## Mermaid 插件（流程图等）

\`\`\`mermaid
graph LR
A --- B
B-->C[fa:fa-ban forbidden]
B-->D(fa:fa-spinner);
\`\`\`

## Align 内容定位

::: align-left
左对齐
:::

::: align-center
居中
:::

::: align-right
右对齐
:::

## TodoList 任务列表

- [x] 已完成任务
- [ ] 未完成任务

## Highlight Lines 高亮代码行 & Copy Code 快捷复制代码

\`\`\` js {1,3}
import VueMarkdownEditor from '@kangc/v-md-editor';
import createHighlightLinesPlugin from '@kangc/v-md-editor/lib/plugins/highlight-lines/index';

VueMarkdownEditor.use(createHighlightLinesPlugin());
\`\`\`
`)
</script>

<style lang="scss" scoped>
/* 样式 */

</style>
