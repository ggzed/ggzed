package com.yf.controller.system;

import com.yf.log.annotation.OperationLog;
import com.yf.model.common.Option;
import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.model.system.form.MenuForm;
import com.yf.model.system.query.MenuPageQuery;
import com.yf.model.vo.MenuPageVO;
import com.yf.model.vo.RouteVO;
import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
import com.yf.result.Result;
import com.yf.service.ISysMenuService;
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
 * 系统菜单-SysMenuController
 *
 * @author: YiFei
 * @since : 2024-04-23 18:43:35
 */
@Tag(name = "系统菜单")
@RestController
@RequestMapping("menu")
@RequiredArgsConstructor
public class SysMenuController {
    /**
     * 系统菜单-SysMenuService
     */
    private final ISysMenuService menuService;

    @Operation(summary = "查询菜单")
    @OperationLog(title = "查询菜单", businessType = BusinessTypeEnum.SEARCH)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:menu:list')")
    @GetMapping("/page")
    public Result<List<MenuPageVO>> getMenuPage(@ParameterObject @Validated MenuPageQuery queryParams) {
        List<MenuPageVO> result = menuService.getMenuPage(queryParams);
        return Result.success(result);
    }

    @Operation(summary = "菜单表单数据")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:user:list')")
    @GetMapping("/{menuId}/form")
    public Result<MenuForm> getMenuForm(@PathVariable Integer menuId) {
        MenuForm menuForm = menuService.getMenuForm(menuId);
        return Result.judge(menuForm);
    }

    @Operation(summary = "新增菜单")
    @OperationLog(title = "新增菜单", businessType = BusinessTypeEnum.INSERT)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:menu:save')")
    @PostMapping
    public Result<Integer> saveMenu(@RequestBody @Validated MenuForm menuForm) {
        Integer result = menuService.saveMenu(menuForm);
        return Result.judge(result);
    }

    @Operation(summary = "删除菜单")
    @OperationLog(title = "删除菜单", businessType = BusinessTypeEnum.DELETE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:menu:delete')")
    @DeleteMapping("/{menuIds}")
    public Result<Void> deleteMenu(@Parameter(description = "ids 以 , 分隔") @PathVariable String menuIds) {
        boolean deleted = menuService.deleteMenu(Arrays.stream(menuIds.split(","))
                .map(Integer::parseInt).collect(Collectors.toList()));
        return Result.judge(deleted);
    }

    @Operation(summary = "修改菜单")
    @OperationLog(title = "修改菜单", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:menu:update')")
    @PutMapping("/{menuId}")
    public Result<Void> updateMenu(@Parameter(description = "菜单ID") @PathVariable Integer menuId, @RequestBody @Validated MenuForm menuForm) {
        boolean deleted = menuService.updateMenu(menuId, menuForm);
        return Result.judge(deleted);
    }


    @Operation(summary = "修改菜单显示状态")
    @OperationLog(title = "修改菜单显示状态", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:menu:update')")
    @PatchMapping("/{menuId}/hidden")
    public Result<Void> updateMenuHidden(@Parameter(description = "菜单ID") @PathVariable Integer menuId,
                                         @Parameter(description = "状态(true:启用;false:禁用)") @RequestParam Boolean hidden) {
        boolean updated = menuService.updateMenuHidden(menuId, hidden);
        return Result.judge(updated);
    }


    @Operation(summary = "路由列表")
    @GetMapping("/routes")
    @PreventDuplicateSubmit
    public Result<List<RouteVO>> listRoutes() {
        List<RouteVO> routeList = menuService.listRoutes();
        return Result.success(routeList);
    }

    @Operation(summary = "菜单下拉列表")
    @PreAuthorize("@permission.checker('system:menu:list')")
    @GetMapping("/options")
    public Result<List<Option<Integer>>> listMenuOptions() {
        List<Option<Integer>> options = menuService.listMenuOptions();
        return Result.success(options);
    }

}

