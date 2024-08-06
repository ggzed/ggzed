export interface DeptPageQuery {
    /**
     * 是否隐藏(0: 显示, 1: 隐藏)
     */
    status?: number;
    /**
     * 关键字(部门名称)
     */
    keywords?: string;
}

export interface DeptPageVO {
    /** 子部门 */
    children?: DeptPageVO[];
    /** 部门 是否可见(0: 显示 ; 1: 隐藏)*/
    status?: number;
    /** 部门 ID */
    id?: number;
    /** 部门 名称 */
    name?: string;
    /** 父部门 ID */
    parentId?: number;
    /** 部门 排序(数字越小排名越靠前) */
    sort?: number;
    /** 部门 类型 */
    type?: number;
}

/**
 * 部门新增删除表单
 */
export interface DeptForm {
    /**
     * 部门ID
     */
    id?: number;
    /**
     * 显示状态(0: 显示;1: 隐藏)
     */
    status?: number;
    /**
     * 菜单名称
     */
    name?: string;
    /**
     * 排序(数字越小排名越靠前)
     */
    sort?: number;
    /**
     * 父菜单ID
     */
    parentId?: number;
}
