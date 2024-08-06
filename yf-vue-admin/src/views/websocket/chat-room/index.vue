<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--  用户列表    -->
      <el-col v-if="device !== DeviceEnum.MOBILE" :lg="6" :md="7" :sm="8" :xs="0">
        <user-container v-model:receiver-user="receiverUser" :current-user-id="userStore.userInfo.userId"
                        :on-select-user="handleSelectUser" :user-list="userList"/>
      </el-col>
      <!--  消息列表    -->
      <el-col :lg="18" :md="17" :sm="16" :xs="24">
        <message-container ref="messageContainerRef"
                           v-model:message="message"
                           :device="device"
                           :handel-mobile-user="()=>{mobileDrawer = true}"
                           :handel-scroll="throttledHandelScroll" :message-list="messageList"
                           :receiver-user="receiverUser"
                           :send-message="handelSendMessage"/>
      </el-col>
    </el-row>
    <!--  移动端用户抽屉  -->
    <el-drawer v-if="device === DeviceEnum.MOBILE" v-model="mobileDrawer" :show-close="false" size="282px">
      <user-container v-model:receiver-user="receiverUser" :current-user-id="userStore.userInfo.userId"
                      :height="'88vh'" :on-select-user="handleSelectUser"
                      :user-list="userList"/>
    </el-drawer>
  </div>
</template>

<script lang="ts" setup>
import {useUserStore} from "@/store/modules/user";
import {RequestConstant} from "@/constants/request";
import {AI_USER, CHAT_ROOM_HEARTBEAT_MESSAGE, ChatRoomAPI, PUBLIC_USER, SYSTEM_USER} from "@/api/websocket/chat-room";
import {ChannelTypes, ChatRoomMessage, ChatRoomUser, ChatRoomUserQuery,} from "@/api/websocket/chat-room/type";
import {useSystemStore} from "@/store/modules/system";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {useTagsViewStore} from "@/store/modules/tagsView";
import {SocketMessagePageQuery, SocketMessageVO} from "@/api/websocket/message/type";
import {MessageAPI} from "@/api/websocket/message";
import {useToNumber} from "@vueuse/core";

defineOptions({
  name: "ChatRoom",
  inheritAttrs: false,
});

const userStore = useUserStore();
const tagsViewStore = useTagsViewStore();
const messageContainerRef = ref();
const token = RequestConstant.Header.AuthorizationPrefix + userStore.authInfo.accessToken;   // Token , 连接失败并不会进行token刷新
const connectUrl = import.meta.env.VITE_APP_API_URL.replace("http", "ws") + ChatRoomAPI.CONNECT.endpoint;  // 连接路径
const device = computed(() => useSystemStore().app.device);
const chatRoomUserQuery: ChatRoomUserQuery = reactive({
  pageNum: 1,
  pageSize: 10
})                                                                         // 查询用户参数
const socketMessageQuery: SocketMessagePageQuery = reactive({
  pageNum: 1,
  pageSize: 20
})                                                                         // 聊天数据分页查询
const messageCount = ref<number>(0);                                       // 消息总条数记录
const userList = ref<ChatRoomUser[]>([{...SYSTEM_USER}, {...AI_USER}, {...PUBLIC_USER}])  // 用户列表
const receiverUser = ref<ChatRoomUser>({...SYSTEM_USER})                    // 当前选择用户 (默认 : 系统消息)

const messageList = ref<SocketMessageVO[]>([])                             // 消息集合
const message = ref<ChatRoomMessage>({
  channel: ChannelTypes.SYSTEM                                             // 默认进入系统频道
})
const mobileDrawer = ref(false);                                           // 适配移动端弹出用户

/**
 * Socket连接处理
 */
const {send, status, open, close} = useWebSocket(`${connectUrl}?Authorization=${token}`, {
  onConnected: (ws: WebSocket) => {
    ElNotification({
      title: '欢迎您进入聊天室',
      message: 'Websocket 聊天室连接成功',
      type: 'success',
    })
  },
  onMessage: async (ws: WebSocket, event: MessageEvent) => {
    // 1. 消息解析
    const data: ChatRoomMessage = JSON.parse(event.data);
    if (!data) {
      return;
    }
    // 2. 消息策略处理
    const handlerMessage = handleChannelMessage[data.channel];
    if (handlerMessage) {
      await handlerMessage(data);
    } else {
      console.error("未知频道");
    }
  },
  onDisconnected: (ws: WebSocket, event: CloseEvent) => {
    ElNotification({
      title: '您已经退出聊天室',
      message: 'Websocket 聊天室连接关闭',
      type: 'info',
    })
  },
  onError: (ws: WebSocket, event: Event) => {
    console.error('Websocket 聊天室连接发生错误', ws, event);
    ElNotification({
      title: 'Websocket 聊天室连接发生错误',
      message: 'Websocket 聊天室连接发生错误 , 如果是 BUG 请联系管理员',
      type: 'error',
    })
  },
  heartbeat: {
    message: JSON.stringify(CHAT_ROOM_HEARTBEAT_MESSAGE),
    interval: 15000,
    pongTimeout: 15000,
  },
  immediate: false
});

/**
 * 处理消息逻辑
 */
const handleChannelMessage = {
  [ChannelTypes.HEARTBEAT]: (data: ChatRoomMessage) => {
    // Handle heartbeat if needed
  },
  [ChannelTypes.SYSTEM]: (data: ChatRoomMessage) => {
    // 1. SYSTEM , 群发消息
    addPublicMessageToList(data, data.senderId, SYSTEM_USER.userId);
  },
  [ChannelTypes.AI]: (data: ChatRoomMessage) => {
    // Handle AI
  },
  [ChannelTypes.PUBLIC]: (data: ChatRoomMessage) => {
    // 1. PUBLIC , 群发消息
    addPublicMessageToList(data, data.senderId, PUBLIC_USER.userId);
  },
  [ChannelTypes.WELCOME]: async (data: ChatRoomMessage) => {
    // 1. WELCOME , 欢迎消息
    const loginUserId = matchDigits(data.content || '');
    if (loginUserId == undefined) {
      return;
    }
    await addOrOnlineUser(loginUserId);
    // 2. 添加到群消息中
    data.senderId = "2";
    addMessageToList(data, {...PUBLIC_USER});
  },
  [ChannelTypes.EXIT]: handleExitOrKickOut,
  [ChannelTypes.KICK_OUT]: handleExitOrKickOut,
  [ChannelTypes.PRIVATE]: (data: ChatRoomMessage) => {
    const senderUser = userList.value.find(user => user.userId === data.senderId);
    addMessageToList(data, senderUser, true);
  },
  [ChannelTypes.PERMISSION]: handlePermissionOrRateLimiter,
  [ChannelTypes.RATE_LIMITER]: handlePermissionOrRateLimiter
}

/**
 *  PERMISSION / RATE_LIMITER 处理
 */
function handlePermissionOrRateLimiter(data: ChatRoomMessage) {
  // 1. PERMISSION / RATE_LIMITER , 处理
  ElNotification({
    title: '您的消息未发出',
    message: data.content,
    type: 'warning',
  })
}

/**
 *  EXIT / KICK_OUT 处理
 */
function handleExitOrKickOut(data: ChatRoomMessage) {
  // 1. EXIT / KICK_OUT, 退出消息
  const exitUserId = matchDigits(data.content || '');
  let exitUser = userList.value.find(user => user.userId == exitUserId);
  // 2. 标记用户下线
  if (exitUser) {
    exitUser.online = false;
  }
  // 3. 添加系统消息
  data.senderId = "2";
  addMessageToList(data, {...PUBLIC_USER});
}

/**
 * 添加消息到列表
 * @param data 需要添加的消息
 * @param senderUser 发送着信息
 * @param isRead 是否发送标记消息已读请求
 */
function addMessageToList(data: ChatRoomMessage, senderUser?: ChatRoomUser, isRead: boolean = false) {
  // 1. 判断消息发送着是否是当前正在聊天用户
  if (receiverUser.value.userId === data.senderId) {
    // 2. 如果是接收到当前聊天对象发送的消息
    // 2.1 添加消息到消息列表
    messageList.value.push(roomMessage2Vo(data, senderUser))
    // 2.2 更新滚动条
    messageContainerRef.value.updateScroll();
    // 2.3 设置消息已读
    if (senderUser) {
      senderUser.unreadMessageCount = 0
      senderUser.lastMessage = data.content;
    }
    // 2.4 判断是否需要发送已读请求
    if (isRead) {
      messageIsRead(data.content);
    }
  } else {
    // 3. 如果不是接收到当前聊天对象发送的消息
    if (senderUser) {
      // 3.1 增加消息未读数量和最近一条消息
      senderUser.unreadMessageCount += 1;
      senderUser.lastMessage = data.content;
    }
  }
}

/**
 * 新增公共消息
 * @param data 新增消息
 * @param senderUserId 发送者Id  , 不存在则消息体中存入默认发送者信息
 * @param defaultUserId 默认发送者Id , 当发送者不存在时使用 （即使存在与否都是对默认发送者的lastMessage和未读消息操作）
 */
function addPublicMessageToList(data: ChatRoomMessage, senderUserId: string | undefined, defaultUserId: string | undefined) {
  // 1. 找寻发送者信息
  let userInfo = userList.value.find(user => user.userId === senderUserId);
  // 2. 找寻默认用户信息
  let defaultUserInfo = userList.value.find(user => user.userId === defaultUserId);
  if (userInfo === undefined) {
    // 2.1 当不存在发送者信息，则赋值为默认用户信息
    userInfo = defaultUserInfo;
  }
  // 3.
  if (receiverUser.value.userId === defaultUserId) {
    messageList.value.push(roomMessage2Vo(data, userInfo))
    messageContainerRef.value.updateScroll();
    if (defaultUserInfo) {
      defaultUserInfo.unreadMessageCount = 0;
      defaultUserInfo.lastMessage = data.content;
    }
  } else {
    if (defaultUserInfo) {
      defaultUserInfo.unreadMessageCount += 1;
      defaultUserInfo.lastMessage = data.content;
    }
  }
}

/**
 * 加载历史消息
 */
async function loadSocketMessagePage() {
  // 1. 提前获取到需要的数据
  const userId = userStore.userInfo.userId;
  const receiver = receiverUser.value;
  // 2. 找寻当前用户
  const senderUser = userList.value.find(user => user.userId === userId) as ChatRoomUser;
  // 3. 加载分页数据
  const {data} = await MessageAPI.USER_PAGE.request(socketMessageQuery);
  // 4. 添加消息到消息列表
  messageCount.value = data.total;
  for (let socketMessageVO of data.list) {
    messageList.value.unshift({
      id: socketMessageVO.id,
      senderId: socketMessageVO.senderId,
      receiverId: socketMessageVO.receiverId,
      channel: socketMessageVO.channel,
      content: socketMessageVO.content,
      createTime: socketMessageVO.createTime,
      userInfo: socketMessageVO.senderId === userId ? {...senderUser} : {...receiver}
    })
  }
}

/**
 * 节流向上滚动请求消息
 * @param scrollTop 滚动条高度
 */
const throttledHandelScroll = useThrottleFn(async ({scrollTop}) => {
  if (scrollTop <= 800) {
    // 1. 计算是否需要分页请求
    const totalPages = Math.ceil(messageCount.value / socketMessageQuery.pageSize);
    if (socketMessageQuery.pageNum < totalPages) {
      // 2. 加载消息
      socketMessageQuery.pageNum += 1;
      await loadSocketMessagePage();
      // 3. 标记已读
      await messageIsRead();
    }
  }
}, 300)

/**
 * 添加用户到列表或者标记上线
 * @param userId 需要添加的用户Id
 */
async function addOrOnlineUser(userId: string) {
  // 1. 查询本地是否存在该用户
  const user = userList.value.find(user => user.userId === userId);  // 精度丢失问题
  if (user === undefined) {
    // 2. 不存在则查询用户
    await ChatRoomAPI.USER_ONE.request(userId).then(({data}) => {
      // 2.1  添加用户信息
      data.connectTime = useDateFormat(data.connectTime, 'HH:mm:ss').value;
      userList.value.push(data)
    })
  } else {
    // 3. 本地存在则标记在线
    user.connectTime = useDateFormat(useNow(), 'HH:mm:ss').value;
    user.online = true;
  }
}

/**
 * 选中用户的回调
 * @description 选中用户后，会自动连接到该用户的频道
 */
async function handleSelectUser() {
  // 1. 清空当前 list : ( TODO 可优化 , 可以设置一个缓存消息表防止频繁切换用户 )
  messageList.value = []
  mobileDrawer.value = false;
  socketMessageQuery.receiverId = receiverUser.value.userId;
  socketMessageQuery.pageNum = 1;

  // 2. TODO 群聊区分查询 , 根据不同频道选择消息内容
  if (receiverUser.value.userId === SYSTEM_USER.userId ||
      receiverUser.value.userId === PUBLIC_USER.userId) {
    // 2.1 系统 / 群发消息不采用用户查询
    socketMessageQuery.receiverId = undefined;
    socketMessageQuery.channel = receiverUser.value.channel;
  } else if (receiverUser.value.userId === AI_USER.userId) {
    // 2.2 AI 频道 查询
    socketMessageQuery.channel = AI_USER.channel;
  } else {
    // 2.3 其他则为私人用户
    socketMessageQuery.channel = ChannelTypes.PRIVATE;
  }
  // 3. 加载消息
  await loadSocketMessagePage();
  // 4. 标记消息已读
  await messageIsRead();
  // 5. 更新滚动条
  messageContainerRef.value.updateScroll();
}

/**
 * 处理消息方法逻辑
 * @param message 消息体
 */
async function handelSendMessage(message: ChatRoomMessage) {
  const userId = userStore.userInfo.userId;
  // 1. 发送消息到 socket 通道
  send(JSON.stringify(message));
  // 2. 添加消息到 List 中
  const currentUser = userList.value.find(user => user.userId === userId);
  // 3. 发送者为当前用户
  message.senderId = userId;
  messageList.value.push(roomMessage2Vo(message, currentUser));
}

/**
 * 标记已读消息已读
 * @param message 可传,传入则更新最近一条消息
 */
async function messageIsRead(message?: string) {
  // 1. 更新消息已读 ( TODO 可优化，使用队列一并发送 )
  await MessageAPI.USER_READ.request(receiverUser.value.userId as string)
  // 2. 修改消息未处理数量为0
  receiverUser.value.unreadMessageCount = 0
  if (message) {
    receiverUser.value.lastMessage = message;
  }
}

/**
 * 匹配消息的第一个数字
 * @param message 需要匹配的消息
 */
function matchDigits(message: string): string | undefined {
  const match = message.match(/\d+/);
  if (match && match[0]) {
    return match[0];
  } else {
    console.error("No digits found in the content:", message);
  }
}

/**
 * 聊天室消息转换为VO
 * @param data
 * @param senderUser
 */
function roomMessage2Vo(data: ChatRoomMessage, senderUser?: ChatRoomUser): SocketMessageVO {
  const now = useNow();
  return {
    channel: data.channel,
    content: data.content,
    createTime: useDateFormat(now, 'YYYY-MM-DD HH:mm:ss').value,
    id: useToNumber(useDateFormat(now, 'DDHHmmss').value).value.toString(),
    receiverId: data.receiverId,
    senderId: data.senderId,
    userInfo: senderUser
  };
}

/**
 * 获取当前登录用户为ChatRoomUser
 */
function getCurrentUser(): ChatRoomUser {
  return {
    userId: userStore.userInfo.userId,
    username: userStore.userInfo.username,
    nickname: userStore.userInfo.nickname,
    avatar: userStore.userInfo.avatar,
    connectTime: useDateFormat(new Date(), 'HH:mm:ss').value,
    channel: ChannelTypes.PRIVATE,
    online: true,
    unreadMessageCount: 0,
    lastMessage: '',
  }
}

/**
 * 当进入页面时处理
 */
onMounted(async () => {
  // 1. 新增当前用户到用户列表中
  userList.value.push(getCurrentUser())
  // 2. 请求聊天室其他用户信息到用户列表中
  const {data} = await ChatRoomAPI.USER_PAGE.request(chatRoomUserQuery)
  data.list.forEach(user => {
    user.connectTime = useDateFormat(user.connectTime, 'HH:mm:ss').value;
    userList.value.push(user);
  });
  // 3. 自动请求一次当前点击用户的消息列表
  await handleSelectUser()
})

/**
 * 如果连接关闭则重新连接
 */
onActivated(async () => {
  if (status.value === "CLOSED") {
    // 打开连接
    await open();
  }
})
/**
 * 当关闭tags标签，则关闭连接
 */
onDeactivated(() => {
  if (!tagsViewStore.cachedViews.includes('ChatRoom')) {
    close();
  }
})
</script>

<style lang="scss" scoped>
/* 样式 */
.app-container {
  .el-col {
    border-radius: 4px;
  }

  :deep(.el-drawer__header) {
    display: none;
  }

  :deep(.el-drawer__body) {
    padding: 0;
  }

  //:deep()
}
</style>
