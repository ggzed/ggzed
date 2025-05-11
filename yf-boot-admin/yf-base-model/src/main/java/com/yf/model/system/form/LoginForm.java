package com.yf.model.system.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import me.zhyd.oauth.model.AuthCallback;

@Data
@Builder
@Schema(description = "统一登录表单")
public class LoginForm {

    @Schema(description = "用户Id", hidden = true, nullable = true)
    private String userId;

    @Schema(description = "用户名", example = "admin", nullable = true)
    private String username;

    @Schema(description = "手机号", nullable = true)
    private String phoneNumber;

    @Schema(description = "邮箱", nullable = true)
    private String email;

    @Schema(description = "密码", example = "123456", nullable = true)
    private String password;

    @Schema(description = "验证码", nullable = true)
    private String verifyCode;

    @Schema(description = "验证码标识 key ", nullable = true)
    private String verifyCodeKey;

    @Schema(description = "短信验证码", nullable = true)
    private String smsCode;

    @Schema(description = "邮箱验证码", nullable = true)
    private String emailCode;

    @Schema(description = "第三方登录 Oauth 授权对象", nullable = true)
    private AuthCallback oauth;
}