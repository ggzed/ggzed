/**
 * DfmsNodePageQuery, 查询条件
 */
export interface DfmsNodePageQuery extends PageQuery {
    /**
     * 名称
     */
    name?: string;

    /**
     * IP地址
     */
    ip?: string;

    /**
     * 端口
     */
    port?: string;

    /**
     * 用户名
     */
    username?: string;

    /**
     * 角色(1-主节点；2-数据节点)
     */
    role?: number;

    /**
    * 角色状态(1-在线；0-离线)集合
    */
    status?: number[];

    /**
     * cpu占用
     */
    cpu?: string;

    /**
     * 内存占用
     */
    memory?: string;

    /**
     * 创建人
     */
    createBy?: string;

    /**
     * 创建时间开始
     */
    createTimeStart?: string;

    /**
     * 创建时间结束
     */
    createTimeEnd?: string;

    /**
     * 修改人Id开始
     */
    updateByStart?: string;

    /**
     * 修改人Id结束
     */
    updateByEnd?: string;

    /**
     * 修改时间开始
     */
    updateTimeStart?: string;

    /**
     * 修改时间结束
     */
    updateTimeEnd?: string;

}

/**
 * DfmsNodePageVO, 展示集合
 */
export interface DfmsNodePageVO {
    /**
     * 名称
     */
    name?: string;

    /**
     * IP地址
     */
    ip?: string;

    /**
     * 端口
     */
    port?: string;

    /**
     * 用户名
     */
    username?: string;

    /**
     * 密码
     */
    password?: string;

    /**
     * 角色(1-主节点；2-数据节点)
     */
    role?: number;

    /**
     * 角色状态(1-在线；0-离线)
     */
    status?: number;

    /**
     * cpu占用
     */
    cpu?: string;

    /**
     * 内存占用
     */
    memory?: string;

}

/**
 * DfmsNodeForm, 表单数据
 */
export interface DfmsNodeForm {
    /**
     * 名称
     */
    name?: string;

    /**
     * IP地址
     */
    ip?: string;

    /**
     * 端口
     */
    port?: string;

    /**
     * 用户名
     */
    username?: string;

    /**
     * 密码
     */
    password?: string;

    /**
     * 角色(1-主节点；2-数据节点)
     */
    role?: number;

    /**
     * 角色状态(1-在线；0-离线)
     */
    status?: number;

    /**
     * cpu占用
     */
    cpu?: string;

    /**
     * 内存占用
     */
    memory?: string;

}
