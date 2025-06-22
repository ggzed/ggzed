/**
 * DfmsTableIndexPageQuery, 查询条件
 */
export interface DfmsTableIndexPageQuery extends PageQuery {
    /**
     * 索引名
     */
    indexName?: string;

    /**
     * 列
     */
    columns?: string;

    /**
    * 索引类型集合
    */
    indexType?: string[];

    /**
     * 是否唯一(1-是；0-否)
     */
    isOnly?: number;

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
 * DfmsTableIndexPageVO, 展示集合
 */
export interface DfmsTableIndexPageVO {
    /**
     * 索引名
     */
    indexName?: string;

    /**
     * 列
     */
    columns?: string;

    /**
     * 索引类型
     */
    indexType?: string;

    /**
     * 是否唯一(1-是；0-否)
     */
    isOnly?: number;

    /**
     * 状态(1-在线；0-离线)
     */
    status?: number;

}

/**
 * DfmsTableIndexForm, 表单数据
 */
export interface DfmsTableIndexForm {
    /**
     * 索引名
     */
    indexName?: string;

    /**
     * 列
     */
    columns?: string;

    /**
     * 索引类型
     */
    indexType?: string;

    /**
     * 是否唯一(1-是；0-否)
     */
    isOnly?: number;

    /**
     * 状态(1-在线；0-离线)
     */
    status?: number;

}
