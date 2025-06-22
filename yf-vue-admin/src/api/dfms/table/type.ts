/**
 * DfmsTablePageQuery, 查询条件
 */
export interface DfmsTablePageQuery extends PageQuery {
    /**
     * 数据库表名
     */
    tableName?: string;

    /**
     * 数据库表描述
     */
    tableComment?: string;

    /**
     * ddl
     */
    ddl?: string;

    /**
    * 状态(1-在线；0-离线)集合
    */
    status?: number[];

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
 * DfmsTablePageVO, 展示集合
 */
export interface DfmsTablePageVO {
    /**
     * 数据库表名
     */
    tableName?: string;

    /**
     * 数据库表描述
     */
    tableComment?: string;

    /**
     * ddl
     */
    ddl?: string;

    /**
     * 状态(1-在线；0-离线)
     */
    status?: number;

}

/**
 * DfmsTableForm, 表单数据
 */
export interface DfmsTableForm {
    /**
     * 数据库表名
     */
    tableName?: string;

    /**
     * 数据库表描述
     */
    tableComment?: string;

    /**
     * ddl
     */
    ddl?: string;

    /**
     * 状态(1-在线；0-离线)
     */
    status?: number;

}
