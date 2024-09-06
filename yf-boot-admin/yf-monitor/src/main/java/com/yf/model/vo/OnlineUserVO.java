package com.yf.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 在线用户VO
 *
 * @author : YiFei
 * @since : 2024/9/5 10:34
 */
@Schema(description = "在线用户信息展示")
@Data
public class OnlineUserVO {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户昵称")
    private String nickname;

}
