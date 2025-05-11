import request from "@/utils/request";
import {AxiosPromise} from "axios";
import {ResetUserPasswordForm, UserProfileForm, UserProfileInfoVO} from "@/api/system/user-profile/type";
import {Oauth} from "@/api/auth/type";

const API_BASE = '/user/profile';


const API_SUFFIXES = {
    /** 获取个人信息 */
    INFO: "",
    /** 修改个人信息 */
    UPDATE: "",
    /** 绑定第三方账号 */
    BIND_THIRD_PARTY: "/{type}/bind-third-party",
    /** 解绑第三方账号 */
    UNBIND_THIRD_PARTY: "/{oauthId}/unbind-third-party",
    /** 修改密码 */
    UPDATE_PASSWORD: "/password",
    /** 修改用户头像 */
    UPLOAD_AVATAR: "/avatar",
    /** 校验密码是否存在 */
    CHECK_PASSWORD_EXISTENCE: "/password/existence",
    /** 修改用户名 */
    UPDATE_USERNAME: "/username"
};

export class UserProfileAPI {
    /**
     * 获取当前登录的用户信息
     */
    static INFO = {
        endpoint: `${API_BASE}${API_SUFFIXES.INFO}`,
        request: (): AxiosPromise<UserProfileInfoVO> => {
            return request<UserProfileInfoVO>({
                url: UserProfileAPI.INFO.endpoint,
                method: "get",
            })
        }
    }
    /**
     * 修改个人信息
     */
    static UPDATE = {
        endpoint: `${API_BASE}${API_SUFFIXES.UPDATE}`,
        request: (userProfileForm: UserProfileForm): AxiosPromise<void> => {
            return request<void>({
                url: UserProfileAPI.UPDATE.endpoint,
                method: "put",
                data: userProfileForm
            })
        }
    }
    /**
     * 修改个人密码
     */
    static UPDATE_PASSWORD = {
        endpoint: `${API_BASE}${API_SUFFIXES.UPDATE_PASSWORD}`,
        request: (resetUserPasswordForm: ResetUserPasswordForm): AxiosPromise<void> => {
            return request<void>({
                url: UserProfileAPI.UPDATE_PASSWORD.endpoint,
                method: "patch",
                data: resetUserPasswordForm
            })
        }
    }
    /**
     * 校验密码是否存在
     */
    static CHECK_PASSWORD_EXISTENCE = {
        endpoint: `${API_BASE}${API_SUFFIXES.CHECK_PASSWORD_EXISTENCE}`,
        request: (): AxiosPromise<boolean> => {
            return request<boolean>({
                url: UserProfileAPI.CHECK_PASSWORD_EXISTENCE.endpoint,
                method: "get"
            })
        }
    }
    /**
     * 校验
     */
    static UPDATE_USERNAME = {
        endpoint: `${API_BASE}${API_SUFFIXES.UPDATE_USERNAME}`,
        request: (username: string): AxiosPromise<void> => {
            return request<void>({
                url: UserProfileAPI.UPDATE_USERNAME.endpoint,
                method: "patch",
                params: {username: username}
            })
        }
    }

    /**
     * 绑定第三方平台账号
     */
    static BIND_THIRD_PARTY = {
        endpoint: (type: string): string => {
            return `${API_BASE}${API_SUFFIXES.BIND_THIRD_PARTY.replace('{type}', type)}`;
        },
        request: (type: string, oauth: Oauth): AxiosPromise<UserProfileInfoVO> => {
            return request<UserProfileInfoVO>({
                url: UserProfileAPI.BIND_THIRD_PARTY.endpoint(type),
                method: "post",
                data: oauth
            })
        }
    }
    /**
     * 解绑第三方平台账号
     */
    static UNBIND_THIRD_PARTY = {
        endpoint: (oauthId: string): string => {
            return `${API_BASE}${API_SUFFIXES.UNBIND_THIRD_PARTY.replace('{oauthId}', oauthId)}`;
        },
        request: (oauthId: string): AxiosPromise<void> => {
            return request<void>({
                url: UserProfileAPI.UNBIND_THIRD_PARTY.endpoint(oauthId),
                method: "delete"
            })
        }
    }
    
    /**
     * 修改头像
     */
    static UPLOAD_AVATAR = {
        endpoint: `${API_BASE}${API_SUFFIXES.UPLOAD_AVATAR}`
    }
}
