package com.yf.controller.dfms;

import com.yf.result.PageResult;
import com.yf.result.Result;
import com.yf.log.annotation.OperationLog;
import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.dfms.node.model.form.DfmsNodeForm;
import com.yf.dfms.node.model.query.DfmsNodePageQuery;
import com.yf.dfms.node.model.vo.DfmsNodePageVO;
import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
import com.yf.result.Result;
import com.yf.dfms.node.service.IDfmsNodeService;
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
 * 节点信息-DfmsNodeController
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:11
 */
@Tag(name = "节点信息")
@RestController
@RequestMapping("dfms_node")
@RequiredArgsConstructor
public class DfmsNodeController {

    private final IDfmsNodeService dfmsNodeService;

    @Operation(summary = "查询节点信息")
    @OperationLog(title = "查询节点信息", businessType = BusinessTypeEnum.SEARCH)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:node:list')" )
    @GetMapping("/page" )
    public PageResult<DfmsNodePageVO> getDfmsNodePage(@ParameterObject @Validated DfmsNodePageQuery queryParams) {
        IPage<DfmsNodePageVO> result = dfmsNodeService.getDfmsNodePage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "节点信息表单数据")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:node:list')" )
    @GetMapping("/{id}/form" )
    public Result<DfmsNodeForm> getDfmsNodeForm(@PathVariable Integer id) {
        DfmsNodeForm dfmsNodeForm = dfmsNodeService.getDfmsNodeForm(id);
        return Result.judge(dfmsNodeForm);
    }

    @Operation(summary = "新增节点信息")
    @OperationLog(title = "新增节点信息", businessType = BusinessTypeEnum.INSERT)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:node:save')" )
    @PostMapping
    public Result<Integer> saveDfmsNode(@RequestBody @Validated DfmsNodeForm dfmsNodeForm) {
        Integer result = dfmsNodeService.saveDfmsNode(dfmsNodeForm);
        return Result.judge(result);
    }

    @Operation(summary = "修改节点信息")
    @OperationLog(title = "修改节点信息", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:node:update')" )
    @PutMapping("/{id}" )
    public Result<Void> updateDfmsNode(@Parameter(description = "节点信息主键" ) @PathVariable Integer id, @RequestBody @Validated DfmsNodeForm dfmsNodeForm) {
        boolean deleted = dfmsNodeService.updateDfmsNode(id, dfmsNodeForm);
        return Result.judge(deleted);
    }

    @Operation(summary = "删除节点信息")
    @OperationLog(title = "删除节点信息", businessType = BusinessTypeEnum.DELETE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:node:delete')" )
    @DeleteMapping("/{ids}" )
    public Result<Void> deleteDfmsNode(@Parameter(description = "{ids 以 , 分隔" ) @PathVariable String ids) {
        boolean deleted = dfmsNodeService.deleteDfmsNode(Arrays.stream(ids.split("," ))
                .map(Integer::parseInt).collect(Collectors.toList()));
        return Result.judge(deleted);
    }

}
