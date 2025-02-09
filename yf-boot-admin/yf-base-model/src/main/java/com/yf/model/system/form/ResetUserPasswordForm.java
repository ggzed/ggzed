package com.yf.model.system.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 重置用户密码表单
 *
 * @author YiFei
 * @since 2024/5/12 20:26
 */
@Data
@Schema(description = "修改密码表单")
public class ResetUserPasswordForm {

    @Schema(description = "旧密码")
    private String oldPassword;

    @Schema(description = "新密码")
    @Length(min = 8, max = 16, message = "密码长度在 8 ~ 16")
    private String newPassword;

    @Schema(description = "校验新密码")
    @Length(min = 8, max = 16, message = "密码长度在 8 ~ 16")
    private String checkPassword;
}
