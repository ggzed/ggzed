/**
 * DfmsTableFieldsPageQuery, 查询条件
 */
export interface DfmsTableFieldsPageQuery extends PageQuery {
    /**
     * 表格展示列名
     */
    showName?: string;

    /**
     * 数据库列名
     */
    columnName?: string;

    /**
    * 数据库类型集合
    */
    columnType?: string[];

    /**
     * 数据库字段描述
     */
    columnComment?: string;

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
 * DfmsTableFieldsPageVO, 展示集合
 */
export interface DfmsTableFieldsPageVO {
    /**
     * 表格展示列名
     */
    showName?: string;

    /**
     * 数据库列名
     */
    columnName?: string;

    /**
     * 数据库类型
     */
    columnType?: string;

    /**
     * 数据库字段描述
     */
    columnComment?: string;

    /**
     * 状态(1-在线；0-离线)
     */
    status?: number;

}

/**
 * DfmsTableFieldsForm, 表单数据
 */
export interface DfmsTableFieldsForm {
    /**
     * 表格展示列名
     */
    showName?: string;

    /**
     * 数据库列名
     */
    columnName?: string;

    /**
     * 数据库类型
     */
    columnType?: string;

    /**
     * 数据库字段描述
     */
    columnComment?: string;

    /**
     * 状态(1-在线；0-离线)
     */
    status?: number;

}
