package com.yf.controller.system;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.log.annotation.OperationLog;
import com.yf.model.common.Option;
import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.model.system.form.DictDataForm;
import com.yf.model.system.query.DictDataPageQuery;
import com.yf.model.vo.DictDataPageVO;
import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
import com.yf.rate_limiting.annotation.RateLimiter;
import com.yf.rate_limiting.annotation.RateLimiters;
import com.yf.rate_limiting.annotation.RateRule;
import com.yf.rate_limiting.model.enums.LimitTypeEnum;
import com.yf.result.PageResult;
import com.yf.result.Result;
import com.yf.service.ISysDictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典数据表-SysDictDataController
 *
 * @author: YiFei
 * @since : 2024-04-23 18:52:08
 */
@Tag(name = "字典数据表")
@RestController
@RequestMapping("dict/data")
@RequiredArgsConstructor
public class SysDictDataController {
    /**
     * 字典数据表-SysDictDataService
     */
    private final ISysDictDataService dictDataService;

    @Operation(summary = "字典下拉列表")
    @RateLimiters(rateLimiters = {
            @RateLimiter(
                    limitTypeEnum = LimitTypeEnum.IP,
                    rateRules = @RateRule(limit = 30)
            )
    })
    @GetMapping("/{types}/options")
    public Result<Map<String, List<Option<String>>>> listDictOptions(@Parameter(description = "字典类型以 , 隔开") @PathVariable String types) {
        Map<String, List<Option<String>>> result = dictDataService.listDictOptions(Arrays.stream(types.split(",")).toList());
        return Result.success(result);
    }


    @Operation(summary = "查询字典数据详情")
    @PreventDuplicateSubmit
    @OperationLog(title = "查询字典数据详情", businessType = BusinessTypeEnum.SEARCH)
    @PreAuthorize("@permission.checker('system:dict-data:list')")
    @GetMapping("/{dictTypeId}/page")
    public PageResult<DictDataPageVO> getDictData(@PathVariable Integer dictTypeId, @ParameterObject @Validated DictDataPageQuery queryParams) {
        IPage<DictDataPageVO> result = dictDataService.getDictData(dictTypeId, queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "字典数据表单数据")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:dict-data:list')")
    @GetMapping("/{dictDataId}/form")
    public Result<DictDataForm> getDictDataForm(@PathVariable Integer dictDataId) {
        DictDataForm dictDataForm = dictDataService.getDictDataForm(dictDataId);
        return Result.judge(dictDataForm);
    }

    @Operation(summary = "新增字典数据")
    @OperationLog(title = "新增字典数据", businessType = BusinessTypeEnum.INSERT)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:dict-data:save')")
    @PostMapping
    public Result<Integer> saveDictData(@RequestBody @Validated DictDataForm dictDataForm) {
        Integer result = dictDataService.saveDictData(dictDataForm);
        return Result.judge(result);
    }

    @Operation(summary = "删除字典数据")
    @OperationLog(title = "删除字典数据", businessType = BusinessTypeEnum.DELETE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:dict-data:delete')")
    @DeleteMapping("/{dictDataIds}")
    public Result<Void> deleteDictData(@Parameter(description = "ids 以 , 分隔") @PathVariable String dictDataIds) {
        boolean deleted = dictDataService.deleteDictData(Arrays.stream(dictDataIds.split(","))
                .map(Integer::parseInt).collect(Collectors.toList()));
        return Result.judge(deleted);
    }

    @Operation(summary = "修改字典数据")
    @OperationLog(title = "修改字典数据", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:dict-data:update')")
    @PutMapping("/{dictDataId}")
    public Result<Void> updateDictData(@Parameter(description = "字典数据ID") @PathVariable Integer dictDataId, @RequestBody @Validated DictDataForm dictDataForm) {
        boolean deleted = dictDataService.updateDictData(dictDataId, dictDataForm);
        return Result.judge(deleted);
    }

    @Operation(summary = "修改字典数据状态")
    @OperationLog(title = "修改字典数据状态", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:dict-data:update')")
    @PatchMapping("/{dictDataId}/status")
    public Result<Void> updateMenuHidden(@Parameter(description = "字典数据ID") @PathVariable Integer dictDataId,
                                         @Parameter(description = "状态(true:启用;false:禁用)") @RequestParam Boolean status) {
        boolean updated = dictDataService.updateDictDataStatus(dictDataId, status);
        return Result.judge(updated);
    }

}

