package com.yf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.mapper.system.SysUserRoleMapper;
import com.yf.model.system.entity.SysUserRole;
import com.yf.service.ISysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户和角色关联表-SysUserRoleIServiceImpl
 *
 * @author YiFei
 * @since 2024-04-23 18:43:36
 */
@Service("sysUserRoleService")
public class SysUserRoleIServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    /**
     * 保存用户角色信息
     *
     * @param userId  用户id
     * @param roleIds 用户角色信息
     * @return 是否保存成功
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean saveUserRole(Long userId, List<Integer> roleIds) {
        if (userId == null || CollectionUtils.isEmpty(roleIds)) {
            return false;
        }

        // 1. 获取目前用户拥有的角色 ( 注: 不使用 mybatis-plus 是因为获取到的对象是 SysUserRole 而不是 Integer )
        List<Integer> existingRoleIds = this.getBaseMapper().findRoleIdsByUserId(userId);

        // 2. 计算需要删除的角色和需要新增的角色
        Set<Integer> toDelete = new HashSet<>(existingRoleIds);
        Set<Integer> toAdd = new HashSet<>(roleIds);

        // 2.1 计算需要新增的菜单
        existingRoleIds.forEach(toAdd::remove);

        // 2.2 计算需要删除的菜单
        roleIds.forEach(toDelete::remove);

        // 3. 执行删除和新增操作
        if (!CollectionUtils.isEmpty(toDelete)) {
            this.lambdaUpdate().eq(SysUserRole::getUserId, userId)
                    .in(SysUserRole::getRoleId, toDelete)
                    .remove();
        }
        if (!CollectionUtils.isEmpty(toAdd)) {
            List<SysUserRole> userRolesToAdd = toAdd.stream()
                    .map(roleId -> new SysUserRole(userId, roleId))
                    .collect(Collectors.toList());
            this.saveBatch(userRolesToAdd);
        }

        return true;
    }
}

