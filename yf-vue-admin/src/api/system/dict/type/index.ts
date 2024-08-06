import request from "@/utils/request";
import {AxiosPromise} from "axios";
import {DictTypeForm, DictTypePageQuery, DictTypePageVO} from "@/api/system/dict/type/type";

const API_BASE = '/dict/type';

const API_SUFFIXES = {
    /** 分页获取字典类型数据 */
    PAGE: '/page',
    /** 获取字典类型表单数据 */
    FORM: '/{dictTypeId}/form',
    /** 新增字典类型 ( POST 请求 ) */
    SAVE: '',
    /** 删除字典类型 */
    DELETE: '/{dictTypeIds}',
    /** 修改字典类型 */
    UPDATE: '/{dictTypeId}',
    /** 修改字典类型状态 */
    UPDATE_STATUS: '/{dictTypeId}/status'

};

// 定义 DictTypeAPI 类
export class DictTypeAPI {
    /**
     * 分页查询字典数据数据
     * @param query 分页参数
     */
    static PAGE = {
        endpoint: `${API_BASE}${API_SUFFIXES.PAGE}`,
        permission: "system:dict-type:page",
        request: (query: DictTypePageQuery): AxiosPromise<PageResult<DictTypePageVO[]>> => {
            return request<PageResult<DictTypePageVO[]>>({
                url: DictTypeAPI.PAGE.endpoint,
                method: "get",
                params: query,
            })
        }
    };

    /**
     * 获取字典数据表单数据
     * @param dictTypeId 字典数据Id
     */
    static FORM = {
        endpoint: (dictTypeId: number): string => {
            return `${API_BASE}${API_SUFFIXES.FORM.replace("{dictTypeId}", dictTypeId.toString())}`;
        },
        permission: "system:dict-type:update",
        request: (dictTypeId: number): AxiosPromise<DictTypeForm> => {
            return request<DictTypeForm>({
                url: DictTypeAPI.FORM.endpoint(dictTypeId),
                method: "get",
            })
        }
    }

    /**
     * 保存字典数据
     * @param userForm 表单数据
     * @return 字典数据Id
     */
    static SAVE = {
        endpoint: `${API_BASE}${API_SUFFIXES.SAVE}`,
        permission: "system:dict-type:save",
        request: (userForm: DictTypeForm): AxiosPromise<number> => {
            return request<number>({
                url: DictTypeAPI.SAVE.endpoint,
                method: "post",
                data: userForm
            })
        }
    }

    /**
     * 删除字典数据
     * @param dictTypeIds 字典数据id集合 ,  以 "," 分隔
     */
    static DELETE = {
        endpoint: (dictTypeIds: string): string => {
            return `${API_BASE}${API_SUFFIXES.DELETE.replace("{dictTypeIds}", dictTypeIds)}`;
        },
        permission: "system:dict-type:delete",
        request: (dictTypeIds: string): AxiosPromise<void> => {
            return request<void>({
                    url: DictTypeAPI.DELETE.endpoint(dictTypeIds),
                    method: "delete"
                }
            )
        }
    }

    /**
     * 修改字典数据
     * @param dictTypeId 字典数据Id
     * @param userForm 字典数据表单
     */
    static UPDATE = {
        endpoint: (dictTypeId: number): string => {
            return `${API_BASE}${API_SUFFIXES.UPDATE.replace("{dictTypeId}", dictTypeId.toString())}`;
        },
        permission: "system:dict-type:update",
        request: (dictTypeId: number, userForm: DictTypeForm) => {
            return request<void>({
                url: DictTypeAPI.UPDATE.endpoint(dictTypeId),
                method: "put",
                data: userForm
            })
        }
    }

    /**
     * 修改字典数据状态
     * @param dictTypeId 字典数据Id
     * @param status 修改状态
     */
    static UPDATE_STATUS = {
        endpoint: (dictTypeId: number): string => {
            return `${API_BASE}${API_SUFFIXES.UPDATE_STATUS.replace("{dictTypeId}", dictTypeId.toString())}`;
        },
        permission: "system:dict-type:update",
        request: (dictTypeId: number, status: boolean) => {
            return request<void>({
                url: DictTypeAPI.UPDATE_STATUS.endpoint(dictTypeId),
                method: "patch",
                params: {status: status}
            })
        }
    }
}
