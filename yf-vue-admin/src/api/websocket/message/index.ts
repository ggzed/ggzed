import request from "@/utils/request";
import {AxiosPromise} from "axios";
import {SocketMessagePageQuery, SocketMessageVO} from "@/api/websocket/message/type";

const API_BASE = '/message';

const API_SUFFIXES = {
    /** 查询用户端的Socket消息 */
    USER_PAGE: '/page',
    /** 根据ID判断当前用户需要已读哪个发送者 */
    USER_READ: '/{senderId}/read'
};

// 定义 MessageAPI 类
export class MessageAPI {
    /**
     * 查询Socket消息
     * @param {string} params.keyword 关键字(消息内容: 目前未做该功能)
     * @param {string} params.senderId 发送消息的用户id
     * @param {string} params.receiverId 接收消息的用户id
     * @param {string} params.channel 消息所在频道(0:系统频道,1:公共频道...)
     * @param {string} params.pageNum 页码
     * @param {string} params.pageSize 每页记录数
     * @returns
     */
    static USER_PAGE = {
        endpoint: `${API_BASE}${API_SUFFIXES.USER_PAGE}`,
        request: (params: SocketMessagePageQuery): AxiosPromise<PageResult<SocketMessageVO[]>> => {
            return request<PageResult<SocketMessageVO[]>>({
                url: MessageAPI.USER_PAGE.endpoint,
                method: "get",
                params: params
            });
        }
    };

    /**
     * 根据ID判断当前用户需要已读那个发送者
     * @param senderId 发送者ID
     */
    static USER_READ = {
        endpoint: (senderId: string): string => {
            return `${API_BASE}${API_SUFFIXES.USER_READ.replace("{senderId}", senderId.toString())}`;
        },
        request: (senderId: string): AxiosPromise<void> => {
            return request<void>({
                url: MessageAPI.USER_READ.endpoint(senderId),
                method: "patch"
            });
        }
    };
}
