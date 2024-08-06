package com.yf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.model.entity.SysRoleMenu;

import java.util.List;

/**
 * 角色和菜单关联表-SysRoleMenuService
 *
 * @author YiFei
 * @since 2024-04-23 18:43:36
 */
public interface ISysRoleMenuService extends IService<SysRoleMenu> {
    /**
     * 分配菜单权限给角色
     *
     * @param roleId  角色 id
     * @param menuIds 菜单 id 集合
     * @return 是否分配成功
     */
    boolean saveRoleMenu(Integer roleId, List<Integer> menuIds);

    /**
     * 获取角色的菜单ID集合
     *
     * @param roleId 角色 id
     * @return 菜单ID集合
     */
    List<Integer> findMenuIdsByRoleId(Integer roleId);
}

