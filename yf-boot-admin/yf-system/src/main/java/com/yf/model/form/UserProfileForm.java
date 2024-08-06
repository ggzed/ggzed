package com.yf.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 个人信息修改表单 （ TODO 后续 email , phoneNumber 移步为单个信息修改 ）
 *
 * @author : YiFei
 * @since : 2024/7/26 10:13
 */
@Data
public class UserProfileForm {
    /**
     * 昵称
     */
    @Schema(description = "昵称")
    @Length(max = 24, message = "用户名过长")
    private String nickname;
    /**
     * 性别(0:未知,1:男;2:女))
     */
    @Schema(description = "性别(0:未知,1:男;2:女))")
    private Integer gender;
    /**
     * 联系方式
     */
    @Schema(description = "联系方式")
    @Pattern(regexp = "^$|^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$", message = "手机号码格式不正确")
    private String phoneNumber;
    /**
     * 用户邮箱
     */
    @Schema(description = "用户邮箱")
    @Email
    private String email;
}
