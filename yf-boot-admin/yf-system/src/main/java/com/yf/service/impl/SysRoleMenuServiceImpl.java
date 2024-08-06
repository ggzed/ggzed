package com.yf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.mapper.SysRoleMenuMapper;
import com.yf.model.entity.SysRoleMenu;
import com.yf.service.ISysRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色和菜单关联表-SysRoleMenuIServiceImpl
 *
 * @author YiFei
 * @since 2024-04-23 18:43:36
 */
@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    /**
     * 分配菜单权限给角色
     *
     * @param roleId  角色 id
     * @param menuIds 菜单 id 集合
     * @return 是否分配成功
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean saveRoleMenu(Integer roleId, List<Integer> menuIds) {
        if (roleId == null) {
            return false;
        }

        // 1. 获取目前角色拥有的菜单 ( 注: 不使用 mybatis-plus 是因为获取到的对象是 SysRoleMenu 而不是 Integer )
        List<Integer> existingMenuIds = this.getBaseMapper().findMenuIdsByRoleId(roleId);

        // 2. 计算需要删除的菜单和需要新增的菜单
        Set<Integer> toDelete = new HashSet<>(existingMenuIds);
        Set<Integer> toAdd = new HashSet<>(menuIds);

        // 2.1 计算需要新增的菜单
        existingMenuIds.forEach(toAdd::remove);

        // 2.2 计算需要删除的菜单
        menuIds.forEach(toDelete::remove);

        // 3. 执行删除和新增操作
        if (!CollectionUtils.isEmpty(toDelete)) {
            this.lambdaUpdate().eq(SysRoleMenu::getRoleId, roleId)
                    .in(SysRoleMenu::getMenuId, toDelete)
                    .remove();
        }
        if (!CollectionUtils.isEmpty(toAdd)) {
            List<SysRoleMenu> userRolesToAdd = toAdd.stream()
                    .map(menuId -> new SysRoleMenu(roleId, menuId))
                    .collect(Collectors.toList());
            this.saveBatch(userRolesToAdd);
        }

        return true;
    }

    /**
     * 获取角色的菜单ID集合
     *
     * @param roleId 角色 id
     * @return 菜单ID集合
     */
    @Override
    public List<Integer> findMenuIdsByRoleId(Integer roleId) {
        return this.getBaseMapper().findMenuIdsByRoleId(roleId);
    }
}

