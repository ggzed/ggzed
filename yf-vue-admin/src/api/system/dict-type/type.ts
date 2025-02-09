export interface DictTypePageQuery extends PageQuery {
    /**
     * 字典名称
     */
    name?: string;
    /**
     * 状态（1正常 0停用）
     */
    status?: number;
    /**
     * 字典类型
     */
    type?: string;
}

/**
 * DictTypePageVO，数据集合
 */
export interface DictTypePageVO {
    /**
     * 主键
     */
    id?: number;
    /**
     * 字典名称
     */
    name?: string;
    /**
     * 备注
     */
    remark?: string;
    /**
     * 状态（1正常 0停用）
     */
    status?: string;
    /**
     * 字典类型
     */
    type?: number;
    /**
     * 创建时间
     */
    createTime?: string;
    /**
     * 修改时间
     */
    updateTime?: string;
}

/**
 * DictTypeForm
 */
export interface DictTypeForm {
    /**
     * 主键
     */
    id?: number;
    /**
     * 字典名称
     */
    name?: string;
    /**
     * 备注
     */
    remark?: string;
    /**
     * 状态（1正常 0停用）
     */
    status?: number;
    /**
     * 字典类型
     */
    type?: string;
}
