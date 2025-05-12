<#assign classNameLower = table.className?substring(0, 1)?lower_case + table.className?substring(1)>
<#switch mapFields.pk[0].javaType>
    <#case "Integer">
        <#assign parseField = "parseInt">
        <#break>
    <#case "Long">
        <#assign parseField = "parseLong">
        <#break>
    <#case "String">
        <#assign parseField = "toString">
        <#break>
    <#default>
        <#assign parseField = "parseUnknown">
</#switch>
<#-- @formatter:off -->
package com.${table.packageName}.${table.moduleName}.${table.businessName}.controller;

import com.${table.packageName}.result.PageResult;
import com.${table.packageName}.result.Result;
import com.${table.packageName}.log.annotation.OperationLog;
import com.${table.packageName}.model.log.enums.BusinessTypeEnum;
import com.${table.packageName}.${table.moduleName}.${table.businessName}.model.form.${table.className}Form;
import com.${table.packageName}.${table.moduleName}.${table.businessName}.model.query.${table.className}PageQuery;
import com.${table.packageName}.${table.moduleName}.${table.businessName}.model.vo.${table.className}PageVO;
import com.${table.packageName}.rate_limiting.annotation.PreventDuplicateSubmit;
import com.${table.packageName}.result.Result;
import com.${table.packageName}.${table.moduleName}.${table.businessName}.service.I${table.className}Service;
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
 * ${table.tableComment}-${table.className}Controller
 *
 * @author: ${table.functionAuthor}
 * @since : ${.now}
 */
@Tag(name = "${table.tableComment}")
@RestController
@RequestMapping("${table.tableName}")
@RequiredArgsConstructor
public class ${table.className}Controller {

    private final I${table.className}Service ${classNameLower}Service;

    @Operation(summary = "查询${table.tableComment}")
    @OperationLog(title = "查询${table.tableComment}", businessType = BusinessTypeEnum.SEARCH)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('${table.moduleName}:${table.businessName}:list')" )
    @GetMapping("/page" )
    public PageResult<${table.className}PageVO> get${table.className}Page(@ParameterObject @Validated ${table.className}PageQuery queryParams) {
        IPage<${table.className}PageVO> result = ${classNameLower}Service.get${table.className}Page(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "${table.tableComment}表单数据")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('${table.moduleName}:${table.businessName}:list')" )
    @GetMapping("/{${mapFields.pk[0].javaTsFieldName}}/form" )
    public Result<${table.className}Form> get${table.className}Form(@PathVariable ${mapFields.pk[0].javaType} ${mapFields.pk[0].javaTsFieldName}) {
        ${table.className}Form ${classNameLower}Form = ${classNameLower}Service.get${table.className}Form(${mapFields.pk[0].javaTsFieldName});
        return Result.judge(${classNameLower}Form);
    }

    @Operation(summary = "新增${table.tableComment}")
    @OperationLog(title = "新增${table.tableComment}", businessType = BusinessTypeEnum.INSERT)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('${table.moduleName}:${table.businessName}:save')" )
    @PostMapping
    public Result<${mapFields.pk[0].javaType}> save${table.className}(@RequestBody @Validated ${table.className}Form ${classNameLower}Form) {
    ${mapFields.pk[0].javaType} result = ${classNameLower}Service.save${table.className}(${classNameLower}Form);
        return Result.judge(result);
    }

    @Operation(summary = "修改${table.tableComment}")
    @OperationLog(title = "修改${table.tableComment}", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('${table.moduleName}:${table.businessName}:update')" )
    @PutMapping("/{${mapFields.pk[0].javaTsFieldName}}" )
    public Result<Void> update${table.className}(@Parameter(description = "${table.tableComment}主键" ) @PathVariable ${mapFields.pk[0].javaType} ${mapFields.pk[0].javaTsFieldName}, @RequestBody @Validated ${table.className}Form ${classNameLower}Form) {
        boolean deleted = ${classNameLower}Service.update${table.className}(${mapFields.pk[0].javaTsFieldName}, ${classNameLower}Form);
        return Result.judge(deleted);
    }

    @Operation(summary = "删除${table.tableComment}")
    @OperationLog(title = "删除${table.tableComment}", businessType = BusinessTypeEnum.DELETE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('${table.moduleName}:${table.businessName}:delete')" )
    @DeleteMapping("/{${mapFields.pk[0].javaTsFieldName}s}" )
    public Result<Void> delete${table.className}(@Parameter(description = "{${mapFields.pk[0].javaTsFieldName}s 以 , 分隔" ) @PathVariable String ${mapFields.pk[0].javaTsFieldName}s) {
        boolean deleted = ${classNameLower}Service.delete${table.className}(Arrays.stream(${mapFields.pk[0].javaTsFieldName}s.split("," ))
                .map(${mapFields.pk[0].javaType}::${parseField}).collect(Collectors.toList()));
        return Result.judge(deleted);
    }

}
<#-- @formatter:on -->