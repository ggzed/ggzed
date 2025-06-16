import request from "@/utils/request";
import {AxiosPromise} from "axios";
import {AiConversationForm, AiConversationPageQuery, AiConversationPageVO} from "./type";

const API_BASE = '/ai_conversation';

const API_SUFFIXES = {
    PAGE: '/page',
    FORM: '/{id}/form',
    DELETE: '/{ids}',
    UPDATE: '/{id}',
};

// 定义 AiConversationAPI 类
export class AiConversationAPI {
    /**
     * 分页查询 AI 会话表 分页数据
     * @param query 分页参数
     */
    static PAGE = {
        endpoint: `${API_BASE}${API_SUFFIXES.PAGE}`,
        permission: "ai:conversation:list",
        request: (query: AiConversationPageQuery): AxiosPromise<PageResult<AiConversationPageVO[]>> => {
            return request<PageResult<AiConversationPageVO[]>>({
                url: AiConversationAPI.PAGE.endpoint,
                method: "get",
                params: query,
            })
        }
    };

    /**
     * 获取 AI 会话表 表单数据
     * @param id AiConversation 主键
     */
    static FORM = {
        endpoint: (id: string): string => {
            return `${API_BASE}${API_SUFFIXES.FORM.replace("{id}", id.toString())}`;
        },
        permission: "ai:conversation:update",
        request: (id: string): AxiosPromise<AiConversationForm> => {
            return request<AiConversationForm>({
                url: AiConversationAPI.FORM.endpoint(id),
                method: "get",
            })
        }
    }

    /**
     * 删除 AI 会话表
     * @param ids AiConversation 主键集合,以 "," 分隔
     */
    static DELETE = {
        endpoint: (ids: string): string => {
            return `${API_BASE}${API_SUFFIXES.DELETE.replace("{ids}", ids)}`;
        },
        permission: "ai:conversation:delete",
        request: (ids: string): AxiosPromise<void> => {
            return request<void>({
                    url: AiConversationAPI.DELETE.endpoint(ids),
                    method: "delete"
                }
            )
        }
    }

    /**
     * 修改 AI 会话表
     * @param id AiConversation 主键
     * @param userForm AiConversation 表单
     */
    static UPDATE = {
        endpoint: (id: string): string => {
            return `${API_BASE}${API_SUFFIXES.UPDATE.replace("{id}", id.toString())}`;
        },
        permission: "ai:conversation:update",
        request: (id: string, form: AiConversationForm) => {
            return request<void>({
                url: AiConversationAPI.UPDATE.endpoint(id),
                method: "put",
                data: form
            })
        }
    }

}
