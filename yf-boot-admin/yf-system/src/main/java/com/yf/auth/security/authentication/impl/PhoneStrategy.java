package com.yf.auth.security.authentication.impl;

import com.yf.auth.model.enums.LoginTypeEnum;
import com.yf.auth.security.authentication.ILoginProcessStrategy;
import com.yf.model.system.form.LoginForm;
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
public class PhoneStrategy implements ILoginProcessStrategy {
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
