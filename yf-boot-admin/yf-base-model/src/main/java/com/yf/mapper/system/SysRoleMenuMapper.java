package com.yf.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.model.system.entity.SysRoleMenu;

import java.util.List;

/**
 * 角色和菜单关联表-SysRoleMenu
 *
 * @author YiFei
 * @since 2024-04-23 18:43:35
 */
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    List<Integer> findMenuIdsByRoleId(Integer roleId);
}

