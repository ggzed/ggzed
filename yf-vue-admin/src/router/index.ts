import type {App} from "vue";
import {constantRoutes} from "@/router/core/ConstantRoutes";
import {createRouter, createWebHistory, Router} from "vue-router";
import {createRouterGuards} from "@/router/core/RouteGuards";
import {RouteConstant} from "@/constants/route";

/**
 * 创建路由
 */
const router: Router = createRouter({
    history: createWebHistory(import.meta.env.VITE_APP_PUBLIC_PATH),
    routes: constantRoutes,
    // 刷新时，滚动条位置还原
    scrollBehavior: () => ({left: 0, top: 0}),
});

/**
 * 重置路由
 */
export function resetRouter() {
    router.replace({path: RouteConstant.LOGIN_PATH}).catch((error) => {
        console.error("路由重置失败", error)
    });
}

// 全局注册 router
export function setupRouter(app: App) {
    // 创建路由守卫
    createRouterGuards(router);
    // 注册路由
    app.use(router);
}

export default router;
