package com.yf.controller.dfms;

import com.yf.dfms.db.model.vo.DfmsDbCardVO;
import com.yf.dfms.db.model.vo.DfmsDbOverviewVO;
import com.yf.result.PageResult;
import com.yf.result.Result;
import com.yf.log.annotation.OperationLog;
import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.dfms.db.model.form.DfmsDbForm;
import com.yf.dfms.db.model.query.DfmsDbPageQuery;
import com.yf.dfms.db.model.vo.DfmsDbPageVO;
import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
import com.yf.result.Result;
import com.yf.dfms.db.service.IDfmsDbService;
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
 * 数据库信息-DfmsDbController
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:42
 */
@Tag(name = "数据库信息")
@RestController
@RequestMapping("dfms_db")
@RequiredArgsConstructor
public class DfmsDbController {

    private final IDfmsDbService dfmsDbService;

    @Operation(summary = "数据库总览信息")
    @OperationLog(title = "数据库总览信息", businessType = BusinessTypeEnum.SEARCH)
    @PreventDuplicateSubmit
//    @PreAuthorize("@permission.checker('dfms:db:list')" )
    @GetMapping("/overview" )
    public Result<List<DfmsDbCardVO>> getOverview() {
        List<DfmsDbCardVO> result = dfmsDbService.getOverview();
        return Result.success(result);
    }

    @Operation(summary = "查询数据库信息")
    @OperationLog(title = "查询数据库信息", businessType = BusinessTypeEnum.SEARCH)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:db:list')" )
    @GetMapping("/page" )
    public PageResult<DfmsDbPageVO> getDfmsDbPage(@ParameterObject @Validated DfmsDbPageQuery queryParams) {
        IPage<DfmsDbPageVO> result = dfmsDbService.getDfmsDbPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "数据库信息表单数据")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:db:list')" )
    @GetMapping("/{id}/form" )
    public Result<DfmsDbForm> getDfmsDbForm(@PathVariable Integer id) {
        DfmsDbForm dfmsDbForm = dfmsDbService.getDfmsDbForm(id);
        return Result.judge(dfmsDbForm);
    }

    @Operation(summary = "新增数据库信息")
    @OperationLog(title = "新增数据库信息", businessType = BusinessTypeEnum.INSERT)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:db:save')" )
    @PostMapping
    public Result<Integer> saveDfmsDb(@RequestBody @Validated DfmsDbForm dfmsDbForm) {
        Integer result = dfmsDbService.saveDfmsDb(dfmsDbForm);
        return Result.judge(result);
    }

    @Operation(summary = "修改数据库信息")
    @OperationLog(title = "修改数据库信息", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:db:update')" )
    @PutMapping("/{id}" )
    public Result<Void> updateDfmsDb(@Parameter(description = "数据库信息主键" ) @PathVariable Integer id, @RequestBody @Validated DfmsDbForm dfmsDbForm) {
        boolean deleted = dfmsDbService.updateDfmsDb(id, dfmsDbForm);
        return Result.judge(deleted);
    }

    @Operation(summary = "删除数据库信息")
    @OperationLog(title = "删除数据库信息", businessType = BusinessTypeEnum.DELETE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('dfms:db:delete')" )
    @DeleteMapping("/{ids}" )
    public Result<Void> deleteDfmsDb(@Parameter(description = "{ids 以 , 分隔" ) @PathVariable String ids) {
        boolean deleted = dfmsDbService.deleteDfmsDb(Arrays.stream(ids.split("," ))
                .map(Integer::parseInt).collect(Collectors.toList()));
        return Result.judge(deleted);
    }

}
