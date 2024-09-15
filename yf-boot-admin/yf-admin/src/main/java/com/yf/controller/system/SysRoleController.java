package com.yf.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.annotation.OperationLog;
import com.yf.annotation.PreventDuplicateSubmit;
import com.yf.model.Option;
import com.yf.model.enums.BusinessTypeEnum;
import com.yf.model.form.RoleForm;
import com.yf.model.query.RolePageQuery;
import com.yf.model.result.PageResult;
import com.yf.model.result.Result;
import com.yf.model.vo.RolePageVO;
import com.yf.service.ISysRoleService;
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
 * 角色表-SysRoleController
 *
 * @author: YiFei
 * @since : 2024-04-23 18:43:36
 */
@Tag(name = "角色表")
@RestController
@RequestMapping("role")
@RequiredArgsConstructor
public class SysRoleController {
    /**
     * 角色表-SysRoleService
     */
    private final ISysRoleService roleService;

    @Operation(summary = "查询角色")
    @OperationLog(title = "查询角色", businessType = BusinessTypeEnum.SEARCH)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:role:list')")
    @GetMapping("/page")
    public PageResult<RolePageVO> getRolePage(@ParameterObject RolePageQuery queryParams) {
        IPage<RolePageVO> result = roleService.getRolePage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "角色表单数据")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:role:list')")
    @GetMapping("/{roleId}/form")
    public Result<RoleForm> getRoleForm(@PathVariable Integer roleId) {
        RoleForm roleForm = roleService.getRoleForm(roleId);
        return Result.judge(roleForm);
    }

    @Operation(summary = "增加角色")
    @OperationLog(title = "增加角色", businessType = BusinessTypeEnum.INSERT)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:role:save')")
    @PostMapping
    public Result<Integer> saveRole(@RequestBody @Validated RoleForm roleForm) {
        Integer result = roleService.saveRole(roleForm);
        return Result.judge(result);
    }

    @Operation(summary = "删除角色")
    @OperationLog(title = "删除角色", businessType = BusinessTypeEnum.DELETE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:role:delete')")
    @DeleteMapping("/{roleIds}")
    public Result<Void> deleteRole(@Parameter(description = "ids 以 , 分隔") @PathVariable String roleIds) {
        boolean deleted = roleService.deleteRole(Arrays.stream(roleIds.split(","))
                .map(Integer::parseInt).collect(Collectors.toList()));
        return Result.judge(deleted);
    }

    @Operation(summary = "修改角色")
    @OperationLog(title = "修改角色", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:role:update')")
    @PutMapping("/{roleId}")
    public Result<Void> updateRole(@Parameter(description = "用户ID") @PathVariable Integer roleId,
                                   @RequestBody @Validated RoleForm roleForm) {
        boolean updated = roleService.updateRole(roleId, roleForm);
        return Result.judge(updated);
    }

    @Operation(summary = "修改角色状态")
    @OperationLog(title = "修改角色状态", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:role:update')")
    @PatchMapping("/{roleId}/status")
    public Result<Void> updateRoleStatus(@Parameter(description = "用户ID") @PathVariable Integer roleId,
                                         @Parameter(description = "状态(true:启用;false:禁用)") @RequestParam Boolean status) {
        boolean updated = roleService.updateRoleStatus(roleId, status);
        return Result.judge(updated);
    }

    @Operation(summary = "获取角色的菜单ID集合")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:role:list')")
    @GetMapping("/{roleId}/menuIds")
    public Result<List<Integer>> getRoleMenuIds(
            @Parameter(description = "角色ID") @PathVariable Integer roleId
    ) {
        List<Integer> menuIds = roleService.getRoleMenuIds(roleId);
        return Result.success(menuIds);
    }

    @Operation(summary = "角色下拉列表")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:role:list')")
    @GetMapping("/options")
    public Result<List<Option<Integer>>> listRoleOptions() {
        List<Option<Integer>> options = roleService.listRoleOptions();
        return Result.success(options);
    }

    @Operation(summary = "权限分配")
    @OperationLog(title = "权限分配", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:role:permission')")
    @PutMapping("/{roleId}/menus")
    public Result<Void> updateRoleMenus(
            @PathVariable Integer roleId, @RequestBody List<Integer> menuIds
    ) {
        boolean result = roleService.updateRoleMenus(roleId, menuIds);
        return Result.judge(result);
    }

}

