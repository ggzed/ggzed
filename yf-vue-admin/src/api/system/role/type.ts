/**
 * 角色分页查询参数
 */
export interface RolePageQuery extends PageQuery {
    /**
     * 关键字(角色名/角色编码)
     */
    keywords?: string;
}

/**
 * RolePageVO，数据集合
 */
export interface RolePageVO {
    /**
     * 角色编码
     */
    code?: string;

    /**
     * 角色ID
     */
    id?: number;
    /**
     * 角色名称
     */
    name?: string;
    /**
     * 排序
     */
    sort?: number;
    /**
     * 角色状态
     */
    status?: number;
}

/**
 * RoleForm，角色表单
 */
export interface RoleForm {
    /**
     * 角色ID
     */
    id?: number;
    /**
     * 角色编码
     */
    code?: string;
    /**
     * 数据权限(0-所有数据；1-部门及子部门数据；2-本部门数据；3-本人数据)
     */
    dataScope?: number;
    /**
     * 角色名称
     */
    name: string;
    /**
     * 显示顺序
     */
    sort?: number;
    /**
     * 角色状态(1-正常；0-停用)
     */
    status?: number;
}
