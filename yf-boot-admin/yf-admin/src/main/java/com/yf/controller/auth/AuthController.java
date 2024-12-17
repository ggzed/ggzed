package com.yf.controller.auth;


import com.yf.auth.model.enums.LoginTypeEnum;
import com.yf.auth.model.form.RefreshTokenForm;
import com.yf.auth.security.service.IAuthService;
import com.yf.graphic.model.dto.CaptchaResult;
import com.yf.graphic.service.ICaptchaCodeService;
import com.yf.justauth.factory.JustAuthFactory;
import com.yf.log.annotation.OperationLog;
import com.yf.mail.service.IEmailService;
import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.model.system.dto.LoginResult;
import com.yf.model.system.form.LoginForm;
import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
import com.yf.rate_limiting.annotation.RateLimiter;
import com.yf.rate_limiting.annotation.RateLimiters;
import com.yf.rate_limiting.annotation.RateRule;
import com.yf.rate_limiting.model.enums.LimitTypeEnum;
import com.yf.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 权限-AuthController
 *
 * @author: YiFei
 * @since : 2024-04-15 20:51:57
 */
@Tag(name = "权限")
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    /**
     * 权限-AuthService
     */
    private final IAuthService authService;
    private final JustAuthFactory justAuthFactory;
    private final ICaptchaCodeService captchaCodeService;
    private final IEmailService emailService;


    @Operation(summary = "登陆")
    @OperationLog(title = "登陆", businessType = BusinessTypeEnum.LOGIN, isSaveRequestData = false, isSaveResponseData = false)
    @PreventDuplicateSubmit
    @PostMapping("/login/{type}")
    public Result<LoginResult> login(@RequestBody LoginForm loginForm, @PathVariable LoginTypeEnum type) {
        LoginResult loginResult = authService.login(loginForm, type);
        return Result.success(loginResult);
    }

    @Operation(summary = "登出")
    @OperationLog(title = "登出", businessType = BusinessTypeEnum.LOGOUT)
    @PreventDuplicateSubmit
    @DeleteMapping("/logout")
    public Result<Void> logout() {
        boolean logout = authService.logout();
        return Result.judge(logout);
    }

    @Operation(summary = "刷新token")
    @PatchMapping("/refresh")
    public Result<LoginResult> refreshToken(@RequestBody @Validated RefreshTokenForm refreshTokenForm) {
        LoginResult loginResult = authService.refreshToken(refreshTokenForm);
        return Result.success(loginResult);
    }

    @Operation(summary = "第三方登录跳转")
    @PreventDuplicateSubmit(expire = 3)
    @GetMapping("/{type}/redirect")
    public Result<String> redirectToThirdPartyLogin(@PathVariable String type) {
        AuthRequest authRequest = justAuthFactory.getAuthRequest(type);
        return Result.success(authRequest.authorize(AuthStateUtils.createState()));
    }

    @RateLimiters(rateLimiters = {
            @RateLimiter(
                    limitTypeEnum = LimitTypeEnum.IP,
                    rateRules = @RateRule
            )
    })
    @Operation(summary = "发送验证码")
    @GetMapping("/captcha/code")
    public Result<CaptchaResult> sendCaptchaCode() {
        return Result.success(captchaCodeService.generateVerifyCode());
    }

    @Operation(summary = "发送邮箱验证码")
    @PreventDuplicateSubmit(expire = 60)
    @GetMapping("/email/code")
    public Result<Void> sendEmailCode(String email) {
        emailService.sendEmailCode(email);
        return Result.success();
    }

}

