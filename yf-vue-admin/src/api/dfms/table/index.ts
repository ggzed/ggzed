import request from "@/utils/request";
import {AxiosPromise} from "axios";
import {DfmsTableForm, DfmsTablePageQuery, DfmsTablePageVO} from "./type";

const API_BASE = '/dfms_table';

const API_SUFFIXES = {
    PAGE: '/page',
    FORM: '/{id}/form',
    SAVE: '',
    DELETE: '/{ids}',
    UPDATE: '/{id}',
};

// 定义 DfmsTableAPI 类
export class DfmsTableAPI {
    /**
     * 分页查询 数据表信息 分页数据
     * @param query 分页参数
     */
    static PAGE = {
        endpoint: `${API_BASE}${API_SUFFIXES.PAGE}`,
        permission: "dfms:table:list",
        request: (query: DfmsTablePageQuery): AxiosPromise<PageResult<DfmsTablePageVO[]>> => {
            return request<PageResult<DfmsTablePageVO[]>>({
                url: DfmsTableAPI.PAGE.endpoint,
                method: "get",
                params: query,
            })
        }
    };

    /**
     * 获取 数据表信息 表单数据
     * @param id DfmsTable 主键
     */
    static FORM = {
        endpoint: (id: number): string => {
            return `${API_BASE}${API_SUFFIXES.FORM.replace("{id}", id.toString())}`;
        },
        permission: "dfms:table:update",
        request: (id: number): AxiosPromise<DfmsTableForm> => {
            return request<DfmsTableForm>({
                url: DfmsTableAPI.FORM.endpoint(id),
                method: "get",
            })
        }
    }

    /**
     * 保存 数据表信息
     * @param userForm 表单数据
     * @return DfmsTable 主键
     */
    static SAVE = {
        endpoint: `${API_BASE}${API_SUFFIXES.SAVE}`,
        permission: "dfms:table:save",
        request: (form: DfmsTableForm): AxiosPromise<number> => {
            return request<number>({
                url: DfmsTableAPI.SAVE.endpoint,
                method: "post",
                data: form
            })
        }
    }

    /**
     * 删除 数据表信息
     * @param ids DfmsTable 主键集合,以 "," 分隔
     */
    static DELETE = {
        endpoint: (ids: string): string => {
            return `${API_BASE}${API_SUFFIXES.DELETE.replace("{ids}", ids)}`;
        },
        permission: "dfms:table:delete",
        request: (ids: string): AxiosPromise<void> => {
            return request<void>({
                    url: DfmsTableAPI.DELETE.endpoint(ids),
                    method: "delete"
                }
            )
        }
    }

    /**
     * 修改 数据表信息
     * @param id DfmsTable 主键
     * @param userForm DfmsTable 表单
     */
    static UPDATE = {
        endpoint: (id: number): string => {
            return `${API_BASE}${API_SUFFIXES.UPDATE.replace("{id}", id.toString())}`;
        },
        permission: "dfms:table:update",
        request: (id: number, form: DfmsTableForm) => {
            return request<void>({
                url: DfmsTableAPI.UPDATE.endpoint(id),
                method: "put",
                data: form
            })
        }
    }

}
