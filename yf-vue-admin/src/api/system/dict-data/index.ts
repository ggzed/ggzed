import request from "@/utils/request";
import {AxiosPromise} from "axios";
import {DictDataForm, DictDataPageQuery, DictDataPageVO, DictType} from "@/api/system/dict-data/type";

const API_BASE = '/dict/data';

const API_SUFFIXES = {
    /** 根据type获取字典数据 */
    DATA_OPTIONS: '/{type}/options',
    /** 分页获取字典数据数据 */
    PAGE: '/{dictTypeId}/page',
    /** 获取字典数据表单数据 */
    FORM: '/{dictDataId}/form',
    /** 新增字典数据 ( POST 请求 ) */
    SAVE: '',
    /** 删除字典数据 */
    DELETE: '/{dictDataIds}',
    /** 修改字典数据 */
    UPDATE: '/{dictDataId}',
    /** 修改字典数据状态 */
    UPDATE_STATUS: '/{dictDataId}/status'
};

// 定义 DictAPI 类
export class DictDataAPI {
    /**
     * 字典数据
     * @param type 字典类型
     */
    static DATA_OPTIONS = {
        endpoint: (type: DictType): string => {
            return `${API_BASE}${API_SUFFIXES.DATA_OPTIONS.replace("{type}", type)}`;
        },
        request: (type: DictType): AxiosPromise<OptionType[]> => {
            return request<OptionType[]>({
                url: DictDataAPI.DATA_OPTIONS.endpoint(type),
                method: "get"
            });
        }
    };

    /**
     * 分页查询字典数据数据
     * @param query 分页参数
     */
    static PAGE = {
        endpoint: (dictTypeId: number): string => {
            return `${API_BASE}${API_SUFFIXES.PAGE.replace("{dictTypeId}", dictTypeId.toString())}`;
        },
        permission: "system:dict-data:list",
        request: (dictTypeId: number, query: DictDataPageQuery): AxiosPromise<PageResult<DictDataPageVO[]>> => {
            return request<PageResult<DictDataPageVO[]>>({
                url: DictDataAPI.PAGE.endpoint(dictTypeId),
                method: "get",
                params: query,
            })
        }
    };

    /**
     * 获取字典类型表单数据
     * @param dictDataId 字典类型Id
     */
    static FORM = {
        endpoint: (dictDataId: number): string => {
            return `${API_BASE}${API_SUFFIXES.FORM.replace("{dictDataId}", dictDataId.toString())}`;
        },
        permission: "system:dict-data:update",
        request: (dictDataId: number): AxiosPromise<DictDataForm> => {
            return request<DictDataForm>({
                url: DictDataAPI.FORM.endpoint(dictDataId),
                method: "get",
            })
        }
    }

    /**
     * 保存字典类型
     * @param userForm 表单数据
     * @return 字典类型Id
     */
    static SAVE = {
        endpoint: `${API_BASE}${API_SUFFIXES.SAVE}`,
        permission: "system:dict-data:save",
        request: (userForm: DictDataForm): AxiosPromise<number> => {
            return request<number>({
                url: DictDataAPI.SAVE.endpoint,
                method: "post",
                data: userForm
            })
        }
    }

    /**
     * 删除字典类型
     * @param dictDataIds 字典类型id集合 ,  以 "," 分隔
     */
    static DELETE = {
        endpoint: (dictDataIds: string): string => {
            return `${API_BASE}${API_SUFFIXES.DELETE.replace("{dictDataIds}", dictDataIds)}`;
        },
        permission: "system:dict-data:delete",
        request: (dictDataIds: string): AxiosPromise<void> => {
            return request<void>({
                    url: DictDataAPI.DELETE.endpoint(dictDataIds),
                    method: "delete"
                }
            )
        }
    }

    /**
     * 修改字典类型
     * @param dictDataId 字典类型Id
     * @param userForm 字典类型表单
     */
    static UPDATE = {
        endpoint: (dictDataId: number): string => {
            return `${API_BASE}${API_SUFFIXES.UPDATE.replace("{dictDataId}", dictDataId.toString())}`;
        },
        permission: "system:dict-data:update",
        request: (dictDataId: number, userForm: DictDataForm) => {
            return request<void>({
                url: DictDataAPI.UPDATE.endpoint(dictDataId),
                method: "put",
                data: userForm
            })
        }
    }

    /**
     * 修改字典类型状态
     * @param dictDataId 字典类型Id
     * @param status 修改状态
     */
    static UPDATE_STATUS = {
        endpoint: (dictDataId: number): string => {
            return `${API_BASE}${API_SUFFIXES.UPDATE_STATUS.replace("{dictDataId}", dictDataId.toString())}`;
        },
        permission: "system:dict-data:update",
        request: (dictDataId: number, status: boolean) => {
            return request<void>({
                url: DictDataAPI.UPDATE_STATUS.endpoint(dictDataId),
                method: "patch",
                params: {status: status}
            })
        }
    }
}
