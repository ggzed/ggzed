import request from "@/utils/request";
import {AxiosPromise} from "axios";
import {UserForm, UserInfo, UserPageQuery, UserPageVO} from "@/api/system/user/type";

const API_BASE = '/user';

const API_SUFFIXES = {
    /** 获取当前登录的用户信息 */
    ME: '/me',
    /** 分页获取用户数据 */
    PAGE: '/page',
    /** 获取表单数据 */
    FORM: '/{userId}/form',
    /** 新增用户 ( POST 请求 ) */
    SAVE: '',
    /** 删除用户 */
    DELETE: '/{userIds}',
    /** 修改用户 */
    UPDATE: '/{userId}',
    /** 修改用户状态 */
    UPDATE_STATUS: '/{userId}/status',
    /** 管理员修改用户密码 */
    ADMIN_RESET_PASSWORD: '/{userId}/reset-password',
};


// 定义 USER_API 类
export class UserAPI {
    /**
     * 获取当前登录的用户信息
     */
    static ME = {
        endpoint: `${API_BASE}${API_SUFFIXES.ME}`,
        request: (): AxiosPromise<UserInfo> => {
            return request<UserInfo>({
                url: UserAPI.ME.endpoint,
                method: "get",
            })
        }
    }

    /**
     * 分页查询用户数据
     * @param query 分页参数
     */
    static PAGE = {
        endpoint: `${API_BASE}${API_SUFFIXES.PAGE}`,
        permission: "system:user:list",
        request: (query: UserPageQuery): AxiosPromise<PageResult<UserPageVO[]>> => {
            return request<PageResult<UserPageVO[]>>({
                url: UserAPI.PAGE.endpoint,
                method: "get",
                params: query,
            })
        }
    };

    /**
     * 获取用户表单数据
     * @param userId 用户Id
     */
    static FORM = {
        endpoint: (userId: string): string => {
            return `${API_BASE}${API_SUFFIXES.FORM.replace("{userId}", userId)}`;
        },
        permission: "system:user:update",
        request: (userId: string): AxiosPromise<UserForm> => {
            return request<UserForm>({
                url: UserAPI.FORM.endpoint(userId),
                method: "get",
            })
        }
    }

    /**
     * 保存用户
     * @param userForm 表单数据
     * @return 用户Id
     */
    static SAVE = {
        endpoint: `${API_BASE}${API_SUFFIXES.SAVE}`,
        permission: "system:user:save",
        request: (userForm: UserForm): AxiosPromise<number> => {
            return request<number>({
                url: UserAPI.SAVE.endpoint,
                method: "post",
                data: userForm
            })
        }
    }

    /**
     * 删除用户
     * @param userIds 用户id集合 ,  以 "," 分隔
     */
    static DELETE = {
        endpoint: (userIds: string): string => {
            return `${API_BASE}${API_SUFFIXES.DELETE.replace("{userIds}", userIds)}`;
        },
        permission: "system:user:delete",
        request: (userIds: string): AxiosPromise<void> => {
            return request<void>({
                    url: UserAPI.DELETE.endpoint(userIds),
                    method: "delete"
                }
            )
        }
    }

    /**
     * 修改用户
     * @param userId 用户Id
     * @param userForm 用户表单
     */
    static UPDATE = {
        endpoint: (userId: string): string => {
            return `${API_BASE}${API_SUFFIXES.UPDATE.replace("{userId}", userId)}`;
        },
        permission: "system:user:update",
        request: (userId: string, userForm: UserForm) => {
            return request<void>({
                url: UserAPI.UPDATE.endpoint(userId),
                method: "put",
                data: userForm
            })
        }
    }

    /**
     * 修改用户状态
     * @param userId 用户Id
     * @param status 修改状态
     */
    static UPDATE_STATUS = {
        endpoint: (userId: string): string => {
            return `${API_BASE}${API_SUFFIXES.UPDATE_STATUS.replace("{userId}", userId)}`;
        },
        permission: "system:user:update",
        request: (userId: string, status: boolean) => {
            return request<void>({
                url: UserAPI.UPDATE_STATUS.endpoint(userId),
                method: "patch",
                params: {status: status}
            })
        }
    }

    /**
     * 重置用户密码（管理员）
     * @param userId 用户Id
     * @param password 修改密码
     */
    static ADMIN_RESET_PASSWORD = {
        endpoint: (userId: string): string => {
            return `${API_BASE}${API_SUFFIXES.ADMIN_RESET_PASSWORD.replace("{userId}", userId)}`
        },
        permission: "system:user:reset-pwd",
        request: (userId: string, password: string) => {
            return request<void>({
                url: UserAPI.ADMIN_RESET_PASSWORD.endpoint(userId),
                method: "patch",
                params: {password: password}
            })
        }
    }
}
