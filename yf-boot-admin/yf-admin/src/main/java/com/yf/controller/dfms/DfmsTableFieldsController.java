package com.yf.controller.dfms;

import com.yf.result.PageResult;
import com.yf.result.Result;
import com.yf.log.annotation.OperationLog;
import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.dfms.tablefields.model.form.DfmsTableFieldsForm;
import com.yf.dfms.tablefields.model.query.DfmsTableFieldsPageQuery;
import com.yf.dfms.tablefields.model.vo.DfmsTableFieldsPageVO;
import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
import com.yf.result.Result;
import com.yf.dfms.tablefields.service.IDfmsTableFieldsService;
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
 * 数据表字段信息-DfmsTableFieldsController
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:51
 */
@Tag(name = "数据表字段信息")
@RestController
@RequestMapping("dfms_table_fields")
@RequiredArgsConstructor
public class DfmsTableFieldsController {

    private final IDfmsTableFieldsService dfmsTableFieldsService;

    @Operation(summary = "查询数据表字段信息")
    @OperationLog(title = "查询数据表字段信息", businessType = BusinessTypeEnum.SEARCH)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:tablefields:list')" )
    @GetMapping("/page" )
    public PageResult<DfmsTableFieldsPageVO> getDfmsTableFieldsPage(@ParameterObject @Validated DfmsTableFieldsPageQuery queryParams) {
        IPage<DfmsTableFieldsPageVO> result = dfmsTableFieldsService.getDfmsTableFieldsPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "数据表字段信息表单数据")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:tablefields:list')" )
    @GetMapping("/{id}/form" )
    public Result<DfmsTableFieldsForm> getDfmsTableFieldsForm(@PathVariable Integer id) {
        DfmsTableFieldsForm dfmsTableFieldsForm = dfmsTableFieldsService.getDfmsTableFieldsForm(id);
        return Result.judge(dfmsTableFieldsForm);
    }

    @Operation(summary = "新增数据表字段信息")
    @OperationLog(title = "新增数据表字段信息", businessType = BusinessTypeEnum.INSERT)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:tablefields:save')" )
    @PostMapping
    public Result<Integer> saveDfmsTableFields(@RequestBody @Validated DfmsTableFieldsForm dfmsTableFieldsForm) {
        Integer result = dfmsTableFieldsService.saveDfmsTableFields(dfmsTableFieldsForm);
        return Result.judge(result);
    }

    @Operation(summary = "修改数据表字段信息")
    @OperationLog(title = "修改数据表字段信息", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:tablefields:update')" )
    @PutMapping("/{id}" )
    public Result<Void> updateDfmsTableFields(@Parameter(description = "数据表字段信息主键" ) @PathVariable Integer id, @RequestBody @Validated DfmsTableFieldsForm dfmsTableFieldsForm) {
        boolean deleted = dfmsTableFieldsService.updateDfmsTableFields(id, dfmsTableFieldsForm);
        return Result.judge(deleted);
    }

    @Operation(summary = "删除数据表字段信息")
    @OperationLog(title = "删除数据表字段信息", businessType = BusinessTypeEnum.DELETE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:tablefields:delete')" )
    @DeleteMapping("/{ids}" )
    public Result<Void> deleteDfmsTableFields(@Parameter(description = "{ids 以 , 分隔" ) @PathVariable String ids) {
        boolean deleted = dfmsTableFieldsService.deleteDfmsTableFields(Arrays.stream(ids.split("," ))
                .map(Integer::parseInt).collect(Collectors.toList()));
        return Result.judge(deleted);
    }

}
