package com.yf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.model.entity.SysUserRole;

import java.util.List;

/**
 * 用户和角色关联表-SysUserRoleService
 *
 * @author YiFei
 * @since 2024-04-23 18:43:36
 */
public interface ISysUserRoleService extends IService<SysUserRole> {
    /**
     * 保存用户角色信息
     *
     * @param userId  用户id
     * @param roleIds 用户角色信息
     * @return 是否保存成功
     */
    boolean saveUserRole(Long userId, List<Integer> roleIds);
}

