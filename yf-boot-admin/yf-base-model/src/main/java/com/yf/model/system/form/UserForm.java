package com.yf.model.system.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 保存用户表单
 *
 * @author YiFei
 * @since 2024/5/10 13:03
 */
@Data
@Builder
public class UserForm {

    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Length(max = 24, message = "用户名过长")
    private String username;

    @Schema(description = "昵称")
    @NotBlank(message = "昵称不能为空")
    @Length(max = 24, message = "昵称过长")
    private String nickname;

    @Schema(description = "手机号码")
    @Pattern(regexp = "^$|^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$", message = "手机号码格式不正确")
    private String phoneNumber;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "邮箱")
    @Email
    private String email;

    @Schema(description = "用户状态(1:正常;0:禁用)")
    private Integer status;

    @Schema(description = "部门ID")
    private Integer deptId;

    @Schema(description = "角色ID集合")
    @NotEmpty(message = "用户角色不能为空")
    private List<Integer> roleIds;
}
