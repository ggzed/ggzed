import request from "@/utils/request";
import {AxiosPromise} from "axios";
import {DfmsNodeForm, DfmsNodePageQuery, DfmsNodePageVO} from "./type";

const API_BASE = '/dfms_node';

const API_SUFFIXES = {
    PAGE: '/page',
    FORM: '/{id}/form',
    SAVE: '',
    DELETE: '/{ids}',
    UPDATE: '/{id}',
};

// 定义 DfmsNodeAPI 类
export class DfmsNodeAPI {
    /**
     * 分页查询 节点信息 分页数据
     * @param query 分页参数
     */
    static PAGE = {
        endpoint: `${API_BASE}${API_SUFFIXES.PAGE}`,
        permission: "dfms:node:list",
        request: (query: DfmsNodePageQuery): AxiosPromise<PageResult<DfmsNodePageVO[]>> => {
            return request<PageResult<DfmsNodePageVO[]>>({
                url: DfmsNodeAPI.PAGE.endpoint,
                method: "get",
                params: query,
            })
        }
    };

    /**
     * 获取 节点信息 表单数据
     * @param id DfmsNode 主键
     */
    static FORM = {
        endpoint: (id: number): string => {
            return `${API_BASE}${API_SUFFIXES.FORM.replace("{id}", id.toString())}`;
        },
        permission: "dfms:node:update",
        request: (id: number): AxiosPromise<DfmsNodeForm> => {
            return request<DfmsNodeForm>({
                url: DfmsNodeAPI.FORM.endpoint(id),
                method: "get",
            })
        }
    }

    /**
     * 保存 节点信息
     * @param userForm 表单数据
     * @return DfmsNode 主键
     */
    static SAVE = {
        endpoint: `${API_BASE}${API_SUFFIXES.SAVE}`,
        permission: "dfms:node:save",
        request: (form: DfmsNodeForm): AxiosPromise<number> => {
            return request<number>({
                url: DfmsNodeAPI.SAVE.endpoint,
                method: "post",
                data: form
            })
        }
    }

    /**
     * 删除 节点信息
     * @param ids DfmsNode 主键集合,以 "," 分隔
     */
    static DELETE = {
        endpoint: (ids: string): string => {
            return `${API_BASE}${API_SUFFIXES.DELETE.replace("{ids}", ids)}`;
        },
        permission: "dfms:node:delete",
        request: (ids: string): AxiosPromise<void> => {
            return request<void>({
                    url: DfmsNodeAPI.DELETE.endpoint(ids),
                    method: "delete"
                }
            )
        }
    }

    /**
     * 修改 节点信息
     * @param id DfmsNode 主键
     * @param userForm DfmsNode 表单
     */
    static UPDATE = {
        endpoint: (id: number): string => {
            return `${API_BASE}${API_SUFFIXES.UPDATE.replace("{id}", id.toString())}`;
        },
        permission: "dfms:node:update",
        request: (id: number, form: DfmsNodeForm) => {
            return request<void>({
                url: DfmsNodeAPI.UPDATE.endpoint(id),
                method: "put",
                data: form
            })
        }
    }

}
