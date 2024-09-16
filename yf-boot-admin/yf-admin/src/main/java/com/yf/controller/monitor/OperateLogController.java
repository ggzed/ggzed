package com.yf.controller.monitor;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.annotation.PreventDuplicateSubmit;
import com.yf.model.query.OperationLogQuery;
import com.yf.model.result.PageResult;
import com.yf.model.result.Result;
import com.yf.model.vo.OperationLogVO;
import com.yf.model.vo.VisitTrendVO;
import com.yf.service.IOperateLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 系统操作日志-SysOperateLogController
 *
 * @author : YiFei
 * @since : 2024-04-15 20:51:57
 */
@Tag(name = "系统操作日志")
@RestController
@RequestMapping("log")
@RequiredArgsConstructor
public class OperateLogController {
    /**
     * 系统操作日志-OperateLogService
     */
    private final IOperateLogService operateLogService;

    @Operation(summary = "查询操作日志")
    @PreAuthorize("@permission.checker('monitor:operation-log:list')")
    @PreventDuplicateSubmit
    @GetMapping("/page")
    public PageResult<OperationLogVO> getOperationLogPage(@ParameterObject @Validated OperationLogQuery queryParams) {
        IPage<OperationLogVO> result = operateLogService.getOperationLogPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "访问趋势")
    @PreventDuplicateSubmit
    @GetMapping("/visit-trend")
    public Result<VisitTrendVO> getVisitTrend(
            @Parameter(description = "开始时间", example = "yyyy-MM-dd")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(description = "结束时间", example = "yyyy-MM-dd")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        VisitTrendVO data = operateLogService.getVisitTrend(startDate, endDate);
        return Result.success(data);
    }

    @Operation(summary = "删除日志信息")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('monitor:operation-log:delete')")
    @DeleteMapping("/{logIds}")
    public Result<Void> deleteOperationLog(@Parameter(description = "ids 以 , 分隔") @PathVariable String logIds) {
        boolean deleted = operateLogService.deleteOperationLog(Arrays.stream(logIds.split(","))
                .map(Long::parseLong).collect(Collectors.toList()));
        return Result.judge(deleted);
    }

}