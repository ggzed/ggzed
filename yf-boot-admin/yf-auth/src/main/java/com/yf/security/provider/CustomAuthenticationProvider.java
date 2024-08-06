package com.yf.security.provider;

import com.yf.exception.ServiceException;
import com.yf.model.enums.LoginTypeEnum;
import com.yf.model.form.LoginForm;
import com.yf.model.result.ResultCode;
import com.yf.security.authentication.ILoginProcessTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义AuthenticationProvider
 * 角色 : 简单工厂 + 模版方法
 *
 * @author YiFei
 * @since 2024/4/16 18:37
 */
@Component
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    // 使用静态初始化块来初始化不同登录类型对应的策略映射
    private final Map<LoginTypeEnum, ILoginProcessTemplate> loginStrategyMap = new EnumMap<>(LoginTypeEnum.class);

    /**
     * 通过构造函数注入`UserDetailsService`和`PasswordEncoder`，
     * 并将它们设置到父类的对应属性中。这一步是必要的，因为`DaoAuthenticationProvider`
     * 需要这些服务来加载用户详情并对密码进行验证。
     *
     * @param userDetailsService 用户服务，提供了一种从持久层加载用户详情的方式。
     * @param passwordEncoder    密码编码器，用于在验证过程中对密码进行编码和匹配。
     */
    @Autowired
    public CustomAuthenticationProvider(List<ILoginProcessTemplate> loginProcessTemplates, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        super.setPasswordEncoder(passwordEncoder); // 设置密码编码器
        super.setUserDetailsService(userDetailsService); // 设置用户详情服务
        loginProcessTemplates.forEach(template -> this.loginStrategyMap.put(template.getLoginTypeSupport(), template));
    }

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 1. 根据凭证到工厂获取对象
        ILoginProcessTemplate loginProcessTemplate = loginStrategyMap.get((LoginTypeEnum) authentication.getCredentials());
        if (loginProcessTemplate == null) {
            throw new ServiceException(ResultCode.AUTH_MALICIOUS_LOGIN);
        }
        LoginForm principal = (LoginForm) authentication.getPrincipal();
        // 2. 校验传入参数
        if (!loginProcessTemplate.validateParameters(principal)) {
            throw new ServiceException(ResultCode.AUTH_PARAMETER_ERROR);
        }
        // 3. 自动注册用户
        if (!loginProcessTemplate.registeredUsers(principal)) {
            throw new ServiceException(ResultCode.AUTH_REGISTER_USER_ERROR);
        }
        // 4. 查询用户信息
        UserDetails userDetails = loginProcessTemplate.getUserDetailsByPrincipal(principal);
        if (userDetails == null) {
            throw new ServiceException(ResultCode.AUTH_LOGIN_ERROR);
        }
        // 5. 后置校验用户信息
        if (!loginProcessTemplate.validatePostParameters(principal, userDetails)) {
            throw new ServiceException(ResultCode.AUTH_LOGIN_ERROR);
        }
        // 6. 创建成功的认证令牌并返回
        return createSuccessAuthentication(userDetails, authentication, userDetails);
    }
}