import request from "@/utils/request";
import {AxiosPromise} from "axios";
import {CaptchaResult, LoginParams, LoginResult, RefreshTokenParams} from "./type"
import {AuthFactory} from "@/api/auth/factory";


const API_BASE = '/auth';

const API_SUFFIXES = {
    /** 授权登录 */
    LOGIN: '/login/{type}',
    /** 刷新 TOKEN */
    REFRESH: '/refresh',
    /** 获取图形验证码 */
    CAPTCHA_CODE: '/captcha/code',
    /** 发送邮箱 */
    EMAIL_CODE: '/email/code',
    /** 退出登录 */
    LOGOUT: '/logout',
    /** 跳转第三方登录 */
    REDIRECT_LOGIN: '/{type}/redirect'
};

// 定义 AuthAPI 类
export class AuthAPI {
    /**
     * 授权登录
     * @param {string} type 登录类型 => 可用值: USERNAME_PASSWORD, PHONE, EMAIL, GITEE, GITHUB
     * @param {object} params 统一登录表单
     * @param {string} params.username 用户名
     * @param {string} params.phoneNumber 手机号
     * @param {string} params.email 邮箱
     * @param {string} params.password 密码
     * @param {string} params.verifyCode 验证码
     * @param {string} params.verifyCodeKey 验证码标识 key
     * @param {string} params.smsCode 短信验证码
     * @param {string} params.emailCode 邮箱验证码
     * @param {object} params.oauth 第三方登录 Oauth 授权对象
     */
    static LOGIN = {
        endpoint: (type: string): string => {
            return `${API_BASE}${API_SUFFIXES.LOGIN.replace("{type}", type)}`;
        },
        request: (type: string, params: LoginParams): AxiosPromise<LoginResult> => {
            return request<LoginResult>({
                url: AuthAPI.LOGIN.endpoint(type),
                method: "post",
                data: AuthFactory.createLoginForm(type, params)
            });
        }
    };

    /**
     * 退出登录
     */
    static LOGOUT = {
        endpoint: `${API_BASE}${API_SUFFIXES.LOGOUT}`,
        request: (): AxiosPromise<void> => {
            return request<void>({
                url: AuthAPI.LOGOUT.endpoint,
                method: "delete"
            });
        }
    };

    /**
     * 刷新 TOKEN
     * @param {object} params 刷新token表单
     * @param {string} params.accessToken 访问token
     * @param {string} params.refreshToken 刷新token
     * @returns
     */
    static REFRESH = {
        endpoint: `${API_BASE}${API_SUFFIXES.REFRESH}`,
        request: (params: RefreshTokenParams): AxiosPromise<LoginResult> => {
            return request<LoginResult>({
                url: AuthAPI.REFRESH.endpoint,
                method: "patch",
                data: params
            });
        }
    };

    /**
     * 获取图形验证码
     */
    static CAPTCHA_CODE = {
        endpoint: `${API_BASE}${API_SUFFIXES.CAPTCHA_CODE}`,
        request: (): AxiosPromise<CaptchaResult> => {
            return request<CaptchaResult>({
                url: AuthAPI.CAPTCHA_CODE.endpoint,
                method: "get"
            });
        }
    };

    /**
     * 发送邮箱验证码
     */
    static EMAIL_CODE = {
        endpoint: `${API_BASE}${API_SUFFIXES.EMAIL_CODE}`,
        request: (email: string): AxiosPromise<void> => {
            return request<void>({
                url: AuthAPI.EMAIL_CODE.endpoint,
                method: "get",
                params: {"email": email}
            });
        }
    }

    /**
     * 第三方登录跳转
     */
    static REDIRECT_LOGIN = {
        endpoint: (type: string): string => {
            return `${API_BASE}${API_SUFFIXES.REDIRECT_LOGIN.replace("{type}", type)}`;
        },
        request: (type: string): AxiosPromise<string> => {
            return request<string>({
                url: AuthAPI.REDIRECT_LOGIN.endpoint(type),
                method: "get"
            });
        }
    };
}
