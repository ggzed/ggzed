export interface OperationLogQuery extends PageQuery {
    /**
     * 业务类型（0其它 1新增 2修改 3删除 ...）
     */
    businessType?: string;
    /**
     * 消耗区间（大于）, 毫秒为单位
     */
    costTime?: string;
    /**
     * 方法名称
     */
    method?: string;
    /**
     * 主机地址
     */
    operatorIp?: string;
    /**
     * 操作人员
     */
    operatorName?: string;
    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    operatorType?: string;
    /**
     * 请求URL
     */
    operatorUrl?: string;
    /**
     * 操作状态（0正常 1异常）
     */
    status?: string;
    /**
     * 模块标题
     */
    title?: string;
    endTime?: string;
    startTime?: string;
}

/**
 * OperationLogVO，数据集合
 */
export interface OperationLogVO {
    /**
     * 业务类型（0其它 1新增 2修改 3删除 ...）
     */
    businessType?: number;
    /**
     * 消耗时间
     */
    costTime?: number;
    /**
     * 操作时间
     */
    createTime?: string;
    /**
     * 错误消息
     */
    errorMsg?: string;
    /**
     * 日志主键
     */
    id?: string;
    /**
     * 返回参数
     */
    jsonResult?: string;
    /**
     * 方法名称
     */
    method?: string;
    /**
     * 浏览器信息
     */
    operatorBrowser?: string;
    /**
     * 主机地址
     */
    operatorIp?: string;
    /**
     * 操作地点
     */
    operatorLocation?: string;
    /**
     * 操作人员
     */
    operatorName?: string;
    /**
     * 操作系统
     */
    operatorOs?: string;
    /**
     * 请求参数
     */
    operatorParam?: string;
    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    operatorType?: number;
    /**
     * 请求URL
     */
    operatorUrl?: string;
    /**
     * 请求方式
     */
    requestMethod?: string;
    /**
     * 操作状态（0正常 1异常）
     */
    status?: number;
    /**
     * 模块标题
     */
    title?: string;
}
