package com.yf.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.model.entity.OperateLog;
import com.yf.model.query.OperationLogQuery;
import com.yf.model.vo.OperationLogVO;
import com.yf.model.vo.VisitTrendVO;

import java.time.LocalDate;
import java.util.List;

/**
 * 操作日志-SysOperateLogService
 *
 * @author YiFei
 * @since 2024-04-15 20:51:58
 */
public interface IOperateLogService extends IService<OperateLog> {
    /**
     * 分页查询操作日志数据
     *
     * @param queryParams 分页参数
     * @return 分页数据
     */
    IPage<OperationLogVO> getOperationLogPage(OperationLogQuery queryParams);

    /**
     * 删除日志信息
     *
     * @param logIds 日志Id集合
     * @return 是否删除成功
     */
    boolean deleteOperationLog(List<Long> logIds);

    /**
     * 访问趋势
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return ECharts图展示内容
     */
    VisitTrendVO getVisitTrend(LocalDate startDate, LocalDate endDate);
}

