import request from "@/utils/request";
import {AxiosPromise} from "axios";
import {DfmsTableIndexForm, DfmsTableIndexPageQuery, DfmsTableIndexPageVO} from "./type";

const API_BASE = '/dfms_table_index';

const API_SUFFIXES = {
    PAGE: '/page',
    FORM: '/{id}/form',
    SAVE: '',
    DELETE: '/{ids}',
    UPDATE: '/{id}',
};

// 定义 DfmsTableIndexAPI 类
export class DfmsTableIndexAPI {
    /**
     * 分页查询 数据表索引信息 分页数据
     * @param query 分页参数
     */
    static PAGE = {
        endpoint: `${API_BASE}${API_SUFFIXES.PAGE}`,
        permission: "dfms:tableindex:list",
        request: (query: DfmsTableIndexPageQuery): AxiosPromise<PageResult<DfmsTableIndexPageVO[]>> => {
            return request<PageResult<DfmsTableIndexPageVO[]>>({
                url: DfmsTableIndexAPI.PAGE.endpoint,
                method: "get",
                params: query,
            })
        }
    };

    /**
     * 获取 数据表索引信息 表单数据
     * @param id DfmsTableIndex 主键
     */
    static FORM = {
        endpoint: (id: number): string => {
            return `${API_BASE}${API_SUFFIXES.FORM.replace("{id}", id.toString())}`;
        },
        permission: "dfms:tableindex:update",
        request: (id: number): AxiosPromise<DfmsTableIndexForm> => {
            return request<DfmsTableIndexForm>({
                url: DfmsTableIndexAPI.FORM.endpoint(id),
                method: "get",
            })
        }
    }

    /**
     * 保存 数据表索引信息
     * @param userForm 表单数据
     * @return DfmsTableIndex 主键
     */
    static SAVE = {
        endpoint: `${API_BASE}${API_SUFFIXES.SAVE}`,
        permission: "dfms:tableindex:save",
        request: (form: DfmsTableIndexForm): AxiosPromise<number> => {
            return request<number>({
                url: DfmsTableIndexAPI.SAVE.endpoint,
                method: "post",
                data: form
            })
        }
    }

    /**
     * 删除 数据表索引信息
     * @param ids DfmsTableIndex 主键集合,以 "," 分隔
     */
    static DELETE = {
        endpoint: (ids: string): string => {
            return `${API_BASE}${API_SUFFIXES.DELETE.replace("{ids}", ids)}`;
        },
        permission: "dfms:tableindex:delete",
        request: (ids: string): AxiosPromise<void> => {
            return request<void>({
                    url: DfmsTableIndexAPI.DELETE.endpoint(ids),
                    method: "delete"
                }
            )
        }
    }

    /**
     * 修改 数据表索引信息
     * @param id DfmsTableIndex 主键
     * @param userForm DfmsTableIndex 表单
     */
    static UPDATE = {
        endpoint: (id: number): string => {
            return `${API_BASE}${API_SUFFIXES.UPDATE.replace("{id}", id.toString())}`;
        },
        permission: "dfms:tableindex:update",
        request: (id: number, form: DfmsTableIndexForm) => {
            return request<void>({
                url: DfmsTableIndexAPI.UPDATE.endpoint(id),
                method: "put",
                data: form
            })
        }
    }

}
