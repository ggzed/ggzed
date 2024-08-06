package com.yf.config;


import com.yf.configuration.SystemConfiguration;
import com.yf.security.exception.CustomAccessDeniedHandler;
import com.yf.security.exception.CustomAuthenticationEntryPoint;
import com.yf.security.filter.BlacklistFilter;
import com.yf.security.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig
 *
 * @author : YiFei
 * @since : 2022/1/4 1:41
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final BlacklistFilter blacklistFilter;
    private final SystemConfiguration systemConfiguration;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    /**
     * security 配置
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Security 基础配置
        http
                // TODO 上线取消 cors 配置 用 Nginx 代理
                .cors(AbstractHttpConfigurer::disable)
                // 关闭 CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // 关闭 退出登录，自定义接口退出
                .logout(AbstractHttpConfigurer::disable)
                // 防止 缓存冲突  ( 禁用缓存控制以便让前端或者后端自己进行缓存控制 )
                .headers(headers -> headers.cacheControl(HeadersConfigurer.CacheControlConfig::disable))
                //  禁用  HttpSession
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // Security 接口访问权限配置
        http
                .authorizeHttpRequests(requestMatcherRegistry -> requestMatcherRegistry
                        .requestMatchers(systemConfiguration.getSecurityWhitelistPaths()).permitAll()
                        .anyRequest().authenticated());
        // Security 过滤器配置
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(blacklistFilter, jwtTokenFilter.getClass());

        // Security 配置错误响应
        http
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(authenticationEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler));
        return http.build();
    }

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager();
    }
}
