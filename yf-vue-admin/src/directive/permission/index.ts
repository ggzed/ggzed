import {Directive, DirectiveBinding} from "vue";
import {useUserStore} from "@/store/modules/user";

/**
 * 按钮权限
 */
export const PermissionChecker: Directive = {
    mounted(el: HTMLElement, binding: DirectiveBinding) {
        const {permissions} = useUserStore();
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
                "need perms! Like v-has-perm=\"['sys:user:add','sys:user:edit']\""
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
                "need perms! Like v-has-perm=\"['sys:user:add','sys:user:edit']\""
            );
        }
    },
};
