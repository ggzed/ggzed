package com.yf.security.authentication.impl;

import cn.hutool.core.lang.Validator;
import com.yf.constants.MailConstants;
import com.yf.exception.ServiceException;
import com.yf.model.dto.SysUserDetails;
import com.yf.model.dto.UserAuthInfo;
import com.yf.model.entity.SysUser;
import com.yf.model.enums.LoginTypeEnum;
import com.yf.model.form.LoginForm;
import com.yf.model.form.UserForm;
import com.yf.model.result.ResultCode;
import com.yf.security.authentication.ILoginProcessStrategy;
import com.yf.service.IEmailService;
import com.yf.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 邮箱登录
 *
 * @author YiFei
 * @since 2024/3/6 18:56
 */
@Component
@RequiredArgsConstructor
public class EmailStrategy implements ILoginProcessStrategy {

    private final IEmailService emailService;
    private final ISysUserService userService;

    @Override
    public LoginTypeEnum getLoginTypeSupport() {
        return LoginTypeEnum.EMAIL;
    }

    /**
     * 校验用户传入参数
     */
    @Override
    public boolean validateParameters(LoginForm principal) {
        String email = principal.getEmail();
        String emailCode = principal.getEmailCode();
        // 1. 校验邮箱合法性
        if (!Validator.isEmail(email)) {
            return false;
        }
        // 2. 校验验证码合法性
        if (!MailConstants.EMAIL_CODE_NUM.equals(emailCode.length())) {
            return false;
        }
        // 3. 验证验证码是否正确
        if (!emailService.checkEmailCode(principal.getEmail(), principal.getEmailCode())) {
            throw new ServiceException(ResultCode.AUTH_CODE_ERROR);
        }
        return true;
    }


    /**
     * 自动注册用户
     *
     * @param principal 表单信息
     * @return 是否注册成功
     */
    @Override
    public boolean registeredUsers(LoginForm principal) {
        String email = principal.getEmail();
        // 1. 查询数据库是否存在该用户
        boolean exists = userService.lambdaQuery()
                .eq(SysUser::getEmail, email)
                .exists();
        if (!exists) {
            userService.autoRegisterUser(UserForm.builder().email(email).build());
        }
        return true;
    }

    @Override
    public UserDetails getUserDetailsByPrincipal(LoginForm principal) {
        // 1. 查询权限信息
        UserAuthInfo userAuthInfo = userService.getUserAuthInfo(principal);
        // 2. 构建 SysUserDetails 信息
        return new SysUserDetails(userAuthInfo);
    }

}


