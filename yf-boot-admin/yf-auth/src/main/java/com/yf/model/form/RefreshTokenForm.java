package com.yf.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 刷新token表单
 *
 * @author YiFei
 * @since 2024/5/5 22:07
 */
@Schema(description = "刷新token表单")
@Data
public class RefreshTokenForm {

    @Schema(description = "访问token")
    @NotBlank
    private String accessToken;

    @Schema(description = "刷新token")
    @NotBlank
    private String refreshToken;
}
