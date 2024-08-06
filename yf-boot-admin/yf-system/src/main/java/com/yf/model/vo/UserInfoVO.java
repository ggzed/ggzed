package com.yf.model.vo;

import com.yf.model.enums.DesensitizationTypeEnum;
import com.yf.serializer.annotation.Desensitization;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * 用户信息Vo
 *
 * @author YiFei
 * @since 2024/5/5 20:48
 */
@Schema(description ="当前登录用户视图对象")
@Data
public class UserInfoVO {

    @Schema(description="用户ID")
    private Long userId;

    @Schema(description="用户名")
    private String username;

    @Schema(description="用户昵称")
    private String nickname;

    @Schema(description = "手机号")
    @Desensitization(type = DesensitizationTypeEnum.MOBILE_PHONE)
    private String phoneNumber;

    @Schema(description = "邮箱")
    @Desensitization(type = DesensitizationTypeEnum.EMAIL)
    private String email;

    @Schema(description="头像地址")
    private String avatar;

    @Schema(description="用户角色编码集合")
    private Set<String> roles;

    @Schema(description="用户权限标识集合")
    private Set<String> permissions;

}
