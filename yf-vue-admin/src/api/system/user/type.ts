/**
 * 用户信息
 */
export interface UserInfo {
    /** 用户id */
    userId?: string;
    /** 用户名 */
    username?: string;
    /** 用户昵称 */
    nickname?: string;
    /** 手机号 */
    phoneNumber?: string;
    /** 邮箱 */
    email?: string;
    /** 头像地址 */
    avatar?: string;
    /** 用户角色编码集合 */
    roles: string[];
    permissions: string[];
}

/**
 * 分页查询参数
 */
export interface UserPageQuery extends PageQuery {
    /**
     * 部门ID
     */
    deptId?: number;
    /**
     * 关键字(用户名/昵称/手机号/邮箱)
     */
    keywords?: string;
    /**
     * 创建时间-开始时间
     */
    startTime?: string;
    /**
     * 创建时间-结束时间
     */
    endTime?: string;
    /**
     * 用户状态
     */
    status?: string;
}

/**
 * 分页查询返回数据
 */
/**
 * UserPageVO，数据集合
 */
export interface UserPageVO {
    /**
     * 用户头像地址
     */
    avatar?: string;
    /**
     * 创建时间
     */
    createTime?: string;
    /**
     * 部门名称
     */
    deptName?: string;
    /**
     * 用户邮箱
     */
    email?: string;
    /**
     * 性别
     */
    gender?: number;
    /**
     * 用户ID
     */
    id?: string;
    /**
     * 用户昵称
     */
    nickname?: string;
    /**
     * 手机号
     */
    phoneNumber?: string;
    /**
     * 角色名称，多个使用英文逗号(,)分割
     */
    roleNames?: string;
    /**
     * 用户状态(1:启用;0:禁用)
     */
    status?: number;
    /**
     * 用户名
     */
    username?: string;
}

/**
 * 用户表单数据 : UserForm
 */
export interface UserForm {
    /**
     * 用户ID
     */
    id?: string;
    /**
     * 用户名
     */
    username: string;
    /**
     * 昵称
     */
    nickname: string;
    /**
     * 用户头像
     */
    avatar?: string;
    /**
     * 部门ID
     */
    deptId?: number;
    /**
     * 邮箱
     */
    email?: string;
    /**
     * 性别
     */
    gender?: number;
    /**
     * 手机号码
     */
    phoneNumber?: string;
    /**
     * 角色ID集合
     */
    roleIds: number[];
    /**
     * 用户状态(1:正常;0:禁用)
     */
    status?: number;
}
