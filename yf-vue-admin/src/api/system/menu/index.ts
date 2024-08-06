import request from "@/utils/request";
import {AxiosPromise} from "axios";
import {MenuForm, MenuPageQuery, MenuPageVO, RouteVO} from "@/api/system/menu/type";


const API_BASE = '/menu';

const API_SUFFIXES = {
    /** 获取路由菜单 */
    ROUTES: '/routes',
    /** 获取菜单列表 */
    PAGE: '/page',
    /** 获取菜单 option列表 */
    OPTIONS: '/options',
    /** 隐藏菜单 */
    UPDATE_HIDDEN: '/{menuId}/hidden',
    /** 更新菜单 */
    UPDATE: '/{menuId}',
    /** 删除菜单 */
    DELETE: '/{menuIds}',
    /** 新增菜单 */
    SAVE: '',
    /** 修改菜单表单 */
    FORM: '/{menuId}/form',
};

// 定义 MenuAPI 类
export class MenuAPI {
    /**
     * 获取路由菜单
     */
    static ROUTES = {
        endpoint: `${API_BASE}${API_SUFFIXES.ROUTES}`,
        request: (): AxiosPromise<RouteVO[]> => {
            return request<RouteVO[]>({
                url: MenuAPI.ROUTES.endpoint,
                method: "get",
            });
        }
    };

    /**
     * 获取菜单列表
     * @param params MenuPageQuery
     */
    static PAGE = {
        endpoint: `${API_BASE}${API_SUFFIXES.PAGE}`,
        permission: 'system:menu:list',
        request: (params: MenuPageQuery): AxiosPromise<MenuPageVO[]> => {
            return request<MenuPageVO[]>({
                url: MenuAPI.PAGE.endpoint,
                method: "get",
                params: params,
            });
        }
    };

    /**
     * 获取菜单下拉列表
     */
    static OPTIONS = {
        endpoint: `${API_BASE}${API_SUFFIXES.OPTIONS}`,
        permission: 'system:menu:list',
        request: (): AxiosPromise<OptionType[]> => {
            return request<OptionType[]>({
                url: MenuAPI.OPTIONS.endpoint,
                method: "get",
            });
        }
    };

    /**
     * 隐藏菜单
     * @param menuId 菜单ID
     * @param hidden 是否隐藏 (0: 显示, 1: 隐藏)
     */
    static UPDATE_HIDDEN = {
        endpoint: (menuId: number): string => {
            return `${API_BASE}${API_SUFFIXES.UPDATE_HIDDEN.replace("{menuId}", menuId.toString())}`;
        },
        permission: 'system:menu:update',
        request: (menuId: number, hidden: boolean): AxiosPromise<void> => {
            return request<void>({
                url: MenuAPI.UPDATE_HIDDEN.endpoint(menuId),
                method: "patch",
                params: {hidden: hidden},
            });
        }
    };

    /**
     * 更新菜单
     * @param menuId 菜单ID
     * @param menuForm MenuForm
     */
    static UPDATE = {
        endpoint: (menuId: number): string => {
            return `${API_BASE}${API_SUFFIXES.UPDATE.replace("{menuId}", menuId.toString())}`;
        },
        permission: 'system:menu:update',
        request: (menuId: number, menuForm: MenuForm): AxiosPromise<void> => {
            return request<void>({
                url: MenuAPI.UPDATE.endpoint(menuId),
                method: "put",
                data: menuForm,
            });
        }
    };

    /**
     * 删除菜单
     * @param menuIds 以逗号分隔的菜单ID
     */
    static DELETE = {
        endpoint: (menuIds: string): string => {
            return `${API_BASE}${API_SUFFIXES.DELETE.replace("{menuIds}", menuIds)}`;
        },
        permission: 'system:menu:delete',
        request: (menuIds: string): AxiosPromise<void> => {
            return request<void>({
                url: MenuAPI.DELETE.endpoint(menuIds),
                method: "delete",
            });
        }
    };

    /**
     * 新增菜单
     * @param menuForm MenuForm
     * @return 新增的菜单ID
     */
    static SAVE = {
        endpoint: `${API_BASE}${API_SUFFIXES.SAVE}`,
        permission: 'system:menu:save',
        request: (menuForm: MenuForm): AxiosPromise<number> => {
            return request<number>({
                url: MenuAPI.SAVE.endpoint,
                method: "post",
                data: menuForm,
            });
        }
    };

    /**
     * 获取菜单表单
     * @param menuId 菜单ID
     */
    static FORM = {
        endpoint: (menuId: number): string => {
            return `${API_BASE}${API_SUFFIXES.FORM.replace("{menuId}", menuId.toString())}`;
        },
        permission: 'system:menu:update',
        request: (menuId: number): AxiosPromise<MenuForm> => {
            return request<MenuForm>({
                url: MenuAPI.FORM.endpoint(menuId),
                method: "get",
            });
        }
    };
}
