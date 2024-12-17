package com.yf.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.constants.RedisKeyConstants;
import com.yf.converter.RoleConverter;
import com.yf.exception.ServiceException;
import com.yf.mapper.system.SysRoleMapper;
import com.yf.model.common.Option;
import com.yf.model.system.entity.SysRole;
import com.yf.model.system.entity.SysRoleMenu;
import com.yf.model.system.entity.SysUserRole;
import com.yf.model.system.form.RoleForm;
import com.yf.model.system.query.RolePageQuery;
import com.yf.model.vo.RolePageVO;
import com.yf.result.ResultCode;
import com.yf.service.ISysRoleMenuService;
import com.yf.service.ISysRoleService;
import com.yf.service.ISysUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * 角色表-SysRoleIServiceImpl
 *
 * @author YiFei
 * @since 2024-04-23 18:43:36
 */
@Service("sysRoleService")
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final RoleConverter roleConverter;
    private final ISysUserRoleService userRoleService;
    private final ISysRoleMenuService roleMenuService;

    /**
     * 获取最大的数据权限
     *
     * @param roles 角色 Code 集合
     * @return 数据权限
     */
    @Override
    public Integer getMaximumDataScope(Set<String> roles) {
        return this.getBaseMapper().getMaximumDataScope(roles);
    }

    /**
     * 查询角色
     *
     * @param queryParams 查询参数
     * @return 角色列表
     */
    @Override
    public IPage<RolePageVO> getRolePage(RolePageQuery queryParams) {
        String keywords = queryParams.getKeywords();
        // 1. 获取分页数据
        Page<SysRole> page = this.lambdaQuery().and(StringUtils.hasText(keywords),
                        wrapper -> wrapper
                                .like(StringUtils.hasText(keywords), SysRole::getName, keywords)   // Name 模糊查询
                                .or()
                                .like(StringUtils.hasText(keywords), SysRole::getCode, keywords))            // Code码 模糊查询
                .page(queryParams.lambdaMpPage(SysRole::getSort, true));
        // 2. 返回 Vo
        return roleConverter.page2pageVo(page);
    }

    /**
     * 增加角色
     *
     * @param roleForm 角色表单
     * @return 增加后的Id
     */
    @Override
    public Integer saveRole(RoleForm roleForm) {
        // 1. 校验是否重复
        isDuplicate(-1, roleForm);
        // 2. form 转换 entity
        SysRole entity = roleConverter.form2entity(roleForm);
        // 3. 保存到数据库
        this.save(entity);
        return entity.getId();
    }

    /**
     * 删除角色
     *
     * @param roleIds 角色 id 集合
     * @return 是否删除成功
     */
    @Override
    @Transactional
    public boolean deleteRole(List<Integer> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return false;
        }
        // 1. 查询角色是否绑定用户
        boolean exists = userRoleService.lambdaQuery().in(SysUserRole::getRoleId, roleIds).exists();
        if (exists) {
            throw new ServiceException(ResultCode.ROLE_BIND_USER);
        }
        // 2. 删除角色
        this.lambdaUpdate().in(SysRole::getId, roleIds).remove();
        // 3. 删除角色绑定的菜单
        roleMenuService.lambdaUpdate().in(SysRoleMenu::getRoleId, roleIds).remove();
        return true;
    }

    /**
     * 角色表单数据
     *
     * @param roleId 角色Id
     * @return 角色表单数据
     */
    @Override
    public RoleForm getRoleForm(Integer roleId) {
        // 1. 查询角色数据
        SysRole oneSysRole = this.lambdaQuery().eq(SysRole::getId, roleId).one();
        // 2. entity 转换为 form 返回
        return roleConverter.entity2form(oneSysRole);
    }

    /**
     * 修改角色状态
     *
     * @param roleId 角色 Id
     * @param status 状态值
     * @return 是否修改状态成功
     */
    @Override
    public boolean updateRoleStatus(Integer roleId, Boolean status) {
        return this.lambdaUpdate().eq(SysRole::getId, roleId).set(SysRole::getStatus, status).update();
    }

    /**
     * 修改角色
     *
     * @param roleId   角色 Id
     * @param roleForm 角色表单
     * @return 是否修改成功
     */
    @Override
    public boolean updateRole(Integer roleId, RoleForm roleForm) {
        // 1. 判断是否重复
        isDuplicate(roleId, roleForm);
        // 2. 修改到数据库
        return this.lambdaUpdate().eq(SysRole::getId, roleId).update(roleConverter.form2entity(roleForm));
    }

    /**
     * 获取角色的菜单ID集合
     *
     * @param roleId 角色 id
     * @return 菜单ID集合
     */
    @Override
    public List<Integer> getRoleMenuIds(Integer roleId) {
        return roleMenuService.findMenuIdsByRoleId(roleId);
    }

    /**
     * 分配菜单权限给角色
     *
     * @param roleId  角色 id
     * @param menuIds 菜单 id 集合
     * @return 是否分配成功
     */
    @Override
    @Transactional
    @CacheEvict(cacheNames = RedisKeyConstants.SYSTEM_ROUTE_CACHE_PREFIX, key = "'routes'")
    public boolean updateRoleMenus(Integer roleId, List<Integer> menuIds) {
        // 注 : 未考虑重新分配权限后的实时性 ， 已登录的用户不会受到本地权限分配的影响
        // 解决方案可考虑 : 异步踢出拥有该权限的用户
        return roleMenuService.saveRoleMenu(roleId, menuIds);
    }

    /**
     * @return 角色下拉列表
     */
    @Override
    public List<Option<Integer>> listRoleOptions() {
        // 1. 查询所有数据
        List<SysRole> list = this.lambdaQuery().select(SysRole::getId, SysRole::getName)   // 查询 id 和 name
                .orderByAsc(SysRole::getSort)               // 按照 sort 排序
                .list();
        // 2. list -> options 返回
        return roleConverter.list2options(list);
    }

    /**
     * 角色是否重复 : form不会全为空 , 否则报错
     */
    private void isDuplicate(Integer roleId, RoleForm roleForm) {
        boolean hasCode = StringUtils.hasText(roleForm.getCode());
        SysRole oneSysRole = this.lambdaQuery()
                .select(SysRole::getId, SysRole::getCode)
                .eq(hasCode, SysRole::getCode, roleForm.getCode())  // 根据Code码查询
                .one();
        if (oneSysRole == null || oneSysRole.getId().equals(roleId)) {
            return;
        }
        // 1. 校验编码是否重复
        if (hasCode && roleForm.getCode().equals(oneSysRole.getCode())) {
            throw new ServiceException(ResultCode.ROLE_CODE_DUPLICATE);
        }
    }
}

