package com.yf.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.converter.OperateLogConverter;
import com.yf.mapper.log.OperateLogMapper;
import com.yf.model.log.bo.OperationLogBO;
import com.yf.model.log.bo.VisitCount;
import com.yf.model.log.entity.OperateLog;
import com.yf.model.log.query.OperationLogQuery;
import com.yf.model.vo.OperationLogVO;
import com.yf.model.vo.VisitTrendVO;
import com.yf.service.IOperateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 操作日志-SysOperateLogServiceImpl
 *
 * @author YiFei
 * @since 2024-04-15 20:51:58
 */
@Service("operateLogService")
@RequiredArgsConstructor
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements IOperateLogService {


    private final OperateLogConverter logConverter;

    /**
     * 分页查询操作日志数据
     *
     * @param queryParams 分页参数
     * @return 分页数据
     */
    @Override
    public IPage<OperationLogVO> getOperationLogPage(OperationLogQuery queryParams) {
        // 1. 创建分页对象
        Page<OperationLogVO> page = new Page<>(queryParams.getPageNum(), queryParams.getPageSize());
        // 2. 分页查询
        long total = this.baseMapper.getOperationLogTotal(queryParams);
        List<OperationLogBO> operationLogBOS = this.baseMapper.getOperationLogPage(queryParams);
        // 3. 填充参数
        page.setTotal(total);
        page.setRecords(logConverter.bos2vos(operationLogBOS));
        return page;
    }

    /**
     * 删除日志信息
     *
     * @param logIds 日志Id集合
     * @return 是否删除成功
     */
    @Override
    public boolean deleteOperationLog(List<Long> logIds) {
        return this.lambdaUpdate().in(OperateLog::getId, logIds).remove();
    }

    /**
     * 访问趋势
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return ECharts图展示内容
     */
    @Override
    public VisitTrendVO getVisitTrend(LocalDate startDate, LocalDate endDate) {
        VisitTrendVO visitTrend = new VisitTrendVO();

        // 获取指定日期范围内的所有日期
        List<String> dates = getDatesBetween(startDate, endDate);

        // 设置起始和结束时间
        String startDateTime = dates.get(0) + " 00:00:00";
        String endDateTime = dates.get(dates.size() - 1) + " 23:59:59";

        // 获取访问量和访问 IP 数的统计数据
        List<VisitCount> visitCounts = this.getBaseMapper().getVisitData(startDateTime, endDateTime);

        // 将 visitCounts 转换为日期为键的映射，便于快速查询
        Map<String, VisitCount> visitCountMap = visitCounts.stream()
                .collect(Collectors.toMap(VisitCount::getDate, vc -> vc, (v1, v2) -> v1));  // 保留第一个值

        // 初始化 pvList、ipList 和 uvList，同时遍历日期范围
        List<Integer> pvList = new ArrayList<>();
        List<Integer> ipList = new ArrayList<>();
        List<Integer> uvList = new ArrayList<>();

        // 一次遍历dates列表，填充PV、IP、UV数据
        dates.forEach(date -> {
            VisitCount visitCount = visitCountMap.getOrDefault(date, new VisitCount());
            pvList.add(visitCount.getPvCount() != null ? visitCount.getPvCount() : 0);
            ipList.add(visitCount.getIpCount() != null ? visitCount.getIpCount() : 0);
            uvList.add(visitCount.getUvCount() != null ? visitCount.getUvCount() : 0);
        });

        // 填充 VO 对象
        visitTrend.setDates(dates);
        visitTrend.setPvList(pvList);
        visitTrend.setIpList(ipList);
        visitTrend.setUvList(uvList);

        return visitTrend;
    }

    /**
     * 开始时间-结束时间日期生成
     */
    private List<String> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        List<String> dates = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate.toString());
            currentDate = currentDate.plusDays(1);
        }
        return dates;
    }
}

