package com.yf.security.authentication;

import com.yf.model.enums.LoginTypeEnum;
import com.yf.model.form.LoginForm;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 登陆过程模板
 *
 * @author YiFei
 * @since 2024/4/16 16:29
 */
public interface ILoginProcessStrategy {

    /**
     * 获取登录类型
     */
    LoginTypeEnum getLoginTypeSupport();

    /**
     * 自动注册用户 : 默认已注册
     */
    default boolean registeredUsers(LoginForm principal) {
        return true;
    }

    /**
     * 校验登录参数
     *
     * @param principal 主体
     */
    boolean validateParameters(LoginForm principal);

    /**
     * 获取用户信息
     */
    UserDetails getUserDetailsByPrincipal(LoginForm principal);

    /**
     * 后置校验用户信息 : 默认校验成功
     */
    default boolean validatePostParameters(LoginForm principal, UserDetails userDetails) {
        return true;
    }
}
