package com.yf.controller.system;

import com.yf.log.annotation.OperationLog;
import com.yf.model.common.Option;
import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.model.system.form.DeptForm;
import com.yf.model.system.query.DeptPageQuery;
import com.yf.model.vo.DeptPageVo;
import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
import com.yf.result.Result;
import com.yf.service.ISysDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门表-SysDeptController
 *
 * @author: YiFei
 * @since : 2024-04-23 18:43:34
 */
@Tag(name = "部门表")
@RestController
@RequestMapping("dept")
@RequiredArgsConstructor
public class SysDeptController {
    /**
     * 部门表-SysDeptService
     */
    private final ISysDeptService deptService;

    @Operation(summary = "部门下拉列表")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:dept:list')")
    @GetMapping("/options")
    public Result<List<Option<Integer>>> listRoleOptions() {
        List<Option<Integer>> options = deptService.listDeptOptions();
        return Result.success(options);
    }

    @Operation(summary = "查询部门")
    @OperationLog(title = "查询部门", businessType = BusinessTypeEnum.SEARCH)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:dept:list')")
    @GetMapping("/page")
    public Result<List<DeptPageVo>> getDeptPage(@ParameterObject @Validated DeptPageQuery queryParams) {
        List<DeptPageVo> result = deptService.getDeptPage(queryParams);
        return Result.success(result);
    }


    @Operation(summary = "新增部门")
    @OperationLog(title = "新增部门", businessType = BusinessTypeEnum.INSERT)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:dept:save')")
    @PostMapping
    public Result<Integer> saveDept(@RequestBody @Validated DeptForm deptForm) {
        Integer result = deptService.saveDept(deptForm);
        return Result.judge(result);
    }

    @Operation(summary = "修改部门")
    @OperationLog(title = "修改部门", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:dept:update')")
    @PutMapping("/{deptId}")
    public Result<Void> updateDept(@Parameter(description = "部门ID") @PathVariable Integer deptId, @RequestBody @Validated DeptForm deptForm) {
        boolean deleted = deptService.updateDept(deptId, deptForm);
        return Result.judge(deleted);
    }

    @Operation(summary = "删除部门")
    @OperationLog(title = "删除部门", businessType = BusinessTypeEnum.DELETE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:dept:delete')")
    @DeleteMapping("/{deptIds}")
    public Result<Void> deleteMenu(@Parameter(description = "ids 以 , 分隔") @PathVariable String deptIds) {
        boolean deleted = deptService.deleteDept(Arrays.stream(deptIds.split(","))
                .map(Integer::parseInt).collect(Collectors.toList()));
        return Result.judge(deleted);
    }


    @Operation(summary = "部门表单数据")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:dept:list')")
    @GetMapping("/{deptId}/form")
    public Result<DeptForm> getDeptForm(@PathVariable Integer deptId) {
        DeptForm deptForm = deptService.getDeptForm(deptId);
        return Result.judge(deptForm);
    }

    @Operation(summary = "修改部门显示状态")
    @OperationLog(title = "修改部门显示状态", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:dept:update')")
    @PatchMapping("/{deptId}/status")
    public Result<Void> updateDeptStatus(@Parameter(description = "部门ID") @PathVariable String deptId,
                                         @Parameter(description = "状态(true:启用;false:禁用)") @RequestParam Boolean status) {
        boolean updated = deptService.updateDeptStatus(deptId, status);
        return Result.judge(updated);
    }
}

