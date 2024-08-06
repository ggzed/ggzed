package com.yf.controller.system;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.annotation.OperationLog;
import com.yf.annotation.PreventDuplicateSubmit;
import com.yf.model.enums.BusinessTypeEnum;
import com.yf.model.form.DictTypeForm;
import com.yf.model.query.DictTypePageQuery;
import com.yf.model.result.PageResult;
import com.yf.model.result.Result;
import com.yf.model.vo.DictTypePageVO;
import com.yf.service.ISysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 字典类型表-SysDictDataController
 *
 * @author: YiFei
 * @since : 2024-04-23 18:52:08
 */
@Tag(name = "字典类型表")
@RestController
@RequestMapping("dict/type")
@RequiredArgsConstructor
public class SysDictTypeController {
    /**
     * 字典类型表-SysDictDataService
     */
    private final ISysDictTypeService dictTypeService;

    @Operation(summary = "查询字典类型")
    @PreventDuplicateSubmit
    @OperationLog(title = "查询字典类型", businessType = BusinessTypeEnum.SEARCH)
    @PreAuthorize("@permission.checker('system:dict-type:list')")
    @GetMapping("/page")
    public PageResult<DictTypePageVO> getDictType(@ParameterObject @Validated DictTypePageQuery queryParams) {
        IPage<DictTypePageVO> result = dictTypeService.getDictType(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "字典类型表单数据")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:dict-type:update')")
    @GetMapping("/{dictTypeId}/form")
    public Result<DictTypeForm> getDictTypeForm(@PathVariable Integer dictTypeId) {
        DictTypeForm dictTypeForm = dictTypeService.getDictTypeForm(dictTypeId);
        return Result.judge(dictTypeForm);
    }

    @Operation(summary = "新增字典类型")
    @OperationLog(title = "新增字典类型", businessType = BusinessTypeEnum.INSERT)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:dict-type:save')")
    @PostMapping
    public Result<Integer> saveDictType(@RequestBody @Validated DictTypeForm dictTypeForm) {
        Integer result = dictTypeService.saveDictType(dictTypeForm);
        return Result.judge(result);
    }

    @Operation(summary = "删除字典类型")
    @OperationLog(title = "删除字典类型", businessType = BusinessTypeEnum.DELETE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:dict-type:delete')")
    @DeleteMapping("/{dictTypeIds}")
    public Result<Void> deleteDictType(@Parameter(description = "ids 以 , 分隔") @PathVariable String dictTypeIds) {
        boolean deleted = dictTypeService.deleteDictType(Arrays.stream(dictTypeIds.split(","))
                .map(Integer::parseInt).collect(Collectors.toList()));
        return Result.judge(deleted);
    }

    @Operation(summary = "修改字典类型")
    @OperationLog(title = "修改字典类型", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:dict-type:update')")
    @PutMapping("/{dictTypeId}")
    public Result<Void> updateDictType(@Parameter(description = "字典类型ID") @PathVariable Integer dictTypeId, @RequestBody @Validated DictTypeForm dictTypeForm) {
        boolean deleted = dictTypeService.updateDictType(dictTypeId, dictTypeForm);
        return Result.judge(deleted);
    }

    @Operation(summary = "修改字典类型状态")
    @OperationLog(title = "修改字典类型状态", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:dict-type:update')")
    @PatchMapping("/{dictTypeId}/status")
    public Result<Void> updateMenuHidden(@Parameter(description = "字典类型ID") @PathVariable Integer dictTypeId,
                                         @Parameter(description = "状态(true:启用;false:禁用)") @RequestParam Boolean status) {
        boolean updated = dictTypeService.updateDictTypeStatus(dictTypeId, status);
        return Result.judge(updated);
    }

}

