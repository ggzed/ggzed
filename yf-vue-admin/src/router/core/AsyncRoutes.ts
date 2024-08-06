import {RouteRecordRaw} from "vue-router";
import {Layout, NotFound} from "@/router/core/ConstantRoutes";
import {RouteVO} from "@/api/system/menu/type";

const modules = import.meta.glob("../../views/**/**.vue");

/**
 * 递归过滤有权限的异步(动态)路由
 *
 * @param routes 接口返回的异步(动态)路由
 * @param roles 用户角色集合
 * @returns 返回用户有权限的异步(动态)路由
 */
export function filterAsyncRoutes(routes: RouteVO[], roles: string[]) {
    const asyncRoutes: RouteRecordRaw[] = [];

    routes.forEach((route) => {
        const tempRoute: RouteRecordRaw = {...route, component: Layout, children: []} as RouteRecordRaw; // ES6扩展运算符复制新对象
        // 1. 未传入 name 则使用path作为 name
        if (!route.name) {
            tempRoute.name = route.path;
        }
        // 2. 判断是否有权限
        if (hasPermission(roles, tempRoute)) {
            // 2. 1 判断是否匹配到子路由
            if (route.component !== 'Layout') {
                const component = modules[`../../views/${route.component}.vue`];
                if (component) {
                    tempRoute.component = component;
                } else {
                    tempRoute.component = NotFound;
                }
            }
            // 2. 2 判断是否含有子路由
            if (route.children) {
                tempRoute.children = filterAsyncRoutes(route.children, roles);
            }
            // 2. 3 添加到异步路由中
            asyncRoutes.push(tempRoute);
        }
    });

    return asyncRoutes;
}

/**
 * 用户角色集合是否包含路由的 meta.roles 判断用户是否有权限
 *
 * @param roles 用户角色集合
 * @param route 路由
 * @returns
 */
export function hasPermission(roles: string[], route: RouteRecordRaw): boolean {
    if (route.meta && route.meta.roles) {
        return roles.some((role) => {
            if (route.meta?.roles) {
                return route.meta.roles.includes(role);
            }
        });
    }
    return false;
}

/**
 * routeVo -> RouteRecordRaw
 */
export function routeVo2RecordRaw(routeVo: RouteVO): RouteRecordRaw {
    const {path, component, redirect, name, meta, children} = routeVo;

    const routeRecordRaw: RouteRecordRaw = {
        path,
        component: Layout,
        redirect,
        name,
        meta: {
            title: meta.title,
            icon: meta.icon,
            hidden: meta.hidden,
            keepAlive: meta.keepAlive,
            affix: meta.affix,
            breadcrumb: meta.breadcrumb,
            showSingleChildren: meta.showSingleChildren,
            roles: meta.roles,
        },
        children: []
    };

    if (children) {
        routeRecordRaw.children = children.map(child => routeVo2RecordRaw(child));
    }

    return routeRecordRaw;
}
