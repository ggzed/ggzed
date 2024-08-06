package com.yf.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 个人信息Vo
 *
 * @author : YiFei
 * @since : 2024/7/22 19:11
 */
@Data
@Accessors(chain = true)
public class UserProfileInfoVO {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "手机号")
    private String phoneNumber;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "角色信息")
    private List<String> roles;

    @Schema(description = "部门信息")
    private String deptName;

    @Schema(description = "第三方授权信息")
    private List<UserProfileOauthVo> oauthInfo;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
