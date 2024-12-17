package com.yf.log.common;

import com.yf.model.log.entity.OperateLog;

/**
 * 公共日志操作服务
 *
 * @author : YiFei
 * @since : 2024/12/17 15:19
 */
public interface ICommonOperateLogService {

    /**
     * 请选填 :
     * operate_log.title    模块标题
     * operate_log.business_type 业务类型
     * operate_log.method     方法名称
     * operate_log.operator_type 操作类型
     * operate_log.operator_param 操作参数
     * operate_log.json_result 返回结果
     * operate_log.status     操作状态
     * operate_log.error_msg  错误消息
     * operate_log.cost_time  操作耗时
     *
     * @param operateLog 操作日志
     */
    void asyncSaveOperateLog(OperateLog operateLog);
}
