package com.yf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.model.Option;
import com.yf.model.entity.SysMenu;
import com.yf.model.form.MenuForm;
import com.yf.model.query.MenuPageQuery;
import com.yf.model.vo.MenuPageVO;
import com.yf.model.vo.RouteVO;

import java.util.List;
import java.util.Set;

/**
 * 系统菜单-SysMenuService
 *
 * @author YiFei
 * @since 2024-04-23 18:43:35
 */
public interface ISysMenuService extends IService<SysMenu> {
    /**
     * 查询角色的全部权限
     *
     * @param roles 角色 Code 集合
     * @return 权限集合
     */
    Set<String> listPermissions(Set<String> roles);

    /**
     * @return 路由列表
     */
    List<RouteVO> listRoutes();

    /**
     * @return 菜单下拉列表
     */
    List<Option<Integer>> listMenuOptions();

    /**
     * 查询菜单
     *
     * @param queryParams 查询参数
     * @return 分页数据
     */
    List<MenuPageVO> getMenuPage(MenuPageQuery queryParams);

    /**
     * 新增菜单
     *
     * @param menuForm 菜单表单
     * @return 新增的菜单 Id
     */
    Integer saveMenu(MenuForm menuForm);

    /**
     * 删除菜单
     *
     * @param menuIds 菜单 ID 集合
     * @return 是否删除成功
     */
    boolean deleteMenu(List<Integer> menuIds);

    /**
     * 修改菜单
     *
     * @param menuId   菜单Id
     * @param menuForm 修改菜单表单
     * @return 是否修改成功
     */
    boolean updateMenu(Integer menuId, MenuForm menuForm);

    /**
     * 修改菜单显示状态 ( 修改菜单以及子菜单 )
     *
     * @param menuId 菜单Id
     * @param hidden 是否隐藏
     * @return 是否修改成功
     */
    boolean updateMenuHidden(Integer menuId, Boolean hidden);

    /**
     * 菜单表单数据
     *
     * @param menuId 菜单Id
     * @return 菜单表单数据
     */
    MenuForm getMenuForm(Integer menuId);
}

