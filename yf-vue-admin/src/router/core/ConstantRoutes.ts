import {RouteRecordRaw} from "vue-router";
import {RouteConstant} from "@/constants/route";

export const Layout = () => import("@/layout/index.vue");
export const Home = () => import("@/views/public/home/index.vue");
export const HotList = () => import("@/views/public/hot-list/index.vue");
export const TodayAvatar = () => import("@/views/public/today-avatar/index.vue");
export const UserProfile = () => import("@/views/public/user-profile/index.vue");
export const Login = () => import("@/views/public/login/index.vue");
export const OauthRedirect = () => import("@/views/public/oauth-redirect/index.vue");
export const Redirect = () => import("@/views/public/redirect/index.vue");
export const NotFound = () => import("@/views/public/error/404/index.vue");
export const SERVER_ERROR = () => import("@/views/public/error/500/index.vue");
// 首页路由
export const HOME_ROUTE_RAW: RouteRecordRaw = {
    path: RouteConstant.HOME_PATH.substring(1),
    name: "Home",
    component: Home,
    meta: {
        title: '首页',
        icon: 'logo',
        affix: true,
        keepAlive: true,
        hidden: false,
    }
}

// 静态路由
export const constantRoutes: RouteRecordRaw[] = [
    {
        path: "/",
        name: "/",
        component: Layout,
        meta: {
            hidden: false,
        },
        redirect: RouteConstant.HOME_PATH,
        children: [
            HOME_ROUTE_RAW,
            {
                path: "user-profile",
                name: "UserProfile",
                component: UserProfile,
                meta: {
                    title: "个人中心",
                    hidden: true,
                    keepAlive: true
                }
            },
            {
                path: "hot-list",
                name: "HotList",
                component: HotList,
                meta: {
                    title: '每日热点',
                    hidden: true,
                    keepAlive: true
                },
                children: []
            },
            {
                path: "today-avatar",
                name: "TodayAvatar",
                component: TodayAvatar,
                meta: {
                    title: '照片推荐墙',
                    hidden: true,
                    keepAlive: true
                },
                children: []
            },
            {
                path: "404",
                name: "NotFound",
                component: NotFound,
                meta: {
                    title: "404",
                    hidden: true,
                    keepAlive: false
                }
            }, {
                path: "500",
                name: "SERVER_ERROR",
                component: SERVER_ERROR,
                meta: {
                    title: "500",
                    hidden: true,
                    keepAlive: false
                }
            }]
    },
    {
        path: "/redirect",
        name: "/redirect",
        component: Layout,
        meta: {hidden: true},
        redirect: "/redirect/",     // /redirect/ 重定向到表示主页
        children: [
            {
                path: "/redirect/:path(.*)",
                name: "Redirect",
                meta: {hidden: true},
                component: Redirect,
            },
        ],
    },
    {
        path: RouteConstant.LOGIN_PATH,
        name: "Login",
        component: Login,
        meta: {
            title: '登录页面',
            hidden: true,
        },
        children: []
    },
    {
        path: "/oauth-redirect",
        name: "OauthRedirect",
        component: OauthRedirect,
        meta: {
            title: '授权分发页面',
            hidden: true,
        },
        children: []
    }
]
