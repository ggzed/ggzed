import request from "@/utils/request";
import {AxiosPromise} from "axios";
import {RoleForm, RolePageQuery, RolePageVO} from "@/api/system/role/type";

const API_BASE = '/role';

const API_SUFFIXES = {
    /** 角色分页 */
    PAGE: '/page',
    /** 角色表单 */
    FORM: '/{roleId}/form',
    /** 保存角色 */
    SAVE: '',
    /** 删除角色 */
    DELETE: '/{roleIds}',
    /** 修改角色 */
    UPDATE: '/{roleId}',
    /** 修改角色状态 */
    UPDATE_STATUS: '/{roleId}/status',
    /** 获取角色菜单集合 */
    MENU_IDS: '/{roleId}/menuIds',
    /** 修改角色菜单 */
    UPDATE_MENUS: '/{roleId}/menus',
    /** 获取角色 options */
    OPTIONS: '/options'
};

export class RoleAPI {
    /**
     * 查询角色
     * @param {Object} queryParams 查询参数
     */
    static PAGE = {
        endpoint: `${API_BASE}${API_SUFFIXES.PAGE}`,
        permission: "system:role:list",
        request: (queryParams: RolePageQuery): AxiosPromise<PageResult<RolePageVO[]>> => {
            return request<PageResult<RolePageVO[]>>({
                url: RoleAPI.PAGE.endpoint,
                method: "get",
                params: queryParams
            });
        }
    };

    /**
     * 角色表单数据
     * @param {number} roleId 角色ID
     */
    static FORM = {
        endpoint: (roleId: number) => `${API_BASE}${API_SUFFIXES.FORM.replace("{roleId}", roleId.toString())}`,
        permission: "system:role:update",
        request: (roleId: number): AxiosPromise<RoleForm> => {
            return request<RoleForm>({
                url: RoleAPI.FORM.endpoint(roleId),
                method: "get"
            });
        }
    };

    /**
     * 增加角色
     * @param {RoleForm} roleForm 角色表单数据
     */
    static SAVE = {
        endpoint: `${API_BASE}${API_SUFFIXES.SAVE}`,
        permission: "system:role:save",
        request: (roleForm: RoleForm): AxiosPromise<number> => {
            return request<number>({
                url: RoleAPI.SAVE.endpoint,
                method: "post",
                data: roleForm
            });
        }
    };

    /**
     * 删除角色
     * @param {string} roleIds 角色ID（以逗号分隔）
     */
    static DELETE = {
        endpoint: (roleIds: string) => `${API_BASE}${API_SUFFIXES.DELETE.replace("{roleIds}", roleIds)}`,
        permission: "system:role:delete",
        request: (roleIds: string) => {
            return request<void>({
                url: RoleAPI.DELETE.endpoint(roleIds),
                method: "delete"
            });
        }
    };

    /**
     * 修改角色
     * @param {number} roleId 角色ID
     * @param {RoleForm} roleForm 角色表单数据
     */
    static UPDATE = {
        endpoint: (roleId: number) => `${API_BASE}${API_SUFFIXES.UPDATE.replace("{roleId}", roleId.toString())}`,
        permission: "system:role:update",
        request: (roleId: number, roleForm: RoleForm) => {
            return request<void>({
                url: RoleAPI.UPDATE.endpoint(roleId),
                method: "put",
                data: roleForm
            });
        }
    };

    /**
     * 修改角色状态
     * @param {number} roleId 角色ID
     * @param {boolean} status 角色状态
     */
    static UPDATE_STATUS = {
        endpoint: (roleId: number) => `${API_BASE}${API_SUFFIXES.UPDATE_STATUS.replace("{roleId}", roleId.toString())}`,
        permission: "system:role:update",
        request: (roleId: number, status: boolean) => {
            return request<void>({
                url: RoleAPI.UPDATE_STATUS.endpoint(roleId),
                method: "patch",
                params: {status: status}
            });
        }
    };

    /**
     * 获取角色的菜单ID
     * @param {number} roleId 角色ID
     */
    static MENU_IDS = {
        endpoint: (roleId: number) => `${API_BASE}${API_SUFFIXES.MENU_IDS.replace("{roleId}", roleId.toString())}`,
        permission: "system:role:list",
        request: (roleId: number,): AxiosPromise<number[]> => {
            return request<number[]>({
                url: RoleAPI.MENU_IDS.endpoint(roleId),
                method: "get"
            });
        }
    };

    /**
     * 分配菜单权限给角色
     * @param {number} roleId 角色ID
     * @param  menuIds 菜单ID列表
     */
    static UPDATE_MENUS = {
        endpoint: (roleId: number) => `${API_BASE}${API_SUFFIXES.UPDATE_MENUS.replace("{roleId}", roleId.toString())}`,
        permission: "system:role:permission",
        request: (roleId: number, menuIds: number[]) => {
            return request<void>({
                url: RoleAPI.UPDATE_MENUS.endpoint(roleId),
                method: "put",
                data: menuIds
            });
        }
    };

    /**
     * 角色下拉列表
     */
    static OPTIONS = {
        endpoint: `${API_BASE}${API_SUFFIXES.OPTIONS}`,
        permission: "system:role:list",
        request: (): AxiosPromise<OptionType[]> => {
            return request<OptionType[]>({
                url: RoleAPI.OPTIONS.endpoint,
                method: "get"
            });
        }
    };
}
