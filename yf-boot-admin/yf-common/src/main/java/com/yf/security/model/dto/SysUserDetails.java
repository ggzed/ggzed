package com.yf.security.model.dto;

import cn.hutool.core.util.ObjectUtil;
import com.yf.exception.ServiceException;
import com.yf.model.common.dto.UserAuthInfo;
import com.yf.model.common.enums.EnableStatusEnum;
import com.yf.result.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Spring Security UserDetails
 *
 * @author : YiFei
 * @since : 2023/9/17 21:13
 */
@Data
@Builder
@AllArgsConstructor
public class SysUserDetails implements UserDetails {
    /**
     * 用户 ID
     */
    private Long userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 是否启用
     */
    private Boolean enabled;
    /**
     * 权限
     */
    private Set<String> permissions;
    /**
     * 部门Id
     */
    private Long deptId;
    /**
     * 数据权限范围
     */
    private Integer dataScope;
    /**
     * 角色
     */
    private Collection<SimpleGrantedAuthority> roles;

    /**
     * 构建 SysUserDetails
     *
     * @param userAuthInfo 通过其创建 SysUserDetails
     */
    public SysUserDetails(UserAuthInfo userAuthInfo) {
        if (userAuthInfo == null) {
            throw new ServiceException(ResultCode.AUTH_USER_NOT_FOUND);
        }
        this.userId = userAuthInfo.getUserId();                 // 设置用户ID
        this.username = userAuthInfo.getUsername();             // 设置用户名

        Set<String> roles = userAuthInfo.getRoles();
        if (!CollectionUtils.isEmpty(roles)) {
            // 处理角色信息
            this.roles = roles.stream()
                    // 在Spring Security中，所有角色前缀需要为 ROLE_
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // 将角色转换为SimpleGrantedAuthority对象
                    .collect(Collectors.toSet()); // 转换为Set集合
        } else {
            // 如果没有角色信息，赋值为空集合
            this.roles = Collections.emptySet();
        }
        this.password = userAuthInfo.getPassword();             // 设置密码

        // 根据用户状态设置是否可用
        this.enabled = ObjectUtil.equal(userAuthInfo.getStatus(), EnableStatusEnum.ENABLE.getValue());

        this.permissions = userAuthInfo.getPermissions();       // 设置权限集合
        this.deptId = userAuthInfo.getDeptId();                 // 设置部门Id
        this.dataScope = userAuthInfo.getDataScope();           // 设置数据范围
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
