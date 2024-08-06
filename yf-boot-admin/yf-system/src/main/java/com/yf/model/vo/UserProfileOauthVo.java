package com.yf.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 个人中心第三方授权详情信息
 *
 * @author : YiFei
 * @since : 2024/7/23 0:43
 */
@Data
public class UserProfileOauthVo {

    /**
     * 主键
     */
    @Schema(description = "主键")
    private Long id;

    /**
     * 第三方平台提供者
     */
    @Schema(description = "第三方平台提供者")
    private String platformName;

    /**
     * 第三方平台头像
     */
    @Schema(description = "第三方平台头像")
    private String platformUserAvatar;

    /**
     * 第三方平台用户名
     */
    @Schema(description = "第三方平台用户名")
    private String platformUsername;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
