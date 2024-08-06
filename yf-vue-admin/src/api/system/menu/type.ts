/**
 * options -> tags 样式
 */
export const MENU_STYLE: Array<'success' | 'info' | 'warning' | 'danger' | 'primary'> = ["danger", "warning", "success", "primary", "info"]

export interface RouteVO {
    /** 路由路径 */
    path: string;
    /** 组件路径 */
    component: string;
    /** 跳转链接 */
    redirect: string;
    /** 路由名称 */
    name: string;
    /** 路由属性类型 */
    meta: {
        /** 路由title */
        title: string;
        /** ICON */
        icon?: string;
        /** 是否隐藏(true-是 false-否) */
        hidden?: boolean;
        /** 【菜单】是否开启页面缓存 */
        keepAlive?: boolean;
        /** 是否固定页签(true-是 false-否) */
        affix?: boolean;
        /** 是否显示在面包屑(true-是 false-否) */
        breadcrumb?: boolean;
        /** 【目录】只有一个子路由是否始终显示 */
        showSingleChildren?: boolean;
        /** 拥有路由权限的角色编码 */
        roles?: string[];
    }
    /** 子路由列表 */
    children?: RouteVO[];
}

export interface MenuPageQuery {
    /**
     * 是否隐藏(0: 显示, 1: 隐藏)
     */
    hidden?: number;
    /**
     * 关键字(菜单名称)
     */
    keywords?: string;
    /**
     * 菜单类型
     */
    type?: number;
}

/**
 * MenuPageVO，响应数据
 */
export interface MenuPageVO {
    /** 子菜单 */
    children?: MenuPageVO[];
    /** 组件路径 */
    component?: string;
    /** 菜单是否可见(0: 显示 ; 1: 隐藏)*/
    hidden?: number;
    /** ICON */
    icon?: string;
    /** 菜单ID */
    id?: number;
    /** 菜单名称 */
    name?: string;
    /** 父菜单ID */
    parentId?: number;
    /** 路由路径 */
    path?: string;
    /** 按钮权限标识 */
    permission?: string;
    /**跳转路径     */
    redirect?: string;
    /** 菜单排序(数字越小排名越靠前) */
    sort?: number;
    /** 菜单类型 */
    type?: number;
}

/**
 * 菜单新增删除表单
 */
export interface MenuForm {
    /**
     * 菜单ID
     */
    id?: number;
    /**
     * 组件路径(vue页面完整路径，省略.vue后缀)
     */
    component?: string;
    /**
     * 【菜单】是否固定(1:是 0:否)
     */
    affix?: number;
    /**
     * 显示状态(0: 显示;1: 隐藏)
     */
    hidden?: number;
    /**
     * 菜单图标
     */
    icon?: string;
    /**
     * 【菜单】是否开启页面缓存(1:是 0:否)
     */
    keepAlive?: number;
    /**
     * 菜单名称
     */
    name?: string;
    /**
     * 父菜单ID
     */
    parentId: number;
    /**
     * 路由路径
     */
    path?: string;
    /**
     * 权限标识
     */
    permission?: string;
    /**
     * 跳转路径
     */
    redirect?: string;
    /**
     * 【目录】是否折叠单个子菜单(1:是 0:否)
     */
    showSingleChildren?: number;
    /**
     * 排序(数字越小排名越靠前)
     */
    sort?: number;
    /**
     * 菜单类型(1-菜单；2-目录；3-外链；4-按钮权限)
     */
    type?: number;
}
