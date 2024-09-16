<template>
  <div class="navbar-right-container">
    <!-- 全屏 -->
    <div v-if="systemStore.app.device !== DeviceEnum.MOBILE"
         class="navbar-right-container-item" @click="toggle">
      <svg-icon :icon-class="isFullscreen ? 'fullscreen-exit' : 'fullscreen'"/>
    </div>
    <!-- 消息通知  -->
    <div class="navbar-right-container-item">
      <el-dropdown trigger="click">
        <el-badge is-dot>
          <svg-icon icon-class="notify"></svg-icon>
        </el-badge>
        <template #dropdown>
          <el-dropdown-menu @command="handleCommand">
            <el-dropdown-item command="none">消息通知正在完善中...</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
    <!-- 聊天室  -->
    <div class="navbar-right-container-item">
      <el-badge is-dot>
        <svg-icon icon-class="chat-room" @click="goToNotifyPage()"></svg-icon>
      </el-badge>
    </div>
    <!-- 打开设置 -->
    <div class="navbar-right-container-item">
      <svg-icon icon-class="setting" @click="systemStore.setSettingsVisible(true)"/>
    </div>
    <!-- 用户名 + 头像   -->
    <div class="navbar-right-container-item">
      <el-dropdown trigger="click" @command="handleCommand">
        <div class="el-dropdown__userInfo">
          <el-avatar :size="24" :src="userStore.userInfo.avatar"></el-avatar>
          <el-tooltip
              :content="userStore.userInfo.nickname"
              effect="dark"
          >
            <span v-show="systemStore.app.device !== DeviceEnum.MOBILE"
                  class="el-dropdown__userInfo__username">{{ userStore.userInfo.nickname }}</span>
          </el-tooltip>
        </div>
        <template #dropdown>
          <el-dropdown-menu @command="handleCommand">
            <el-dropdown-item command="user-profile">个人中心</el-dropdown-item>
            <el-dropdown-item command="gitee">GITEE 源码</el-dropdown-item>
            <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {useSystemStore} from "@/store/modules/system";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {useUserStore} from "@/store/modules/user";
import {AuthAPI} from "@/api/auth";
// 数据
const {isFullscreen, toggle} = useFullscreen();
const systemStore = useSystemStore();
const userStore = useUserStore();
const router = useRouter();

const rightPadding = computed(() => {
  return systemStore.app.device === DeviceEnum.MOBILE ? "8px" : "12px";
})

// 定义一个对象映射，将命令字符串映射到相应的处理函数
const commandActions: { [key: string]: () => void } = {
  'user-profile': () => router.push('/user-profile'), // 当命令为 'user-profile' 时，跳转到用户个人中心页面
  'gitee': () => window.open('https://gitee.com/fateyifei/yf-vue3-admin-base', '_blank'), // 当命令为 'gitee' 时，在新标签页中打开 GITEE 源码页面
  'logout': () => { // 当命令为 'logout' 时，执行退出登录操作
    AuthAPI.LOGOUT.request().then(() => {
      userStore.resetToken(); // 调用用户存储的方法来重置令牌
    });
  }
};

// 处理接收到的命令
function handleCommand(command: string) {
  // 检查命令是否在映射对象中存在
  if (commandActions.hasOwnProperty(command)) {
    commandActions[command](); // 如果存在，则执行对应的处理函数
  }
}

/**
 * 目前跳转为聊天页面
 * 后期需要改为跳转到通知页面
 */
function goToNotifyPage() {
  router.push('/websocket/chat-room')
}

</script>

<style lang="scss" scoped>
/* 样式 */
.navbar-right-container {
  display: flex;
  justify-content: right;
  align-items: center;

  &-item {
    padding: 0 v-bind(rightPadding);
  }
}

.el-dropdown__userInfo {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  cursor: pointer;

  &__username {
    padding: 0 v-bind(rightPadding);
    max-width: 108px;
    white-space: nowrap; /* 防止文本换行 */
    overflow: hidden; /* 隐藏溢出的部分 */
    text-overflow: ellipsis; /* 显示省略号 */
  }
}

.el-badge {
  display: flex;
}

</style>
