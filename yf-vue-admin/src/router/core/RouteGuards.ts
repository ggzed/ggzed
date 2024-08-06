import {
    NavigationFailure,
    NavigationGuardNext,
    RouteLocationNormalized,
    RouteLocationNormalizedLoaded,
    Router,
    RouteRecordRaw
} from "vue-router";
import NProgress from 'nprogress'; // 导入 NProgress
import "nprogress/nprogress.css"; // 导入 NProgress 的样式
import {useUserStore} from "@/store/modules/user";
import {RouteConstant} from "@/constants/route";

NProgress.configure({
    showSpinner: false,     // 是否显示旋转进度条
    easing: 'ease',         // 进度条动画效果
    speed: 500,             // 进度条加载速度，单位是毫秒
    minimum: 0.1,           // 最小百分比
    trickleSpeed: 200,      // 自动增加进度条的速度
});

export function createRouterGuards(router: Router) {
    // 前置路由守卫
    router.beforeEach(async (to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => {
        NProgress.start()
        // 1. 判断是否需要登录权限
        const userStore = useUserStore();
        // 2. 判断是否登录 (jwt 最小长度 57)
        if (userStore.authInfo.accessToken && userStore.authInfo.accessToken.length >= 57) {
            // 表示已登录
            // 1. 判断是否访问登录页面
            if (to.path === RouteConstant.LOGIN_PATH) {
                // 1.1 如果已登录，跳转首页
                next({path: RouteConstant.HOME_PATH});
                NProgress.done();
            } else {
                // 1.2 判断是否需要 加载过动态路由 & 获取用户基础信息
                if (userStore.roles && userStore.roles.length > 0) {
                    // 1.2.1 如果已加载过动态路由判断是否有匹配的路由
                    // 未匹配到任何路由，跳转404
                    if (to.matched.length === 0) {
                        /*
                        * 方式一 : 访问未知页面时，如果为系统已有的页面，则永远不会跳转到 NOT_FOUND_PATH
                        * 方式二 : 访问未知页面时，跳转到 NOT_FOUND_PATH
                        * */
                        // from.name ? next({ name: from.name }) : next(RouteConstant.NOT_FOUND_PATH);
                        next(RouteConstant.NOT_FOUND_PATH);
                    } else {
                        next();
                    }
                } else {
                    const {roles} = await userStore.getUserInfo();
                    const accessRoutes = await userStore.generateRoutes(roles);
                    accessRoutes.forEach((route: RouteRecordRaw) => {
                        router.addRoute(route);
                    });
                    next({...to, replace: true});
                }
            }
        } else {
            // 表示未登录
            // 1. 判断是否在白名单中
            if (RouteConstant.GUARDS_WHITE_LIST.indexOf(to.path) !== -1) {
                next()
            } else {
                next({path: '/login', query: {redirect: to.path, ...to.query}});
                NProgress.done();
            }
        }
    })
    // 后置路由守卫
    router.afterEach((to: RouteLocationNormalized, from: RouteLocationNormalized, failure?: NavigationFailure | void) => {
        NProgress.done()
    })
    // 错误路由守卫
    router.onError((error, to: RouteLocationNormalized, from: RouteLocationNormalizedLoaded) => {
        console.error(error, to, from)
        // router.replace({path: RouteConstant.SERVER_ERROR_PATH}).catch(() => console.error("跳转错误页面失败"))
        NProgress.done()
    });
}
