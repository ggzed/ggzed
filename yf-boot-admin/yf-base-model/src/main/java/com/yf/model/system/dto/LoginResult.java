package com.yf.model.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "登录响应对象")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResult {

    @Schema(description = "访问token")
    private String accessToken;

    @Schema(description = "刷新token")
    private String refreshToken;

    @Schema(description = "过期时间(单位：毫秒)")
    private Long expires;

}
