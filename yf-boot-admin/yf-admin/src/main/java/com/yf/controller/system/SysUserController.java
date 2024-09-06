package com.yf.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.annotation.OperationLog;
import com.yf.annotation.PreventDuplicateSubmit;
import com.yf.model.enums.BusinessTypeEnum;
import com.yf.model.form.UserForm;
import com.yf.model.query.UserPageQuery;
import com.yf.model.result.PageResult;
import com.yf.model.result.Result;
import com.yf.model.vo.UserInfoVO;
import com.yf.model.vo.UserPageVO;
import com.yf.service.ISysUserService;
import com.yf.utils.SecurityUtil;
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
 * 用户信息表-SysUserController
 *
 * @author : YiFei
 * @since : 2024-04-18 16:59:58
 */
@Tag(name = "用户信息表")
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class SysUserController {
    /**
     * 用户信息表-SysUserService
     */
    private final ISysUserService userService;

    @Operation(summary = "获取当前登录用户信息")
    @PreventDuplicateSubmit
    @GetMapping("/me")
    public Result<UserInfoVO> getCurrentUserInfo() {
        UserInfoVO userInfoVO = userService.getCurrentUserInfo(SecurityUtil.getUserId());
        return Result.success(userInfoVO);
    }

    @Operation(summary = "查询用户")
    @OperationLog(title = "查询用户", businessType = BusinessTypeEnum.SEARCH)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:user:list')")
    @GetMapping("/page")
    public PageResult<UserPageVO> getUserPage(@ParameterObject @Validated UserPageQuery queryParams) {
        IPage<UserPageVO> result = userService.getUserPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "用户表单数据")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:user:update')")
    @GetMapping("/{userId}/form")
    public Result<UserForm> getUserForm(@PathVariable Long userId) {
        UserForm userForm = userService.getUserForm(userId);
        return Result.judge(userForm);
    }

    @Operation(summary = "新增用户")
    @OperationLog(title = "新增用户", businessType = BusinessTypeEnum.INSERT,
            excludeParamNames = {"phoneNumber", "email"})
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:user:save')")
    @PostMapping
    public Result<Long> saveUser(@RequestBody @Validated UserForm userForm) {
        Long saveId = userService.saveUser(userForm);
        return Result.judge(saveId);
    }

    @Operation(summary = "删除用户")
    @OperationLog(title = "删除用户", businessType = BusinessTypeEnum.DELETE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:user:delete')")
    @DeleteMapping("/{userIds}")
    public Result<Void> deleteUser(@Parameter(description = "用户ID，多个以英文逗号(,)分割")
                                   @PathVariable String userIds) {
        boolean deleted = userService.deleteUser(Arrays.stream(userIds.split(","))
                .map(Long::parseLong).collect(Collectors.toList()));
        return Result.judge(deleted);
    }

    @Operation(summary = "修改用户")
    @OperationLog(title = "修改用户", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:user:update')")
    @PutMapping("/{userId}")
    public Result<Void> updateUser(@Parameter(description = "用户ID") @PathVariable Long userId,
                                   @RequestBody @Validated UserForm userForm) {
        boolean updated = userService.updateUser(userId, userForm);
        return Result.judge(updated);
    }

    @Operation(summary = "修改用户状态")
    @OperationLog(title = "修改用户状态", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:user:update')")
    @PatchMapping("/{userId}/status")
    public Result<Void> updateUserStatus(@Parameter(description = "用户ID") @PathVariable Long userId,
                                         @Parameter(description = "状态(true:启用;false:禁用)") @RequestParam Boolean status) {
        boolean updated = userService.updateUserStatus(userId, status);
        return Result.judge(updated);
    }

    @Operation(summary = "重置用户密码（管理员）")
    @OperationLog(title = "重置用户密码", businessType = BusinessTypeEnum.UPDATE, excludeParamNames = {"password"})
    @PreventDuplicateSubmit
    @PatchMapping("/{userId}/reset-password")
    public Result<Void> resetUserPassword(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "密码") @RequestParam String password
    ) {
        boolean updated = userService.resetUserPassword(userId, password);
        return Result.judge(updated);
    }
}

