/**
 * DfmsDbPageQuery, 查询条件
 */
export interface DfmsDbPageQuery extends PageQuery {
    /**
     * 名称
     */
    name?: string;

    /**
     * 字符集
     */
    charset?: string;

    /**
     * 排序规则
     */
    collation?: string;

    /**
     * 大小
     */
    size?: string;

    /**
     * 表数量
     */
    tables?: string;

    /**
    * 类型(1-pgsql；2-timescale；3-vector；4-geospatial)集合
    */
    type?: number;

    /**
     * 保留策略(timescale)
     */
    retentionPolicy?: string;

    /**
     * 序列数(timescale)
     */
    sequenceNumber?: string;

    /**
     * 数据点数(timescale)
     */
    dataPointsNumber?: string;

    /**
     * 维度(vector)
     */
    dimension?: string;

    /**
     * 向量数量(vector)
     */
    vectorNumber?: string;

    /**
    * 索引类型(vector)集合
    */
    indexType?: string[];

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
 * DfmsDbPageVO, 展示集合
 */
export interface DfmsDbPageVO {
    /**
     * 名称
     */
    name?: string;

    /**
     * 字符集
     */
    charset?: string;

    /**
     * 排序规则
     */
    collation?: string;

    /**
     * 大小
     */
    size?: string;

    /**
     * 表数量
     */
    tables?: string;

    /**
     * 类型(1-pgsql；2-timescale；3-vector；4-geospatial)
     */
    type?: number;

    /**
     * 保留策略(timescale)
     */
    retentionPolicy?: string;

    /**
     * 序列数(timescale)
     */
    sequenceNumber?: string;

    /**
     * 数据点数(timescale)
     */
    dataPointsNumber?: string;

    /**
     * 维度(vector)
     */
    dimension?: string;

    /**
     * 向量数量(vector)
     */
    vectorNumber?: string;

    /**
     * 索引类型(vector)
     */
    indexType?: string;

    /**
     * 状态(1-在线；0-离线)
     */
    status?: number;

}

/**
 * DfmsDbForm, 表单数据
 */
export interface DfmsDbForm {
    /**
     * 名称
     */
    name?: string;

    /**
     * 字符集
     */
    charset?: string;

    /**
     * 排序规则
     */
    collation?: string;

    /**
     * 大小
     */
    size?: string;

    /**
     * 表数量
     */
    tables?: string;

    /**
     * 类型(1-pgsql；2-timescale；3-vector；4-geospatial)
     */
    type?: number;

    /**
     * 保留策略(timescale)
     */
    retentionPolicy?: string;

    /**
     * 序列数(timescale)
     */
    sequenceNumber?: string;

    /**
     * 数据点数(timescale)
     */
    dataPointsNumber?: string;

    /**
     * 维度(vector)
     */
    dimension?: string;

    /**
     * 向量数量(vector)
     */
    vectorNumber?: string;

    /**
     * 索引类型(vector)
     */
    indexType?: string;

    /**
     * 状态(1-在线；0-离线)
     */
    status?: number;

}
export interface DfmsDbOverviewVOs {
    data: DfmsDbOverviewVO[]
}

/**
 * DfmsDbForm, 概览
 */
export interface DfmsDbOverviewVO {

    /**
     * 名称
     */
    name?: string;
    num?: number;
    /**
     * 大小
     */
    size?: string;
    /**
     * 状态(1-在线；0-离线)
     */
    status?: number;
}