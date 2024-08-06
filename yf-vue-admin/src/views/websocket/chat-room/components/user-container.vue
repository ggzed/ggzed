<template>
  <div class="user-container">
    <el-card>
      <template #header>
        <el-input :prefix-icon="Search" placeholder="搜索用户..."></el-input>
      </template>
      <el-scrollbar :height="height">
        <!--   用户集合     -->
        <div v-for="user in userList"
             :key="user.userId"
             :class="{'active' : user.userId === receiverUser.userId}"
             @click="handleSelectUser(user)"
        >
          <div v-if="user.userId !== currentUserId" class="user-item">
            <!--   头像分区       -->
            <div class="user-item-avatar">
              <el-badge :type="user.online ? 'success' : 'danger'" is-dot>
                <el-avatar :size="40" :src="user.avatar"></el-avatar>
              </el-badge>
            </div>
            <!--   内容分区       -->
            <div class="user-item-content">
              <div class="user-item-content-title">
                <h4> {{ user.nickname }}</h4>
                <time>{{ user.connectTime }}</time>
              </div>
              <div class="user-item-content-message">
                <div>{{ user.lastMessage }}</div>
                <el-badge v-show="user.unreadMessageCount != 0" :value="user.unreadMessageCount">
                </el-badge>
              </div>
            </div>
          </div>
        </div>
      </el-scrollbar>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
// 数据
import {ChatRoomUser} from "@/api/websocket/chat-room/type";
import {Search} from "@element-plus/icons-vue";

defineOptions({
  name: "UserContainer",
  inheritAttrs: false,
});

const props = withDefaults(defineProps<{
  userList: ChatRoomUser[],       // 用户集合列表
  receiverUser: ChatRoomUser,     // 当前选中用户
  height?: string                 // 高度
  onSelectUser?: () => void,      // 选中用户后的回调
  currentUserId?: string          // 当前用户id
}>(), {
  height: "480px",
  currentUserId: "0"
});

const emit = defineEmits<{
  (event: "update:receiverUser", user: ChatRoomUser): void    // v-model 绑定
}>()

// 方法
/**
 * 选中后用户的回调 onSelectUser 方法
 * @param user
 */
function handleSelectUser(user: ChatRoomUser) {
  // 1. 判断是否是当前用户，
  // 如果是当前用户，则不进行任何操作
  // 否则，执行选中用户后的回调
  if (user.userId === props.receiverUser.userId) {
    return
  }
  // 2. 改变 currentUser的值
  emit("update:receiverUser", user);
  // 3. 执行选中用户后的回调
  if (props.onSelectUser) {
    props.onSelectUser();
  }
}

// 生命周期
defineExpose({
  handleSelectUser
})
</script>

<style lang="scss" scoped>
/* 样式 */
// 用户数据样式
.user-container {
  :deep(.el-card__body) {
    padding: 0;
  }

  .user-item {
    padding: 5px 10px;
    height: 68px;
    display: flex;
    justify-content: left;
    align-items: center;
    gap: 10px;
    cursor: pointer;

    &-avatar {
      :deep(.el-badge__content) {
        top: 35px;
      }
    }

    &-content {
      display: flex;
      width: 100%;
      flex-direction: column;
      gap: 8px;

      &-title {
        display: flex;
        justify-content: space-between;

        h4 {
          margin: 0;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          max-width: 12ch; /* 指定最大宽度为 14 个字符 */
        }
      }

      &-message {
        display: flex;
        justify-content: space-between;
        align-items: center;
        height: 18px;

        > div {
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          max-width: 18ch; /* 指定最大宽度为 18 个字符 */
        }
      }
    }
  }

  .active {
    background-color: var(--el-color-primary-light-9);
  }
}
</style>
