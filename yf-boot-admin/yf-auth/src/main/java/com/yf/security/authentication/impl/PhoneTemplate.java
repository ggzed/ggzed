package com.yf.security.authentication.impl;

import com.yf.model.enums.LoginTypeEnum;
import com.yf.model.form.LoginForm;
import com.yf.security.authentication.ILoginProcessTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 手机号登陆
 *
 * @author YiFei
 * @since 2024/4/16 19:46
 */
@Component
@RequiredArgsConstructor
public class PhoneTemplate implements ILoginProcessTemplate {
    @Override
    public LoginTypeEnum getLoginTypeSupport() {
        return LoginTypeEnum.PHONE;
    }

    @Override
    public boolean validateParameters(LoginForm principal) {
        return false;
    }

    @Override
    public UserDetails getUserDetailsByPrincipal(LoginForm principal) {
        return null;
    }
}
