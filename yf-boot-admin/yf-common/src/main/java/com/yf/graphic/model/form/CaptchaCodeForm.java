package com.yf.graphic.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

/**
 * 校验验证码form
 *
 * @author yiFei
 * @since 2024/3/7 13:25
 */
@Data
@Builder
@Schema(description = "校验验证码form")
public class CaptchaCodeForm {

    @Schema(description = "验证key")
    @NotBlank
    public String verifyCodeKey;

    @Schema(description = "验证码")
    @NotBlank
    public String verifyCode;

}
