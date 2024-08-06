import {ChannelTypes, ChatRoomMessage, ChatRoomUser, ChatRoomUserQuery} from "@/api/websocket/chat-room/type";
import {AxiosPromise} from "axios";
import request from "@/utils/request";

/** 默认用户 */
export const SYSTEM_USER: ChatRoomUser = {
    userId: "0",
    username: "system",
    nickname: "系统消息",
    channel: ChannelTypes.SYSTEM,
    online: true,
    avatar: "https://oss.youlai.tech/youlai-boot/2023/05/16/811270ef31f548af9cffc026dfc3777b.gif",
    connectTime: "23:00",
    unreadMessageCount: 0
}
export const AI_USER: ChatRoomUser = {
    userId: "1",
    username: "ai",
    nickname: "AI助手",
    channel: ChannelTypes.AI,
    online: true,
    avatar: "https://oss.youlai.tech/youlai-boot/2023/05/16/811270ef31f548af9cffc026dfc3777b.gif",
    connectTime: "22:00",
    unreadMessageCount: 0
}
export const PUBLIC_USER: ChatRoomUser = {
    userId: "2",
    username: "public",
    nickname: "群聊",
    channel: ChannelTypes.PUBLIC,
    online: true,
    avatar: "https://oss.youlai.tech/youlai-boot/2023/05/16/811270ef31f548af9cffc026dfc3777b.gif",
    connectTime: "24:00",
    unreadMessageCount: 0
}
/** 聊天室心跳消息 后端限流 60s => 4次 */
export const CHAT_ROOM_HEARTBEAT_MESSAGE: ChatRoomMessage = {
    channel: ChannelTypes.HEARTBEAT,
};

const API_BASE = '/chat-room';

const API_SUFFIXES = {
    /** 获取聊天室用户列表 */
    USER_PAGE: '/page',
    /** 获取单个用户 */
    USER_ONE: '/{userId}'
};

// 定义 ChatRoomAPI 类
export class ChatRoomAPI {
    /**
     * 连接 endpoint
     */
    static CONNECT = {
        endpoint: `${API_BASE}`
    }
    /**
     * 获取聊天室用户列表
     * @param params 分页参数
     */
    static USER_PAGE = {
        endpoint: `${API_BASE}${API_SUFFIXES.USER_PAGE}`,
        request: (params: ChatRoomUserQuery): AxiosPromise<PageResult<ChatRoomUser[]>> => {
            return request<PageResult<ChatRoomUser[]>>({
                url: ChatRoomAPI.USER_PAGE.endpoint,
                method: "get",
                params: params
            });
        }
    };

    /**
     * 获取单个用户
     * @param userId 用户ID
     */
    static USER_ONE = {
        endpoint: (userId: string): string => {
            return `${API_BASE}${API_SUFFIXES.USER_ONE.replace("{userId}", userId)}`;
        },
        request: (userId: string): AxiosPromise<ChatRoomUser> => {
            return request<ChatRoomUser>({
                url: ChatRoomAPI.USER_ONE.endpoint(userId),
                method: "get"
            });
        }
    };
}
