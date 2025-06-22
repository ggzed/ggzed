import request from "@/utils/request";
import {AxiosPromise} from "axios";
import {DfmsTableFieldsForm, DfmsTableFieldsPageQuery, DfmsTableFieldsPageVO} from "./type";

const API_BASE = '/dfms_table_fields';

const API_SUFFIXES = {
    PAGE: '/page',
    FORM: '/{id}/form',
    SAVE: '',
    DELETE: '/{ids}',
    UPDATE: '/{id}',
};

// 定义 DfmsTableFieldsAPI 类
export class DfmsTableFieldsAPI {
    /**
     * 分页查询 数据表字段信息 分页数据
     * @param query 分页参数
     */
    static PAGE = {
        endpoint: `${API_BASE}${API_SUFFIXES.PAGE}`,
        permission: "dfms:tablefields:list",
        request: (query: DfmsTableFieldsPageQuery): AxiosPromise<PageResult<DfmsTableFieldsPageVO[]>> => {
            return request<PageResult<DfmsTableFieldsPageVO[]>>({
                url: DfmsTableFieldsAPI.PAGE.endpoint,
                method: "get",
                params: query,
            })
        }
    };

    /**
     * 获取 数据表字段信息 表单数据
     * @param id DfmsTableFields 主键
     */
    static FORM = {
        endpoint: (id: number): string => {
            return `${API_BASE}${API_SUFFIXES.FORM.replace("{id}", id.toString())}`;
        },
        permission: "dfms:tablefields:update",
        request: (id: number): AxiosPromise<DfmsTableFieldsForm> => {
            return request<DfmsTableFieldsForm>({
                url: DfmsTableFieldsAPI.FORM.endpoint(id),
                method: "get",
            })
        }
    }

    /**
     * 保存 数据表字段信息
     * @param userForm 表单数据
     * @return DfmsTableFields 主键
     */
    static SAVE = {
        endpoint: `${API_BASE}${API_SUFFIXES.SAVE}`,
        permission: "dfms:tablefields:save",
        request: (form: DfmsTableFieldsForm): AxiosPromise<number> => {
            return request<number>({
                url: DfmsTableFieldsAPI.SAVE.endpoint,
                method: "post",
                data: form
            })
        }
    }

    /**
     * 删除 数据表字段信息
     * @param ids DfmsTableFields 主键集合,以 "," 分隔
     */
    static DELETE = {
        endpoint: (ids: string): string => {
            return `${API_BASE}${API_SUFFIXES.DELETE.replace("{ids}", ids)}`;
        },
        permission: "dfms:tablefields:delete",
        request: (ids: string): AxiosPromise<void> => {
            return request<void>({
                    url: DfmsTableFieldsAPI.DELETE.endpoint(ids),
                    method: "delete"
                }
            )
        }
    }

    /**
     * 修改 数据表字段信息
     * @param id DfmsTableFields 主键
     * @param userForm DfmsTableFields 表单
     */
    static UPDATE = {
        endpoint: (id: number): string => {
            return `${API_BASE}${API_SUFFIXES.UPDATE.replace("{id}", id.toString())}`;
        },
        permission: "dfms:tablefields:update",
        request: (id: number, form: DfmsTableFieldsForm) => {
            return request<void>({
                url: DfmsTableFieldsAPI.UPDATE.endpoint(id),
                method: "put",
                data: form
            })
        }
    }

}
