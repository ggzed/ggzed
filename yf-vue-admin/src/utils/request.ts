import axios, {AxiosResponse, InternalAxiosRequestConfig} from "axios";
import {useUserStore} from "@/store/modules/user";
import {RequestConstant} from "@/constants/request";
import {LoginResult} from "@/api/auth/type";

/**
 * 使用方式 :
 *
 * import request from "@/utils/request";
 *
 * export function functionName(data: ?) : AxiosPromise<?> {
 *   return request({
 *     url: "",
 *     method: "",
 *     data: data
 *   });
 * }
 */
// 创建 axios 实例
const service = axios.create({
    baseURL: import.meta.env.VITE_APP_API_URL,
    timeout: 50000,
    headers: {"Content-Type": "application/json;charset=utf-8"},
});

// 请求拦截器
service.interceptors.request.use((config: InternalAxiosRequestConfig) => {
        const userStore = useUserStore()
        if (userStore.authInfo.accessToken && userStore.authInfo.accessToken !== "") {
            // 设置头部 token
            config.headers.Authorization = RequestConstant.Header.AuthorizationPrefix + userStore.authInfo.accessToken;
        }
        return config;
    },
    (error: any) => {
        return Promise.reject(error);
    }
);

// 响应拦截器
service.interceptors.response.use(async (response: AxiosResponse) => {
        const {code, msg} = response.data;
        if (code === RequestConstant.Code.SUCCESS) {
            return response.data;
        }
        // 响应数据为二进制流处理(Excel导出)
        if (response.data instanceof ArrayBuffer || response.data instanceof Blob) {
            return response;
        }
        if (code === RequestConstant.Code.AUTH_USER_INFO_ERROR) {
            const userStore = useUserStore()
            await userStore.resetToken()
        }

        ElMessage({
            message: msg || "Error",
            type: 'error',
        })

        return Promise.reject(new Error(msg || "Error"));
    },
    async (error: any) => {
        const userStore = useUserStore()
        if (error.response?.status === 401) {
            if (error.response?.data?.code === RequestConstant.Code.AUTH_TOKEN_EXPIRED) {
                // token 过期处理
                // 1. 刷新 token
                const loginResult: LoginResult = await userStore.refreshToken()
                if (loginResult) {
                    // refreshToken 未过期
                    // 2.1 重构请求头
                    error.config.headers.Authorization = RequestConstant.Header.AuthorizationPrefix + userStore.authInfo.accessToken;
                    // 2.2 请求
                    return await service.request(error.config);
                } else {
                    // refreshToken 过期
                    // 1. 重置登录 token , 跳转登录页
                    await userStore.resetToken()
                }
            } else {
                // 401 默认处理
                await userStore.resetToken()
            }
        } else if (error.response?.status === 403) {
            ElNotification({
                title: 'Warning',
                message: error.response?.data?.msg ?? error.message,
                type: 'warning',
            })
            if (error.response?.data?.code === RequestConstant.Code.AUTH_USER_ELSEWHERE_LOGIN) {
                // 账号在别处登录处理
                // 1. 重置登录 token , 跳转登录页
                await userStore.resetToken()
            }
        } else {
            // 异常默认处理
            console.log("请求响应错误", error)
            ElMessage({
                message: error.response.data.msg || error.message,
                type: 'error',
            })
        }
        return Promise.reject(error.message);
    }
);

// 导出 axios 实例
export default service;
