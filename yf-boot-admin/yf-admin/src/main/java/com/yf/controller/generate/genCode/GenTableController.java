package com.yf.controller.generate.genCode;

import com.yf.annotation.OperationLog;
import com.yf.annotation.PreventDuplicateSubmit;
import com.yf.model.enums.BusinessTypeEnum;
import com.yf.model.result.Result;
import com.yf.service.IGenTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * GenTableController
 *
 * @author: YiFei
 * @since : 2024-06-14 16:52:44
 */
@Tag(name = "生成表")
@RestController
@RequestMapping("gen/table")
@RequiredArgsConstructor
public class GenTableController {
    /**
     * GenTableService
     */
    private final IGenTableService genTableService;

    @Operation(summary = "同步数据库")
    @OperationLog(title = "同步数据库", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PostMapping("/syncDatabase")
    public Result<Void> syncDatabase() {
        boolean result = genTableService.syncDatabase();
        return Result.judge(result);
    }

    @Operation(summary = "分页查询table")
    @GetMapping("/tables")
    public Result<Void> getTablesPaginated() {
        return Result.success();
    }

    @Operation(summary = "获取单个table详情")
    @GetMapping("/tables/{id}")
    public Result<Void> getTableById(@PathVariable Long id) {
        return Result.success();
    }

    @Operation(summary = "修改table")
    @PutMapping("/tables/{id}")
    public Result<Void> updateTable(@PathVariable Long id) {
        return Result.success();
    }

    @Operation(summary = "保存table")
    @PostMapping("/tables")
    public Result<Void> saveTable() {
        return Result.success();
    }

    @Operation(summary = "删除table")
    @DeleteMapping("/tables/{id}")
    public Result<Void> deleteTable(@PathVariable Long id) {
        return Result.success();
    }

}

