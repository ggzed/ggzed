package com.yf.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.mapper.OperateLogMapper;
import com.yf.model.entity.OperateLog;
import com.yf.model.query.OperationLogQuery;
import com.yf.model.vo.OperationLogVO;
import com.yf.service.IOperateLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志-SysOperateLogServiceImpl
 *
 * @author YiFei
 * @since 2024-04-15 20:51:58
 */
@Service("operateLogService")
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements IOperateLogService {

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
        page.setTotal(this.baseMapper.getOperationLogTotal(queryParams));
        page.setRecords(this.baseMapper.getOperationLogPage(queryParams));
        // 2. 分页查询
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
}

