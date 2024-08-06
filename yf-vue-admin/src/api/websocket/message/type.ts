import {ChannelTypes, ChatRoomUser} from "@/api/websocket/chat-room/type";

/**
 * socketMessageVO
 *
 * @param id 消息ID
 * @param content 消息内容
 * @param createTime 消息创建时间
 */
export interface SocketMessageVO {
    /** 消息ID */
    id: string,
    /** 发送者ID */
    senderId?: string,
    /** 接受者ID */
    receiverId?: string,
    /** 消息频道 */
    channel?: ChannelTypes,
    /** 消息内容 */
    content?: string,
    /** 消息创建时间 */
    createTime?: string,
    /** 用户信息 */
    userInfo?: ChatRoomUser
}


export interface SocketMessagePageQuery extends PageQuery {
    /** 关键字(消息内容) :目前未做该功能 */
    keyword?: string,
    /** 发送消息的用户id */
    senderId?: string,
    /** 接收消息的用户id */
    receiverId?: string,
    /** 消息所在频道(0:系统频道,1:公共频道...) */
    channel?: ChannelTypes

}
