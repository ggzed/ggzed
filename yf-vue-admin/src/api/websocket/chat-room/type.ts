/**
 * 聊天室频道枚举
 */
export enum ChannelTypes {
    SYSTEM = "SYSTEM",
    PUBLIC = "PUBLIC",
    PRIVATE = "PRIVATE",
    AI = "AI",
    KICK_OUT = "KICK_OUT",
    WELCOME = "WELCOME",
    EXIT = "EXIT",
    PERMISSION = "PERMISSION",
    RATE_LIMITER = "RATE_LIMITER",
    HEARTBEAT = "HEARTBEAT",
}

/**
 * 聊天室用户查询参数
 */
export interface ChatRoomUserQuery extends PageQuery {
    /** 用户名 */
    keyword?: string,
}

/**
 * 聊天室用户信息
 */
export interface ChatRoomUser {
    /** 用户ID */
    userId?: string,
    /** 用户名 */
    username?: string,
    /** 用户昵称 */
    nickname?: string,
    /** 用户头像 */
    avatar?: string,
    /** 上线时间 */
    connectTime?: string,
    /** 所属频道 */
    channel?: ChannelTypes,
    /** 是否在线 */
    online?: boolean,
    /** 消息未读数量 */
    unreadMessageCount: number,
    /** 记录最后一条消息 */
    lastMessage?: string
}


/**
 * 发送消息载体
 *
 * @param receiverId 接受者ID
 * @param channel 频道
 * @param content 消息
 */
export interface ChatRoomMessage {
    /** 发送者ID */
    senderId?: string,
    /** 接受者ID */
    receiverId?: string,
    /** 频道 */
    channel: ChannelTypes,
    /** 消息 */
    content?: string
}
