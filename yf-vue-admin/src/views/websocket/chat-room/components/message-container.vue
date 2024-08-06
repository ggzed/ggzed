<template>
  <div class="message-container">
    <el-card>
      <template #header>
        <div class="message-header">
          <div class="message-header__left">
            <el-avatar :size="44" :src="receiverUser.avatar"></el-avatar>
            <h3>{{ receiverUser.nickname }}</h3>
          </div>
          <div class="message-header__right">
            <svg-icon v-if="device === DeviceEnum.MOBILE" icon-class="user" @click="handelMobileUser"></svg-icon>
          </div>
        </div>
      </template>
      <el-scrollbar ref="scrollbarRef" height="330px" style="padding: 10px;" @scroll="handelScroll">
        <div ref="scrollbarDiv">
          <div v-for="messageVO in messageList" :key="messageVO.id" class="message-item">
            <!--   发送者聊天信息     -->
            <div :class="{
              'message-item__sender' : userId === messageVO.senderId,
              'message-item__receiver' : userId !== messageVO.senderId
            }">
              <!--     头像         -->
              <div class="message-item__avatar">
                <el-avatar :size="44" :src="messageVO.userInfo?.avatar"></el-avatar>
              </div>
              <div class="message-item__content">
                <div class="message-item__content__header">
                  <span>{{ messageVO.userInfo?.username }}</span>
                  <time>{{ messageVO.createTime }}</time>
                </div>
                <div class="message-item__content__main">
                  {{ messageVO.content }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-scrollbar>
      <template #footer>
        <div class="message-footer">
          <div class="message-footer-options">
            <div class="message-footer-options__left">
              <!--     颜文字         -->
              <emoji-picker :on-emoji-click="onEmojiClick">
                <template #trigger>
                  <svg-icon icon-class="emoji" size="24px"></svg-icon>
                </template>
              </emoji-picker>
              <svg-icon icon-class="attachment" size="24px"></svg-icon>
              <svg-icon icon-class="other" size="24px"></svg-icon>
            </div>
            <div class="message-footer-options__right">
              <svg-icon icon-class="paper-airplanes" size="22px" @click="throttledSendMessage"></svg-icon>
            </div>
          </div>
          <el-input ref="contentInput" v-model="content" :autosize="{ minRows: 3, maxRows: 3 }" clearable
                    placeholder="请输入你要发送的消息..."
                    resize="none" type="textarea"
                    @keydown="handelSendMessage"></el-input>
        </div>
      </template>
    </el-card>
  </div>
</template>

<script lang="ts" setup>

import {ChannelTypes, ChatRoomMessage, ChatRoomUser} from "@/api/websocket/chat-room/type";
import {useUserStore} from "@/store/modules/user";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {SocketMessageVO} from "@/api/websocket/message/type";
import {ElScrollbar} from "element-plus";

defineOptions({
  name: "MessageContainer",
  inheritAttrs: false,
});

const props = withDefaults(defineProps<{
  receiverUser: ChatRoomUser,        // 当前选中用户
  messageList: SocketMessageVO[],   // 消息列表
  message: ChatRoomMessage,         // 当前消息
  device?: DeviceEnum               // 设备状态
  handelMobileUser?: () => void     // 点击移动端用户列表
  sendMessage: (message: ChatRoomMessage) => void  // 发送消息回调
  handelScroll: ({scrollLeft, scrollTop}: { scrollLeft: number; scrollTop: number }) => void          // 滚动聊天框回调
}>(), {});

const emit = defineEmits<{
  (event: "update:message", message: ChatRoomMessage): void         // v-model 绑定
}>()
const max = ref(0)                                                  // 目前消息高度
const scrollbarRef = ref<InstanceType<typeof ElScrollbar>>()        // 滚动条
const scrollbarDiv = ref<HTMLDivElement>()                          // 滚动条DOM
const contentInput = ref(ElInput);                                  // 消息输入框
const content = ref<string>('')                                     // 消息内容
const userId = computed(() => useUserStore().userInfo?.userId);     // 用户ID
// 方法
const throttledSendMessage = useThrottleFn(() => {
  // 1. 空则清空消息
  if (content.value.trim() === "") {
    content.value = "";
    return
  }
  // 2. 更新 message 值
  const currentMessage: ChatRoomMessage = {
    receiverId: props.receiverUser.userId,
    channel: props.receiverUser.channel || ChannelTypes.PRIVATE,
    content: content.value
  }
  // 3. 清空消息内容
  content.value = "";
  // 4. 更新 messageList
  emit("update:message", currentMessage);
  // 5. 回调此消息
  if (props.sendMessage) {
    props.sendMessage(currentMessage)
  }
  // 6. 获取滚动条最大值,并且移动到最下面
  updateScroll()
}, 500)

/**
 * 发送消息
 */
function handelSendMessage(event: Event) {
  // Ts 校验处理
  if (!(event instanceof KeyboardEvent)) return;
  if (event.shiftKey && event.key === 'Enter') {
    // 处理 shift + enter
    event.preventDefault()
    content.value += '\n';
  } else if (event.key === 'Enter') {
    // 处理发送消息
    event.preventDefault()
    throttledSendMessage()
  }
}

/**
 * 添加表情
 * @param emoji 表情
 */
function onEmojiClick(emoji: string) {
  content.value += emoji;
  contentInput.value.focus();
}

/**
 * 更新滚动条
 */
function updateScroll() {
  nextTick(() => {
    max.value = scrollbarDiv.value!.clientHeight
    scrollbarRef.value!.setScrollTop(max.value)
  })
}

/**
 * 组件暴露滚动条计算方法
 */
defineExpose({
  updateScroll
})
</script>

<style lang="scss" scoped>
/* 样式 */
// 消息体样式
.message-container {

  :deep(.el-card__header) {
    padding: 10px;
  }

  :deep(.el-card__body) {
    padding: 0;
  }

  :deep(.el-card__footer) {
    padding: 10px;
  }

  .message-header {
    display: flex;
    align-items: center;
    justify-content: space-between;

    &__left {
      display: flex;
      gap: 20px;
    }

    &__right {
      .svg-icon {
        cursor: pointer;
      }
    }
  }

  .message-item {
    display: flex;
    margin-bottom: 18px;

    :deep(.el-card__body) {
      padding: 10px;
    }

    &__sender {
      display: flex;
      flex-direction: row-reverse;
      max-width: 80%;
      align-self: flex-end;
      margin-left: auto;
      gap: 12px;

      .message-item__content {
        display: flex;
        flex-direction: column;

        &__header {
          display: flex;
          flex-direction: row-reverse;
          gap: 10px;
          align-items: flex-end;
          height: 24px;
          margin-bottom: 10px;

          > span {
            font-size: 14px;
            font-weight: 700;
          }

          > time {
            font-size: 12px;
          }
        }

        &__main {
          display: inline-block;
          align-self: flex-end; /* 确保内容左对齐 */
          background-color: var(--el-color-success-light-8);
          box-shadow: var(--el-box-shadow-light);
          padding: 10px;
          border-radius: 10px;
          letter-spacing: 1px;
          line-height: 2;
          word-wrap: break-word;
          white-space: pre-wrap; /* Preserve whitespace and allow wrapping */
        }
      }
    }

    &__receiver {
      display: flex;

      max-width: 80%;
      align-self: flex-start;
      margin-right: auto;
      gap: 12px;

      .message-item__content {
        &__header {
          display: flex;
          gap: 10px;
          align-items: flex-end;
          height: 24px;
          margin-bottom: 10px;

          > span {
            font-size: 14px;
            font-weight: 700;
          }

          > time {
            font-size: 12px;
          }
        }

        &__main {
          display: inline-block;
          background-color: var(--el-card-bg-color);
          box-shadow: var(--el-box-shadow-light);
          padding: 10px;
          border-radius: 10px;
          letter-spacing: 1px;
          line-height: 2;
          word-wrap: break-word;
          white-space: pre-wrap; /* Preserve whitespace and allow wrapping */
        }
      }
    }


    .message-item__sender::after,
    .message-item__receiver::after {
      content: "";
      display: block;
      clear: both;
    }
  }

  .message-footer {

    &-options {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 10px 10px;

      .svg-icon {
        cursor: pointer;
      }

      &__left {
        .svg-icon {
          margin-right: 10px;
        }
      }
    }
  }

  h2 {
    margin: 0;
  }
}
</style>
