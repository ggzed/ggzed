package com.yf.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.model.common.Option;
import com.yf.model.system.entity.SysRole;
import com.yf.model.system.form.RoleForm;
import com.yf.model.system.query.RolePageQuery;
import com.yf.model.vo.RolePageVO;

import java.util.List;
import java.util.Set;

/**
 * 角色表-SysRoleService
 *
 * @author YiFei
 * @since 2024-04-23 18:43:36
 */
public interface ISysRoleService extends IService<SysRole> {
    /**
     * 获取最大的数据权限
     *
     * @param roles 角色 Code 集合
     * @return 数据权限
     */
    Integer getMaximumDataScope(Set<String> roles);

    /**
     * @return 角色下拉列表
     */
    List<Option<Integer>> listRoleOptions();

    /**
     * 查询角色
     *
     * @param queryParams 查询参数
     * @return 角色列表
     */
    IPage<RolePageVO> getRolePage(RolePageQuery queryParams);

    /**
     * 增加角色
     *
     * @param roleForm 角色表单
     * @return 增加后的Id
     */
    Integer saveRole(RoleForm roleForm);

    /**
     * 删除角色
     *
     * @param roleIds 角色 id 集合
     * @return 是否删除成功
     */
    boolean deleteRole(List<Integer> roleIds);

    /**
     * 角色表单数据
     *
     * @param roleId 角色Id
     * @return 角色表单数据
     */
    RoleForm getRoleForm(Integer roleId);

    /**
     * 修改角色状态
     *
     * @param roleId 角色 Id
     * @param status 状态值
     * @return 是否修改状态成功
     */
    boolean updateRoleStatus(Integer roleId, Boolean status);

    /**
     * 修改角色
     *
     * @param roleId   角色 Id
     * @param roleForm 角色表单
     * @return 是否修改成功
     */
    boolean updateRole(Integer roleId, RoleForm roleForm);

    /**
     * 获取角色的菜单ID集合
     *
     * @param roleId 角色 id
     * @return 菜单ID集合
     */
    List<Integer> getRoleMenuIds(Integer roleId);

    /**
     * 分配菜单权限给角色
     *
     * @param roleId  角色 id
     * @param menuIds 菜单 id 集合
     * @return 是否分配成功
     */
    boolean updateRoleMenus(Integer roleId, List<Integer> menuIds);

    /**
     * 获取管理员角色
     *
     * @return 管理员角色
     */
    SysRole getAdminRole();
}

