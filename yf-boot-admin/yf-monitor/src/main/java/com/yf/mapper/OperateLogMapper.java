package com.yf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.model.entity.OperateLog;
import com.yf.model.query.OperationLogQuery;
import com.yf.model.vo.OperationLogVO;
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
    List<OperationLogVO> getOperationLogPage(@Param("queryParams") OperationLogQuery queryParams);

    /**
     * @return 总条数
     */
    long getOperationLogTotal(@Param("queryParams") OperationLogQuery queryParams);
}

