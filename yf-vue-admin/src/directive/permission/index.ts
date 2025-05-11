import {Directive, DirectiveBinding} from "vue";
import {useUserStore} from "@/store/modules/user";
import {SystemConstant} from "@/constants/system";

/**
 * 按钮权限
 */
export const PermissionChecker: Directive = {
    mounted(el: HTMLElement, binding: DirectiveBinding) {
        const {permissions, roles} = useUserStore();
        // 管理员不需要认证
        if (roles.some((role => role === SystemConstant.ADMIN))) {
            return true;
        }
        // 「」按钮权限校验
        const {value} = binding;
        if (value) {
            const hasPermission = permissions?.some((permission) => {
                return value.includes(permission);
            });

            if (!hasPermission) {
                el.parentNode && el.parentNode.removeChild(el);
            }
        } else {
            throw new Error(
                "need perms! Like v-permission=\"['sys:user:add','sys:user:edit']\""
            );
        }
    },
};


/**
 * 角色权限
 */
export const RoleChecker: Directive = {
    mounted(el: HTMLElement, binding: DirectiveBinding) {
        const {roles} = useUserStore();
        // 管理员不需要认证
        if (roles.some((role => role === SystemConstant.ADMIN))) {
            return true;
        }
        // 「」角色校验
        const {value} = binding;
        if (value) {

            const hasRoles = roles.some((role) => {
                return value.includes(role);
            });

            if (!hasRoles) {
                el.parentNode && el.parentNode.removeChild(el);
            }
        } else {
            throw new Error(
                "need perms! Like v-role=\"['ADMIN','TEST']\""
            );
        }
    },
};
