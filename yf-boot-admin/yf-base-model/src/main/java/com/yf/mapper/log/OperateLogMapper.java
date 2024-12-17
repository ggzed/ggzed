package com.yf.mapper.log;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.model.log.bo.OperationLogBO;
import com.yf.model.log.bo.VisitCount;
import com.yf.model.log.entity.OperateLog;
import com.yf.model.log.query.OperationLogQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作日志-SysOperateLog
 *
 * @author yiFei
 * @since 2024-04-15 20:51:57
 */
public interface OperateLogMapper extends BaseMapper<OperateLog> {
    /**
     * 分页数据查询
     *
     * @param queryParams 分页参数
     * @return 分页数据
     */
    List<OperationLogBO> getOperationLogPage(@Param("queryParams") OperationLogQuery queryParams);

    /**
     * @return 总条数
     */
    long getOperationLogTotal(@Param("queryParams") OperationLogQuery queryParams);

    /**
     * @return 系统访问趋势数据
     */
    List<VisitCount> getVisitData(@Param("startDateTime") String startDateTime, @Param("endDateTime") String endDateTime);
}

