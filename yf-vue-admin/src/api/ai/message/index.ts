import request from "@/utils/request";
import {AxiosPromise} from "axios";
import {AiMessageForm, AiMessagePageQuery, AiMessagePageVO} from "./type";

const API_BASE = '/ai_message';

const API_SUFFIXES = {
    PAGE: '/{conversationId}/page',
    FORM: '/{id}/form',
    SAVE: '',
    DELETE: '/{ids}',
    UPDATE: '/{id}',
};

// 定义 AiMessageAPI 类
export class AiMessageAPI {
    /**
     * 分页查询 Ai 消息记录表 分页数据
     * @param query 分页参数
     */
    static PAGE = {
        endpoint: (conversationId: string): string => {
            return `${API_BASE}${API_SUFFIXES.PAGE.replace("{conversationId}", conversationId)}`;
        },
        permission: "ai:message:list",
        request: (conversationId: string, query: AiMessagePageQuery): AxiosPromise<PageResult<AiMessagePageVO[]>> => {
            return request<PageResult<AiMessagePageVO[]>>({
                url: AiMessageAPI.PAGE.endpoint(conversationId),
                method: "get",
                params: query
            });
        }
    };

    /**
     * 获取 Ai 消息记录表 表单数据
     * @param id AiMessage 主键
     */
    static FORM = {
        endpoint: (id: string): string => {
            return `${API_BASE}${API_SUFFIXES.FORM.replace("{id}", id.toString())}`;
        },
        permission: "ai:message:update",
        request: (id: string): AxiosPromise<AiMessageForm> => {
            return request<AiMessageForm>({
                url: AiMessageAPI.FORM.endpoint(id),
                method: "get",
            })
        }
    }

    /**
     * 保存 Ai 消息记录表
     * @param userForm 表单数据
     * @return AiMessage 主键
     */
    static SAVE = {
        endpoint: `${API_BASE}${API_SUFFIXES.SAVE}`,
        permission: "ai:message:save",
        request: (form: AiMessageForm): AxiosPromise<string> => {
            return request<string>({
                url: AiMessageAPI.SAVE.endpoint,
                method: "post",
                data: form
            })
        }
    }

    /**
     * 删除 Ai 消息记录表
     * @param ids AiMessage 主键集合,以 "," 分隔
     */
    static DELETE = {
        endpoint: (ids: string): string => {
            return `${API_BASE}${API_SUFFIXES.DELETE.replace("{ids}", ids)}`;
        },
        permission: "ai:message:delete",
        request: (ids: string): AxiosPromise<void> => {
            return request<void>({
                    url: AiMessageAPI.DELETE.endpoint(ids),
                    method: "delete"
                }
            )
        }
    }

    /**
     * 修改 Ai 消息记录表
     * @param id AiMessage 主键
     * @param userForm AiMessage 表单
     */
    static UPDATE = {
        endpoint: (id: string): string => {
            return `${API_BASE}${API_SUFFIXES.UPDATE.replace("{id}", id.toString())}`;
        },
        permission: "ai:message:update",
        request: (id: string, form: AiMessageForm) => {
            return request<void>({
                url: AiMessageAPI.UPDATE.endpoint(id),
                method: "put",
                data: form
            })
        }
    }

}
