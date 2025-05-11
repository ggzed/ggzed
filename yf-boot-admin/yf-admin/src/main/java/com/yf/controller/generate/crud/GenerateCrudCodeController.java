package com.yf.controller.generate.crud;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.log.annotation.OperationLog;
import com.yf.model.common.Option;
import com.yf.model.generate.form.GenTableFieldsForm;
import com.yf.model.generate.form.GenTableForm;
import com.yf.model.generate.form.GenTableMenuForm;
import com.yf.model.generate.query.DBTablePageQuery;
import com.yf.model.generate.query.GenCrudTablePageQuery;
import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.model.vo.DBTableVO;
import com.yf.model.vo.GenCrudTableVO;
import com.yf.model.vo.PreviewGenCodeTreeVO;
import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
import com.yf.result.PageResult;
import com.yf.result.Result;
import com.yf.service.IGenTableFieldsService;
import com.yf.service.IGenTableService;
import com.yf.service.IGenerateCrudCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GenTableController
 * 生成步骤 :
 * 1. 查询数据库所有可导入表，导入数据库表
 * 2. 修改生成表数据        （ 按钮 : 修改进入下一步 、 修改后退出 、跳过进入下一步 、 返回上一步 ）
 * 3. 修改生成表字段信息     （ 按钮 : 修改进入下一步 、 修改后退出 、跳过进入下一步 、 返回上一步 ）
 * 4. 预览生成的代码       （ 按钮 : 进入下一步 、 返回上一步 ）
 * 5. 预览生成的代码效果图   （ 按钮 : 进入下一步 、 返回上一步 ）
 * 6. 生成菜单 （ 生成进入下一步 、 跳过进入下一步 、 返回上一步  ， 有新增菜单权限才展示）
 * 7. 生成代码 （ 生成 、返回上一步 ）
 *
 * @author: YiFei
 * @since : 2024-06-14 16:52:44
 */
@Tag(name = "生成表")
@RestController
@RequestMapping("crud/code")
@RequiredArgsConstructor
public class GenerateCrudCodeController {
    /**
     * generateCrudCodeService
     */
    private final IGenerateCrudCodeService generateCrudCodeService;
    private final IGenTableService genTableService;
    private final IGenTableFieldsService genTableFieldsService;

    @Operation(summary = "查询可生成的表")
    @OperationLog(title = "查询可生成的表", businessType = BusinessTypeEnum.SEARCH)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('generate:crud:list')")
    @GetMapping("/page")
    public PageResult<GenCrudTableVO> getGenCrudTablePage(@ParameterObject GenCrudTablePageQuery queryParams) {
        IPage<GenCrudTableVO> result = generateCrudCodeService.getGenCrudTablePage(queryParams);
        return PageResult.success(result);
    }


    @Operation(summary = "查询数据库可导入表")
    @OperationLog(title = "查询数据库可导入表", businessType = BusinessTypeEnum.SEARCH)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('generate:crud:import')")
    @GetMapping("/db/page")
    public PageResult<DBTableVO> getGenCrudTablePage(@ParameterObject DBTablePageQuery queryParams) {
        IPage<DBTableVO> result = genTableService.getDBTablePage(queryParams);
        return PageResult.success(result);
    }


    @Operation(summary = "导入数据库表")
    @OperationLog(title = "导入数据库表", businessType = BusinessTypeEnum.INSERT)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('generate:crud:import')")
    @PostMapping("/import/{dbTableNames}")
    public Result<Integer> importDBToTable(@Parameter(description = "dbTableNames 以 , 分隔") @PathVariable String dbTableNames) {
        generateCrudCodeService.importDBToTable(Arrays.stream(dbTableNames.split(",")).toList());
        return Result.success();
    }


    @Operation(summary = "生成 Crud 代码 Zip 文件")
    @OperationLog(title = "生成 Crud 代码 Zip 文件", isSaveResponseData = false, businessType = BusinessTypeEnum.EXPORT)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('generate:crud:zip')")
    @PostMapping("/generate/{tableId}")
    public ResponseEntity<byte[]> exportCrudCode(@PathVariable Integer tableId) {
        // 生成文件名
        String filename = genTableService.generateFileName(tableId);
        // 生成代码的逻辑
        byte[] fileByte = generateCrudCodeService.generateCrudCodeZip(tableId);

        // 设置响应头，告诉浏览器这是一个文件下载
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8) + ".zip");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);

        // 返回 ZIP 文件
        return ResponseEntity.ok()
                .headers(headers)
                .body(fileByte);
    }


    @Operation(summary = "预览待生成的代码")
    @OperationLog(title = "预览待生成的代码", isSaveResponseData = false, businessType = BusinessTypeEnum.SEARCH)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('generate:crud:preview')")
    @GetMapping("/preview/{tableId}")
    public Result<PreviewGenCodeTreeVO> previewGenCrudCode(@PathVariable Integer tableId) {
        return Result.success(generateCrudCodeService.previewGenCrudCode(tableId));
    }


    @Operation(summary = "预览效果示意图")
    @OperationLog(title = "预览效果示意图", isSaveResponseData = false, businessType = BusinessTypeEnum.SEARCH)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('generate:crud:preview')")
    @GetMapping("/display/{tableId}")
    public Result<String> previewGenCrudDisplay(@PathVariable Integer tableId) {
        String genCrudDisplay = generateCrudCodeService.previewGenCrudDisplay(tableId);
        // 生成代码的逻辑
        return Result.success(genCrudDisplay);
    }

    @Operation(summary = "删除表数据")
    @OperationLog(title = "删除表数据", isSaveResponseData = false, businessType = BusinessTypeEnum.DELETE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('generate:crud:delete')")
    @DeleteMapping("/{tableIds}")
    public Result<Void> deleteGenTable(@Parameter(description = "ids 以 , 分隔") @PathVariable String tableIds) {
        boolean deleted = generateCrudCodeService.deleteGenTable(Arrays.stream(tableIds.split(","))
                .map(Integer::parseInt).collect(Collectors.toList()));
        return Result.judge(deleted);
    }

    @Operation(summary = "查询表表单数据")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('generate:crud:list')")
    @GetMapping("/{tableId}/form")
    public Result<GenTableForm> getGenTableForm(@Parameter(description = "生成表ID") @PathVariable Integer tableId) {
        GenTableForm genTableForm = genTableService.getGenTableForm(tableId);
        return Result.judge(genTableForm);
    }

    @Operation(summary = "查询表表单数据")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('generate:crud:list')")
    @GetMapping("/{tableId}/fields/form")
    public Result<List<GenTableFieldsForm>> getGenTableFieldsForm(@Parameter(description = "生成表ID") @PathVariable Integer tableId) {
        List<GenTableFieldsForm> result = genTableFieldsService.getGenTableFieldsForm(tableId);
        return Result.judge(result);
    }

    @Operation(summary = "修改生成表")
    @OperationLog(title = "修改生成表", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('generate:crud:update')")
    @PutMapping("/{tableId}")
    public Result<Void> updateGenTable(@Parameter(description = "生成表ID") @PathVariable Integer tableId,
                                       @RequestBody @Validated GenTableForm form) {
        boolean updated = genTableService.updateGenTable(tableId, form);
        return Result.judge(updated);
    }

    @Operation(summary = "修改表字段信息")
    @OperationLog(title = "修改表字段信息", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('generate:crud:update')")
    @PutMapping("/{tableId}/fields")
    public Result<Void> updateGenTableFields(@PathVariable Integer tableId,
                                             @RequestBody @Validated List<GenTableFieldsForm> forms) {
        boolean updated = genTableService.genTableIsExist(tableId);
        if (updated) {
            genTableFieldsService.updateGenTableFields(forms);
        }
        return Result.judge(updated);
    }

    @Operation(summary = "Crud 新增菜单")
    @OperationLog(title = "Crud 新增菜单", businessType = BusinessTypeEnum.INSERT)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('generate:crud:menu')")
    @PostMapping("/{tableId}/menu")
    public Result<Void> addGenTableMenu(@PathVariable Integer tableId,
                                        @RequestBody @Validated GenTableMenuForm form) {
        boolean added = generateCrudCodeService.addGenTableMenu(tableId, form);
        return Result.judge(added);
    }

    @Operation(summary = "Crud 新增菜单目录下拉列表")
    @PreAuthorize("@permission.checker('generate:crud:menu')")
    @GetMapping("/menu/options")
    public Result<List<Option<Integer>>> listMenuOptions() {
        List<Option<Integer>> options = generateCrudCodeService.listMenuOptions();
        return Result.success(options);
    }


}

