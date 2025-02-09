package com.yf.controller.system;

import com.yf.log.annotation.OperationLog;
import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.model.system.form.ResetUserPasswordForm;
import com.yf.model.system.form.UserProfileForm;
import com.yf.model.vo.UserProfileInfoVO;
import com.yf.oss.constraints.MultipartFileValid;
import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
import com.yf.rate_limiting.annotation.RateLimiter;
import com.yf.rate_limiting.annotation.RateLimiters;
import com.yf.rate_limiting.annotation.RateRule;
import com.yf.result.Result;
import com.yf.result.ResultCode;
import com.yf.service.ISysUserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.model.AuthCallback;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

/**
 * 个人中心
 *
 * @author : YiFei
 * @since : 2024/7/22 16:19
 */
@Tag(name = "用户信息表")
@RestController
@RequestMapping("user/profile")
@RequiredArgsConstructor
public class SysProfileController {

    private final ISysUserProfileService userProfileService;

    @Operation(summary = "获取个人信息")
    @PreventDuplicateSubmit
    @GetMapping
    public Result<UserProfileInfoVO> getUserProfileInfo() {
        UserProfileInfoVO userProfileInfo = userProfileService.getUserProfileInfo();
        return Result.success(userProfileInfo);
    }

    @Operation(summary = "检查密码是否存在")
    @PreventDuplicateSubmit
    @GetMapping("/password/existence")
    public Result<Boolean> checkPasswordExistence() {
        boolean result = userProfileService.checkPasswordExistence();
        return Result.success(result);
    }

    @Operation(summary = "修改用户名")
    @RateLimiters(
            rateLimiters = {
                    @RateLimiter(rateRules = {
                            @RateRule(limit = 1, timeUnit = TimeUnit.DAYS, timeDuration = 7)
                    }, message = ResultCode.USER_UPDATE_USERNAME_TIMES)
            }
    )
    @PatchMapping("/username")
    public Result<Void> updateUsername(@RequestParam
                                       @Length(min = 4, max = 24, message = "用户名长度为4-24位")
                                       String username) {
        boolean result = userProfileService.updateUsername(username);
        return Result.judge(result);
    }

    @Operation(summary = "修改个人信息")
    @OperationLog(title = "修改个人信息", businessType = BusinessTypeEnum.UPDATE)
    @PreventDuplicateSubmit
    @PutMapping
    public Result<Void> updateUserProfile(@RequestBody @Validated UserProfileForm form) {
        boolean result = userProfileService.updateUserProfile(form);
        return Result.judge(result);
    }

    @Operation(summary = "绑定第三方平台账户")
    @OperationLog(title = "绑定第三方平台账户", businessType = BusinessTypeEnum.INSERT)
    @PreventDuplicateSubmit
    @PostMapping("/{type}/bind-third-party")
    public Result<Void> bindThirdParty(@Parameter(description = "第三方平台类型")
                                       @PathVariable String type,
                                       @RequestBody AuthCallback oauth) {
        boolean result = userProfileService.bindThirdParty(type, oauth);
        return Result.judge(result);
    }

    @Operation(summary = "解绑绑定第三方平台账户")
    @OperationLog(title = "解绑绑定第三方平台账户", businessType = BusinessTypeEnum.DELETE)
    @PreventDuplicateSubmit
    @DeleteMapping("/{oauthId}/unbind-third-party")
    public Result<Void> bindThirdParty(
            @Parameter(description = "第三方平台ID")
            @PathVariable String oauthId) {
        boolean result = userProfileService.unbindThirdParty(oauthId);
        return Result.judge(result);
    }

    @Operation(summary = "修改用户头像")
    @OperationLog(title = "修改用户头像", businessType = BusinessTypeEnum.UPLOAD)
    @PreventDuplicateSubmit
    @PatchMapping("/avatar")
    public Result<String> updateAvatar(@Validated @MultipartFileValid MultipartFile avatar) {
        String avatarUrl = userProfileService.updateAvatar(avatar);
        return Result.success(avatarUrl);
    }

    @Operation(summary = "修改个人密码")
    @OperationLog(title = "修改个人密码", businessType = BusinessTypeEnum.UPDATE, excludeParamNames =
            {"oldPassword", "newPassword", "checkPassword"})
    @PreventDuplicateSubmit
    @PatchMapping("/password")
    public Result<Void> updateUserPassword(@RequestBody ResetUserPasswordForm resetUserPasswordForm) {
        boolean updated = userProfileService.updateUserPassword(resetUserPasswordForm);
        return Result.judge(updated);
    }
}
