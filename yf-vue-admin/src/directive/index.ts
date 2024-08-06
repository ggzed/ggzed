import type {App} from "vue";

import {PermissionChecker, RoleChecker} from "./permission";

// 全局注册 directive
export function setupDirective(app: App<Element>) {
    // 使 v-permission 在所有组件中都可用
    app.directive("permission", PermissionChecker);
    // 使 v-role 在所有组件中都可用
    app.directive("role", RoleChecker);
}
