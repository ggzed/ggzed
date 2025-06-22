package com.yf.controller.dfms;

import com.yf.result.PageResult;
import com.yf.result.Result;
import com.yf.log.annotation.OperationLog;
import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.dfms.tableindex.model.form.DfmsTableIndexForm;
import com.yf.dfms.tableindex.model.query.DfmsTableIndexPageQuery;
import com.yf.dfms.tableindex.model.vo.DfmsTableIndexPageVO;
import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
import com.yf.result.Result;
import com.yf.dfms.tableindex.service.IDfmsTableIndexService;
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
 * 数据表索引信息-DfmsTableIndexController
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:54
 */
@Tag(name = "数据表索引信息")
@RestController
@RequestMapping("dfms_table_index")
@RequiredArgsConstructor
public class DfmsTableIndexController {

    private final IDfmsTableIndexService dfmsTableIndexService;

    @Operation(summary = "查询数据表索引信息")
    @OperationLog(title = "查询数据表索引信息", businessType = BusinessTypeEnum.SEARCH)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:tableindex:list')" )
    @GetMapping("/page" )
    public PageResult<DfmsTableIndexPageVO> getDfmsTableIndexPage(@ParameterObject @Validated DfmsTableIndexPageQuery queryParams) {
        IPage<DfmsTableIndexPageVO> result = dfmsTableIndexService.getDfmsTableIndexPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "数据表索引信息表单数据")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:tableindex:list')" )
    @GetMapping("/{id}/form" )
    public Result<DfmsTableIndexForm> getDfmsTableIndexForm(@PathVariable Integer id) {
        DfmsTableIndexForm dfmsTableIndexForm = dfmsTableIndexService.getDfmsTableIndexForm(id);
        return Result.judge(dfmsTableIndexForm);
    }

    @Operation(summary = "新增数据表索引信息")
    @OperationLog(title = "新增数据表索引信息", businessType = BusinessTypeEnum.INSERT)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:tableindex:save')" )
    @PostMapping
    public Result<Integer> saveDfmsTableIndex(@RequestBody @Validated DfmsTableIndexForm dfmsTableIndexForm) {
        Integer result = dfmsTableIndexService.saveDfmsTableIndex(dfmsTableIndexForm);
        return Result.judge(result);
    }

    @Operation(summary = "修改数据表索引信息")
    @OperationLog(title = "修改数据表索引信息", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:tableindex:update')" )
    @PutMapping("/{id}" )
    public Result<Void> updateDfmsTableIndex(@Parameter(description = "数据表索引信息主键" ) @PathVariable Integer id, @RequestBody @Validated DfmsTableIndexForm dfmsTableIndexForm) {
        boolean deleted = dfmsTableIndexService.updateDfmsTableIndex(id, dfmsTableIndexForm);
        return Result.judge(deleted);
    }

    @Operation(summary = "删除数据表索引信息")
    @OperationLog(title = "删除数据表索引信息", businessType = BusinessTypeEnum.DELETE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:tableindex:delete')" )
    @DeleteMapping("/{ids}" )
    public Result<Void> deleteDfmsTableIndex(@Parameter(description = "{ids 以 , 分隔" ) @PathVariable String ids) {
        boolean deleted = dfmsTableIndexService.deleteDfmsTableIndex(Arrays.stream(ids.split("," ))
                .map(Integer::parseInt).collect(Collectors.toList()));
        return Result.judge(deleted);
    }

}
