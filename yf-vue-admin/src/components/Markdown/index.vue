<template>
  <v-md-editor v-model="modelValue"
               :autofocus="props.autofocus"
               :default-fullscreen="props.defaultFullscreen"
               :default-show-toc="props.defaultShowToc"
               :disabled-menus="[]"
               :height="props.height"
               :include-level="[1, 2, 3, 4, 5, 6]"
               :left-toolbar="props.leftToolBar"
               :mode="props.mode"
               :placeholder="props.placeholder"
               :right-toolbar="props.rightToolBar"
               :table-size="props.tabSize"
               :toc-nav-position-right="props.tocNavPositionRight"
               :toolbar="toolbar"
               @upload-image="handleUploadImage"
  />
</template>

<script lang="ts" setup>
import {FileAPI} from "@/api/file";
// 数据
const props = withDefaults(defineProps<{
  modelValue: string;
  savePath?: string;
  mode?: "editable" | "preview" | "edit";
  height?: string;
  tocNavPositionRight?: boolean;
  defaultFullscreen?: boolean;
  defaultShowToc?: boolean;
  autofocus?: boolean;
  leftToolBar?: string;
  tabSize?: number;
  rightToolBar?: string;
  placeholder?: string;
}>(), {
  height: "100%",
  savePath: "",
  mode: "editable",
  tabSize: 2,
  tocNavPositionRight: false,
  defaultShowToc: false,
  defaultFullscreen: false,
  autofocus: false,
  leftToolBar: "undo redo | h bold italic strikethrough quote | ul ol table hr customAlign | link image code | tip emoji customTodoList customToc| save",
  rightToolBar: "preview toc fullscreen",
  placeholder: "请输入内容",
});

const emit = defineEmits<{
  (event: 'update:modelValue', value: string): void;
}>();

const modelValue = useVModel(props, 'modelValue', emit)
// toolbar
const toolbar = {
  customAlign: {
    title: '对齐',
    icon: 'v-md-icon-align',
    menus: [
      {
        name: 'left',
        text: '左对齐',
        action(editor: any) {
          editor.insert(function (selected: string | null) {
            return {
              text: `::: align-left\n${selected || "左对齐"}\n:::`,
              selected: selected || "左对齐",
            };
          });
        }
      },
      {
        name: 'center',
        text: '居中',
        action(editor: any) {
          editor.insert(function (selected: string | null) {
            return {
              text: `::: align-center\n${selected || "居中"}\n:::`,
              selected: selected || "居中",
            };
          });
        }
      },
      {
        name: 'right',
        text: '右对齐',
        action(editor: any) {
          editor.insert(function (selected: string | null) {
            return {
              text: `::: align-right\n${selected || "右对齐"}\n:::`,
              selected: selected || "右对齐",
            };
          });
        }
      }
    ]
  },
  customTodoList: {
    title: '任务列表',
    icon: 'v-md-icon-checkbox',
    menus: [
      {
        name: 'completedTasks',
        text: '已完成任务',
        action(editor: any) {
          editor.insert(function (selected: string | null) {
            const prefix = '- [x] ';
            const placeholder = '已完成任务';
            const content = selected || placeholder;

            return {
              text: `${prefix}${content}`,
              selected: content,
            };
          });
        }
      },
      {
        name: 'pendingTasks',
        text: '未完成任务',
        action(editor: any) {
          editor.insert(function (selected: string | null) {
            const prefix = '- [ ] ';
            const placeholder = '未完成任务';
            const content = selected || placeholder;

            return {
              text: `${prefix}${content}`,
              selected: content,
            };
          });
        },
      },
    ],
  },
  customToc: {
    title: '插入目录',
    icon: 'v-md-icon-directory',
    action(editor: any) {
      editor.insert(function (selected: string | null) {
        return {
          text: `\n[[toc]]\n\n`,
        };
      })
    }
  }
}

// 方法
/**
 * 上传本地图片
 */
function handleUploadImage(event: any, insertImage: any, files: File[]) {
  // 获取文件
  const file = files[0];
  // 去掉文件名后缀
  const fileNameWithoutExtension = file.name.replace(/\.[^/.]+$/, "");
  // 上传文件后填入 Markdown
  FileAPI.UPLOAD.request(props.savePath, file).then(({data}) => {
    insertImage({
      url: data,
      desc: fileNameWithoutExtension,
      width: 'auto',
      height: 'auto',
    });
  })
}
</script>

<style lang="scss">
/* 样式 */
.v-md-icon-align:before {
  content: "\2637";
}

.v-md-icon-directory:before {
  content: "\2633"
}
</style>
