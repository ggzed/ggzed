import request from "@/utils/request";
import axios, {AxiosPromise} from "axios";
import {DfmsDbForm, DfmsDbOverviewVO, DfmsDbPageQuery, DfmsDbPageVO,DfmsDbOverviewVOs} from "./type";

const API_BASE = '/dfms_db';

const API_SUFFIXES = {
    PAGE: '/page',
    FORM: '/{id}/form',
    SAVE: '',
    DELETE: '/{ids}',
    UPDATE: '/{id}',
    OVERVIEW: '/overview',
};

// 定义 DfmsDbAPI 类
export class DfmsDbAPI {
    /**
     * 分页查询 数据库信息 分页数据
     * @param query 分页参数
     */
    static PAGE = {
        endpoint: `${API_BASE}${API_SUFFIXES.PAGE}`,
        permission: "dfms:db:list",
        request: (query: DfmsDbPageQuery): AxiosPromise<PageResult<DfmsDbPageVO[]>> => {
            return request<PageResult<DfmsDbPageVO[]>>({
                url: DfmsDbAPI.PAGE.endpoint,
                method: "get",
                params: query,
            })
        }
    };

    /**
     * 获取 数据库信息 表单数据
     * @param id DfmsDb 主键
     */
    static FORM = {
        endpoint: (id: number): string => {
            return `${API_BASE}${API_SUFFIXES.FORM.replace("{id}", id.toString())}`;
        },
        permission: "dfms:db:update",
        request: (id: number): AxiosPromise<DfmsDbForm> => {
            return request<DfmsDbForm>({
                url: DfmsDbAPI.FORM.endpoint(id),
                method: "get",
            })
        }
    }

    /**
     * 保存 数据库信息
     * @param userForm 表单数据
     * @return DfmsDb 主键
     */
    static SAVE = {
        endpoint: `${API_BASE}${API_SUFFIXES.SAVE}`,
        permission: "dfms:db:save",
        request: (form: DfmsDbForm): AxiosPromise<number> => {
            return request<number>({
                url: DfmsDbAPI.SAVE.endpoint,
                method: "post",
                data: form
            })
        }
    }

    /**
     * 删除 数据库信息
     * @param ids DfmsDb 主键集合,以 "," 分隔
     */
    static DELETE = {
        endpoint: (ids: string): string => {
            return `${API_BASE}${API_SUFFIXES.DELETE.replace("{ids}", ids)}`;
        },
        permission: "dfms:db:delete",
        request: (ids: string): AxiosPromise<void> => {
            return request<void>({
                    url: DfmsDbAPI.DELETE.endpoint(ids),
                    method: "delete"
                }
            )
        }
    }

    /**
     * 修改 数据库信息
     * @param id DfmsDb 主键
     * @param userForm DfmsDb 表单
     */
    static UPDATE = {
        endpoint: (id: number): string => {
            return `${API_BASE}${API_SUFFIXES.UPDATE.replace("{id}", id.toString())}`;
        },
        permission: "dfms:db:update",
        request: (id: number, form: DfmsDbForm) => {
            return request<void>({
                url: DfmsDbAPI.UPDATE.endpoint(id),
                method: "put",
                data: form
            })
        }
    }

    /**
     * 分页查询 数据库信息 分页数据
     * @param query 分页参数
     */
    static OVERVIEW = {
        endpoint: `${API_BASE}${API_SUFFIXES.OVERVIEW}`,
        permission: "dfms:db:list",
        request: (): AxiosPromise<DfmsDbOverviewVO[]> => {
            return request<DfmsDbOverviewVO[]>({
                url: DfmsDbAPI.OVERVIEW.endpoint,
                method: "get",
            })
        }
    };

}
