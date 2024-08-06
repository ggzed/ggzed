import {RouteRecordRaw} from "vue-router";
import {StoreTypeEnum} from "@/store/type";
import {constantRoutes} from "@/router/core/ConstantRoutes";
import {LoginResult} from "@/api/auth/type";
import {UserInfo} from "@/api/system/user/type";
import {UserAPI} from "@/api/system/user";
import {MenuAPI} from "@/api/system/menu";
import {filterAsyncRoutes} from "@/router/core/AsyncRoutes";
import {resetRouter} from "@/router";
import {AuthAPI} from "@/api/auth";

// 刷新 token 的单例对象 : Promise<void>
let singletonRefreshToken: Promise<LoginResult> | null = null

export interface UserBaseInfo {
    /** 用户id */
    userId?: string;
    /** 用户名 */
    username?: string;
    /** 用户昵称 */
    nickname?: string;
    /** 手机号 */
    phoneNumber?: string;
    /** 邮箱 */
    email?: string;
    /** 头像地址 */
    avatar?: string;
}

export const useUserStore = defineStore(StoreTypeEnum.USER, {
    state: (): {
        userInfo: UserBaseInfo,
        authInfo: LoginResult,
        routes: RouteRecordRaw[]
        roles: string[],
        permissions: string[],
    } => {
        return {
            // 用户基本信息
            userInfo: {},
            // 认证信息
            authInfo: {},
            // 路由信息
            routes: constantRoutes,
            // 角色信息
            roles: [],
            // 权限信息
            permissions: [],
        }
    },
    persist: {
        paths: ['authInfo'],
    },
    getters: {},
    actions: {
        /**
         * 设置权限信息
         * @param authInfo 权限信息
         */
        setAuthInfo(authInfo: LoginResult) {
            this.authInfo = authInfo;
        },
        /**
         * 获取用户登录信息
         */
        getUserInfo() {
            return new Promise<UserInfo>((resolve) => {
                UserAPI.ME.request().then(({data}) => {
                    const {roles, permissions, ...userInfo} = data;
                    // 记录用户基本信息
                    this.userInfo = userInfo;
                    // 记录角色信息
                    this.roles = roles;
                    // 记录权限信息
                    this.permissions = permissions;
                    resolve(data);
                }).catch(() => {
                    // 网络异常
                    ElNotification({
                        title: '无法获取到个人信息',
                        message: '网络原因或未开启后端服务',
                        type: 'warning',
                    })
                })
            })
        },
        /**
         * 生成路由
         * @param roles 角色信息
         */
        generateRoutes(roles: string[]): Promise<RouteRecordRaw[]> {
            return new Promise<RouteRecordRaw[]>((resolve) => {
                // 接口获取所有路由
                MenuAPI.ROUTES.request().then(({data}) => {
                    // 根据角色获取有访问权限的路由
                    const accessedRoutes = filterAsyncRoutes(data, roles);
                    this.routes = this.routes.concat(accessedRoutes)
                    resolve(accessedRoutes);
                })
            });
        },
        /**
         * 重置 token ,退出登录
         */
        resetToken(): Promise<void> {
            return new Promise<void>((resolve) => {
                this.userInfo = {};
                this.authInfo = {};
                this.routes = constantRoutes;
                this.roles = [];
                this.permissions = [];
                resetRouter();
                resolve();
            });
        },
        /**
         * 刷新 token
         */
        refreshToken(): Promise<LoginResult> {
            // 如果 singletonRefreshToken 不为 null 说明已经在刷新中，直接返回
            if (singletonRefreshToken !== null) {
                return singletonRefreshToken
            }
            // 设置 singletonRefreshToken 为一个 Promise 对象 , 处理刷新 token 请求
            singletonRefreshToken = new Promise<LoginResult>(async (resolve) => {
                await AuthAPI.REFRESH.request({
                    accessToken: this.authInfo.accessToken as string,
                    refreshToken: this.authInfo.refreshToken as string
                }).then(({data}) => {
                    // 设置刷新后的Token
                    this.authInfo = data
                    // 刷新路由
                    resolve(data)
                }).catch(() => {
                    this.resetToken()
                })
            })
            // 最终将 singletonRefreshToken 设置为 null, 防止 singletonRefreshToken 一直占用
            singletonRefreshToken.finally(() => {
                singletonRefreshToken = null;
            })
            return singletonRefreshToken
        },
        setUserInfo(userInfo: UserInfo) {
            this.userInfo = userInfo;
        }
    }
})
