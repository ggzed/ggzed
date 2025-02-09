import {App} from "vue";
import VueMarkdownEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css';
import '@kangc/v-md-editor/lib/plugins/mermaid/mermaid.css';
import '@kangc/v-md-editor/lib/plugins/todo-list/todo-list.css';
import '@kangc/v-md-editor/lib/plugins/highlight-lines/highlight-lines.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import createEmojiPlugin from '@kangc/v-md-editor/lib/plugins/emoji/index';
import createKatexPlugin from '@kangc/v-md-editor/lib/plugins/katex/cdn';
import createCopyCodePlugin from '@kangc/v-md-editor/lib/plugins/copy-code/index';
import createAlignPlugin from '@kangc/v-md-editor/lib/plugins/align';
import createMermaidPlugin from '@kangc/v-md-editor/lib/plugins/mermaid/cdn';
import createTodoListPlugin from '@kangc/v-md-editor/lib/plugins/todo-list/index';
import createHighlightLinesPlugin from '@kangc/v-md-editor/lib/plugins/highlight-lines/index';

// 代码高亮
import Prism from 'prismjs';

/**
 * 注册 MarkdownEditor
 * @param app
 */
export function setupVueMarkdownEditor(app: App<Element>) {
    // 使用主题和插件
    VueMarkdownEditor.use(vuepressTheme, {
        Prism,
        codeHighlightExtensionMap: {
            vue: 'html',
        },
        config: {
            toc: {
                includeLevel: [1, 2, 3, 4, 5, 6],
            },
        }
    });
    // 支持 emoji 插件
    VueMarkdownEditor.use(createEmojiPlugin());
    // 支持插入 katex 公式
    VueMarkdownEditor.use(createKatexPlugin());
    // 支持复制代码
    VueMarkdownEditor.use(createCopyCodePlugin());
    // 支持对齐方式
    VueMarkdownEditor.use(createAlignPlugin());
    // 支持 mermaid 图表
    VueMarkdownEditor.use(createMermaidPlugin());
    // 支持 todoList
    VueMarkdownEditor.use(createTodoListPlugin());
    // 支持高亮行
    VueMarkdownEditor.use(createHighlightLinesPlugin());
    // 注册到 Vue 应用
    app.use(VueMarkdownEditor);
}
