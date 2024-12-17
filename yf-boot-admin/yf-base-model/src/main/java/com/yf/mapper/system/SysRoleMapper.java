package com.yf.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.model.system.entity.SysRole;

import java.util.Set;

/**
 * 角色表-SysRole
 *
 * @author YiFei
 * @since 2024-04-23 18:43:36
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 获取最大的数据权限
     *
     * @param roles 角色 Code 集合
     * @return 数据权限
     */
    Integer getMaximumDataScope(Set<String> roles);
}

