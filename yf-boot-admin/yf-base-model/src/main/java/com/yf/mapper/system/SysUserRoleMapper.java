package com.yf.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.model.system.entity.SysUserRole;

import java.util.List;

/**
 * 用户和角色关联表-SysUserRole
 *
 * @author YiFei
 * @since 2024-04-23 18:43:36
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    /**
     * 用户用户名查询用户角色
     *
     * @param userId 角色id
     * @return 用户拥有的角色
     */
    List<Integer> findRoleIdsByUserId(Long userId);
}

