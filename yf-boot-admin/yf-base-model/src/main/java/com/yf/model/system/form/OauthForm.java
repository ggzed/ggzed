package com.yf.model.system.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 第三方授权表单
 *
 * @author YiFei
 * @since 2024/5/20 10:13
 */
@Schema(description = "第三方授权表单")
@Data
@Builder
public class OauthForm {

    @Schema(description = "第三方平台提供者")
    private String platformName;

    @Schema(description = "第三方平台唯一标识")
    private String platformUserId;

    @Schema(description = "第三方平台头像")
    private String platformUserAvatar;

    @Schema(description = "第三方平台用户名")
    private String platformUsername;

}
