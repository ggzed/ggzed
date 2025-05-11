/**
 * 字典类型
 */
export enum DictType {
    MENU = "menu",
    GENDER = "gender",
    DATA_PERMISSION = "data_permission",
    BUSINESS = "business",
    OPERATOR_TYPE = "operator_type",
    BACK_END = "back_end",
    FRONT_END = "front_end",
    JAVA_TYPE = "java_type",
    TS_TYPE = "ts_type",
    DB_TYPE = "db_type",
    FORM_TYPE = "form_type",
    QUERY_FORM_TYPE = "query_form_type",
    QUERY_TYPE = "query_type",
    SHOW_TYPE = "show_type",
}

export interface DictDataPageQuery extends PageQuery {
    /**
     * 是否默认(1:是;0:否)
     */
    defaulted?: string;
    /**
     * 字典项名称
     */
    name?: string;
    /**
     * 状态(1:正常;0:禁用)
     */
    status?: string;
}

/**
 * DictDataPageVO，数据集合
 */
export interface DictDataPageVO {
    id?: number;
    /**
     * 字典项名称
     */
    name?: string;
    /**
     * 字典项值
     */
    value?: string;

    /**
     * 排序
     */
    sort?: number;

    /**
     * 状态(1:正常;0:禁用)
     */
    status: number;
    /**
     * 是否默认(1:是;0:否)
     */
    defaulted: number;
    /**
     * 备注
     */
    remark?: string;
}

/**
 * DictDataForm，字典数据表单
 */
export interface DictDataForm {
    /**
     * 主键
     */
    id?: number;
    /**
     * 字典类型Id
     */
    dictTypeId?: number;
    /**
     * 是否默认(1:是;0:否)
     */
    defaulted?: number;
    /**
     * 字典项名称
     */
    name?: string;
    /**
     * 备注
     */
    remark?: string;
    /**
     * 排序
     */
    sort?: number;
    /**
     * 状态(1:正常;0:禁用)
     */
    status?: number;
    /**
     * 字典项值
     */
    value?: string;
}

