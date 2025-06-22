package com.yf.controller.dfms;

import com.yf.result.PageResult;
import com.yf.result.Result;
import com.yf.log.annotation.OperationLog;
import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.dfms.table.model.form.DfmsTableForm;
import com.yf.dfms.table.model.query.DfmsTablePageQuery;
import com.yf.dfms.table.model.vo.DfmsTablePageVO;
import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
import com.yf.result.Result;
import com.yf.dfms.table.service.IDfmsTableService;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
 * 数据表信息-DfmsTableController
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:49
 */
@Tag(name = "数据表信息")
@RestController
@RequestMapping("dfms_table")
@RequiredArgsConstructor
public class DfmsTableController {

    private final IDfmsTableService dfmsTableService;

    @Operation(summary = "查询数据表信息")
    @OperationLog(title = "查询数据表信息", businessType = BusinessTypeEnum.SEARCH)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:table:list')" )
    @GetMapping("/page" )
    public PageResult<DfmsTablePageVO> getDfmsTablePage(@ParameterObject @Validated DfmsTablePageQuery queryParams) {
        IPage<DfmsTablePageVO> result = dfmsTableService.getDfmsTablePage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "数据表信息表单数据")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:table:list')" )
    @GetMapping("/{id}/form" )
    public Result<DfmsTableForm> getDfmsTableForm(@PathVariable Integer id) {
        DfmsTableForm dfmsTableForm = dfmsTableService.getDfmsTableForm(id);
        return Result.judge(dfmsTableForm);
    }

    @Operation(summary = "新增数据表信息")
    @OperationLog(title = "新增数据表信息", businessType = BusinessTypeEnum.INSERT)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:table:save')" )
    @PostMapping
    public Result<Integer> saveDfmsTable(@RequestBody @Validated DfmsTableForm dfmsTableForm) {
        Integer result = dfmsTableService.saveDfmsTable(dfmsTableForm);
        return Result.judge(result);
    }

    @Operation(summary = "修改数据表信息")
    @OperationLog(title = "修改数据表信息", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:table:update')" )
    @PutMapping("/{id}" )
    public Result<Void> updateDfmsTable(@Parameter(description = "数据表信息主键" ) @PathVariable Integer id, @RequestBody @Validated DfmsTableForm dfmsTableForm) {
        boolean deleted = dfmsTableService.updateDfmsTable(id, dfmsTableForm);
        return Result.judge(deleted);
    }

    @Operation(summary = "删除数据表信息")
    @OperationLog(title = "删除数据表信息", businessType = BusinessTypeEnum.DELETE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:table:delete')" )
    @DeleteMapping("/{ids}" )
    public Result<Void> deleteDfmsTable(@Parameter(description = "{ids 以 , 分隔" ) @PathVariable String ids) {
        boolean deleted = dfmsTableService.deleteDfmsTable(Arrays.stream(ids.split("," ))
                .map(Integer::parseInt).collect(Collectors.toList()));
        return Result.judge(deleted);
    }

}
