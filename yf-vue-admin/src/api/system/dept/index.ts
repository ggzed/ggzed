import request from "@/utils/request";
import {DeptForm, DeptPageQuery, DeptPageVO} from "@/api/system/dept/type";
import {AxiosPromise} from "axios";

const API_BASE = '/dept';

const API_SUFFIXES = {
    /** 获取部门列表 */
    PAGE: '/page',
    /** 更新部门 */
    UPDATE: '/{deptId}',
    /** 隐藏部门 */
    UPDATE_STATUS: '/{deptId}/status',
    /** 删除部门 */
    DELETE: '/{deptIds}',
    /** 获取部门 option列表 */
    OPTIONS: '/options',
    /** 修改部门表单 */
    FORM: '/{deptId}/form',
    /** 新增部门 */
    SAVE: ''
};

// 定义 DeptAPI 类
export class DeptAPI {
    /**
     * 获取部门列表
     * @param params DeptPageQuery
     */
    static PAGE = {
        endpoint: `${API_BASE}${API_SUFFIXES.PAGE}`,
        permission: 'system:dept:list',
        request: (params: DeptPageQuery): AxiosPromise<DeptPageVO[]> => {
            return request<DeptPageVO[]>({
                url: DeptAPI.PAGE.endpoint,
                method: "get",
                params: params,
            });
        }
    };

    /**
     * 修改部门
     * @param deptId 部门ID
     * @param deptForm DeptForm
     */
    static UPDATE = {
        endpoint: (deptId: number): string => {
            return `${API_BASE}${API_SUFFIXES.UPDATE.replace("{deptId}", deptId.toString())}`;
        },
        permission: 'system:dept:update',
        request: (deptId: number, deptForm: DeptForm): AxiosPromise<void> => {
            return request<void>({
                url: DeptAPI.UPDATE.endpoint(deptId),
                method: "put",
                data: deptForm,
            });
        }
    };

    /**
     * 隐藏部门
     * @param deptId 部门ID
     * @param status 是否隐藏 (0: 显示, 1: 隐藏)
     */
    static UPDATE_STATUS = {
        endpoint: (deptId: number): string => {
            return `${API_BASE}${API_SUFFIXES.UPDATE_STATUS.replace("{deptId}", deptId.toString())}`;
        },
        permission: 'system:dept:update',
        request: (deptId: number, status: boolean): AxiosPromise<void> => {
            return request<void>({
                url: DeptAPI.UPDATE_STATUS.endpoint(deptId),
                method: "patch",
                params: {status: status},
            });
        }
    };

    /**
     * 删除部门
     * @param deptIds 使用 , 分割的部门ID
     */
    static DELETE = {
        endpoint: (deptIds: string): string => {
            return `${API_BASE}${API_SUFFIXES.DELETE.replace("{deptIds}", deptIds.toString())}`;
        },
        permission: 'system:dept:delete',
        request: (deptIds: string): AxiosPromise<void> => {
            return request<void>({
                url: DeptAPI.DELETE.endpoint(deptIds),
                method: "delete",
            });
        }
    };

    /**
     * 获取部门下拉列表
     */
    static OPTIONS = {
        endpoint: `${API_BASE}${API_SUFFIXES.OPTIONS}`,
        permission: 'system:dept:list',
        request: (): AxiosPromise<OptionType[]> => {
            return request<OptionType[]>({
                url: DeptAPI.OPTIONS.endpoint,
                method: "get",
            });
        }
    };

    /**
     * 获取部门表单
     * @param deptId 部门ID
     */
    static FORM = {
        endpoint: (deptId: number): string => {
            return `${API_BASE}${API_SUFFIXES.FORM.replace("{deptId}", deptId.toString())}`;
        },
        permission: 'system:dept:list',
        request: (deptId: number): AxiosPromise<DeptForm> => {
            return request<DeptForm>({
                url: DeptAPI.FORM.endpoint(deptId),
                method: "get",
            });
        }
    };

    /**
     * 新增部门
     * @param deptForm 部门表单
     */
    static SAVE = {
        endpoint: `${API_BASE}${API_SUFFIXES.SAVE}`,
        permission: 'system:dept:save',
        request: (deptForm: DeptForm): AxiosPromise<number> => {
            return request<number>({
                url: DeptAPI.SAVE.endpoint,
                method: "post",
                data: deptForm,
            });
        }
    };
}
