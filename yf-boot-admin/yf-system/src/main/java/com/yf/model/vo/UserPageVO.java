package com.yf.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yf.model.enums.DesensitizationTypeEnum;
import com.yf.model.enums.GenderEnum;
import com.yf.serializer.annotation.Desensitization;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户分页返回数据
 *
 * @author YiFei
 * @since 2024/5/12 13:44
 */
@Data
public class UserPageVO {

    @Schema(description="用户ID")
    private Long id;

    @Schema(description="用户名")
    private String username;

    @Schema(description="用户昵称")
    private String nickname;

    @Schema(description="手机号")
    @Desensitization(type = DesensitizationTypeEnum.MOBILE_PHONE)
    private String phoneNumber;

    @Schema(description="性别")
    private GenderEnum gender;

    @Schema(description="用户头像地址")
    private String avatar;

    @Schema(description="用户邮箱")
    @Desensitization(type = DesensitizationTypeEnum.EMAIL)
    private String email;

    @Schema(description="用户状态(1:启用;0:禁用)")
    private Integer status;

    @Schema(description="部门名称")
    private String deptName;

    @Schema(description="角色名称，多个使用英文逗号(,)分割")
    private String roleNames;

    @Schema(description="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
