package com.yf.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.constants.RedisKeyConstants;
import com.yf.constants.SystemConstants;
import com.yf.converter.MenuConverter;
import com.yf.mapper.SysMenuMapper;
import com.yf.model.Option;
import com.yf.model.bo.MenuAndChildrenBo;
import com.yf.model.bo.RouteBo;
import com.yf.model.entity.SysMenu;
import com.yf.model.entity.SysRoleMenu;
import com.yf.model.enums.MenuTypeEnum;
import com.yf.model.form.MenuForm;
import com.yf.model.query.MenuPageQuery;
import com.yf.model.vo.MenuPageVO;
import com.yf.model.vo.RouteVO;
import com.yf.service.ISysMenuService;
import com.yf.service.ISysRoleMenuService;
import com.yf.utils.TreeNodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统菜单-SysMenuIServiceImpl
 *
 * @author YiFei
 * @since 2024-04-23 18:43:35
 */
@Service("sysMenuService")
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    private final MenuConverter menuConverter;
    private final ISysRoleMenuService roleMenuService;

    /**
     * 查询角色的全部权限
     *
     * @param roles 角色 Code 集合
     * @return 权限集合
     */
    @Override
    public Set<String> listPermissions(Set<String> roles) {
        return this.getBaseMapper().listPermissions(roles, MenuTypeEnum.MENU, MenuTypeEnum.BUTTON);
    }

    /**
     * 路由列表
     *
     * @return 路由
     */
    @Cacheable(cacheNames = RedisKeyConstants.SYSTEM_ROUTE_CACHE_PREFIX, key = "'routes'")
    @Override
    public List<RouteVO> listRoutes() {
        List<RouteBo> routes = this.getBaseMapper().listRoutes(MenuTypeEnum.BUTTON);
        List<RouteBo> routeBos = TreeNodeUtil.buildTree(routes, Collections.singletonList(0), (item) -> {
            String path = item.getPath();
            String realName = StrUtil.upperFirst(StrUtil.toCamelCase(path, '-'));
            item.setName(realName);
        });
        return menuConverter.routeBo2Vo(routeBos);
    }

    /**
     * @return 菜单下拉列表
     */
    @Override
    public List<Option<Integer>> listMenuOptions() {
        // 1. 查询所有数据
        List<SysMenu> list = this.lambdaQuery()
                .select(SysMenu::getId, SysMenu::getParentId, SysMenu::getName)
                .orderByAsc(SysMenu::getSort)
                .list();
        // 2. list -> options 返回
        return TreeNodeUtil.buildTree(menuConverter.list2options(list), Collections.singletonList(0));
    }

    /**
     * 查询菜单
     *
     * @param queryParams 查询参数
     * @return 分页数据
     */
    @Override
    public List<MenuPageVO> getMenuPage(MenuPageQuery queryParams) {
        String keywords = queryParams.getKeywords();
        Integer type = queryParams.getType();
        Integer hidden = queryParams.getHidden();
        // 1. 获取分页数据
        List<SysMenu> list = this.lambdaQuery()
                .and(StringUtils.hasText(keywords), wrapper ->
                        wrapper.like(StringUtils.hasText(keywords), SysMenu::getName, keywords) // 模糊查询 Name
                )
                .eq(hidden != null, SysMenu::getHidden, hidden) // 菜单状态
                .eq(type != null, SysMenu::getType, type)       // 菜单类型
                .orderByAsc(SysMenu::getSort)
                .list();
        // 2. 构建树形结构
        // 2.1 获取到所有父元素
        Set<Integer> allIds = list.stream().map(SysMenu::getId).collect(Collectors.toSet());
        Set<Integer> allParentIds = list.stream().map(SysMenu::getParentId).collect(Collectors.toSet());
        Set<Integer> ids = allParentIds.stream().filter(id -> !allIds.contains(id)).collect(Collectors.toSet());
        // 3. entity2vo
        List<MenuPageVO> menuPageVOS = menuConverter.list2vo(list);
        // 4. 返回树形结构
        return TreeNodeUtil.buildTree(menuPageVOS, ids);
    }

    /**
     * 新增菜单
     *
     * @param menuForm 菜单表单
     * @return 新增的菜单 Id
     */
    @Override
    @CacheEvict(value = RedisKeyConstants.SYSTEM_ROUTE_CACHE_PREFIX, key = "'routes'")
    public Integer saveMenu(MenuForm menuForm) {
        // 1. 构建菜单 entity
        SysMenu entity = buildSysMenu(menuForm);
        // 2. 存储数据
        this.save(entity);
        return entity.getId();
    }

    /**
     * 修改菜单
     *
     * @param menuId   菜单Id
     * @param menuForm 修改菜单表单
     * @return 是否修改成功
     */
    @Override
    @Transactional
    @CacheEvict(value = RedisKeyConstants.SYSTEM_ROUTE_CACHE_PREFIX, key = "'routes'")
    public boolean updateMenu(Integer menuId, MenuForm menuForm) {
        // 1. 构建菜单 entity
        Integer parentId = menuForm.getParentId();
        // 2. 处理菜单类型
        handleMenuType(menuForm);
        // 3. 查询菜单 （ 方便确认是否修改 parentId ）
        SysMenu one = this.lambdaQuery()
                .select(SysMenu::getTreePath, SysMenu::getParentId)
                .eq(SysMenu::getId, menuId)
                .one();
        // 4. form 转 entity
        SysMenu entity = menuConverter.form2entity(menuForm);
        if (parentId.equals(one.getParentId())) {
            // 5. 修改当前元素
            this.lambdaUpdate()
                    .eq(SysMenu::getId, menuId)
                    .update(entity);
        } else {
            // 6. 修改当前元素的树形结构 ( 注: 顺序不能变，否则查询出来的 parent_id 为未修改的 parent_id )
            String treePath = TreeNodeUtil.generateTreePath(SystemConstants.INTEGER_ROOT_ID, parentId,
                    (currentParentId) -> this.lambdaQuery()
                            .eq(SysMenu::getId, currentParentId)
                            .select(SysMenu::getTreePath)
                            .one().getTreePath());
            entity.setTreePath(treePath);
            this.lambdaUpdate()
                    .eq(SysMenu::getId, menuId)
                    .update(entity);
            // 7. 修改子菜单的树形结构
            updateMenuChildrenTreePath(menuId);
        }
        return true;
    }

    /**
     * 删除菜单
     *
     * @param menuIds 菜单 ID 集合
     * @return 是否删除成功
     */
    @Override
    @Transactional
    @CacheEvict(value = RedisKeyConstants.SYSTEM_ROUTE_CACHE_PREFIX, key = "'routes'")
    public boolean deleteMenu(List<Integer> menuIds) {
        // 1. 删除当前菜单以及子菜单
        List<MenuAndChildrenBo> menuAndChildrenIds = this.getBaseMapper().getMenuAndChildrenByIds(menuIds);
        if (CollectionUtils.isEmpty(menuAndChildrenIds)) {
            return false;
        }
        List<Integer> deleteIds = menuAndChildrenIds.stream().map(MenuAndChildrenBo::getId).toList();
        // 2. 删除菜单以及子菜单
        boolean removed = this.lambdaUpdate()
                .in(SysMenu::getId, deleteIds)
                .remove();
        if (removed) {
            // 3. 删除菜单角色表绑定的菜单数据
            roleMenuService.lambdaUpdate()
                    .in(SysRoleMenu::getMenuId, deleteIds)
                    .remove();
            return true;
        }
        return false;
    }

    /**
     * 修改菜单显示状态 ( 修改菜单以及子菜单 )
     *
     * @param menuId 菜单Id
     * @param hidden 是否隐藏
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(value = RedisKeyConstants.SYSTEM_ROUTE_CACHE_PREFIX, key = "'routes'")
    public boolean updateMenuHidden(Integer menuId, Boolean hidden) {
        // 1. 查询菜单以及子菜单
        List<MenuAndChildrenBo> menuAndChildrenIds = this.getBaseMapper().getMenuAndChildrenByIds(Collections.singletonList(menuId));
        List<Integer> updateIds = menuAndChildrenIds.stream().map(MenuAndChildrenBo::getId).toList();
        // 2. 修改菜单状态
        return this.lambdaUpdate()
                .in(SysMenu::getId, updateIds)
                .ne(SysMenu::getType, MenuTypeEnum.BUTTON)
                .set(SysMenu::getHidden, hidden)
                .update();
    }

    /**
     * 菜单表单数据
     *
     * @param menuId 菜单Id
     * @return 菜单表单数据
     */
    @Override
    public MenuForm getMenuForm(Integer menuId) {
        // 1. 查询角色数据
        SysMenu sysMenu = this.lambdaQuery()
                .eq(SysMenu::getId, menuId)
                .one();
        // 2. entity 转换为 form 返回
        return menuConverter.entity2form(sysMenu);
    }

    /**
     * 根据 menuForm 构建  SysMenu
     *
     * @param menuForm menuForm
     * @return SysMenu
     */
    private SysMenu buildSysMenu(MenuForm menuForm) {
        Integer parentId = menuForm.getParentId();
        // 1. 处理对应菜单不同类型的数据
        handleMenuType(menuForm);
        // 2. 转换为 entity
        SysMenu entity = menuConverter.form2entity(menuForm);
        // 2.1 生成 tree_path
        String treePath = TreeNodeUtil.generateTreePath(SystemConstants.INTEGER_ROOT_ID, parentId,
                (currentParentId) -> this.lambdaQuery()
                        .eq(SysMenu::getId, currentParentId)
                        .select(SysMenu::getTreePath)
                        .one().getTreePath());
        // 2.2 设置 tree_path 路径
        entity.setTreePath(treePath);
        return entity;
    }


    /**
     * 根据 menuId 修改子菜单 tree_path
     *
     * @param menuId 当前菜单Id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateMenuChildrenTreePath(Integer menuId) {
        // 1. 查询菜单信息 ( filter 排除当前菜单 , 剩下为子菜单信息 )
        List<MenuAndChildrenBo> childrenBos = this.getBaseMapper()
                .getMenuAndChildrenByIds(Collections.singletonList(menuId)).stream()
                .filter(menuAndChildrenBo -> !menuAndChildrenBo.getId().equals(menuId))
                .toList();
        // 2. 构建子菜单 tree_path 的Hash表
        Map<Integer, String> menuTreePathMap = buildMenuTreePathMap(childrenBos);
        // 3. 构建 List<MenuAndChildrenBo>  转换为  List<SysMenu>
        List<SysMenu> collect = childrenBos.stream()
                .map(childrenBo -> SysMenu.builder()
                        .id(childrenBo.getId())
                        .treePath(menuTreePathMap.get(childrenBo.getParentId()))
                        .build()

                ).toList();
        // 4. 修改子元素tree_path
        this.saveOrUpdateBatch(collect);
    }

    /**
     * 构建菜单树形结构 map
     *
     * @return key-> parent_id , value -> newTreePath
     */
    private Map<Integer, String> buildMenuTreePathMap(List<MenuAndChildrenBo> bos) {
        // 1. 整合所有父 Id 用户构建 tree_path
        Set<Integer> collect = bos.stream().map(MenuAndChildrenBo::getParentId).collect(Collectors.toSet());
        // 2. 构建 tree_path 并且转成 Map结构
        return collect.stream()
                .collect(Collectors.toMap(
                                parentId -> parentId,                                 // 构建 Key
                                item -> TreeNodeUtil.generateTreePath(                // 构建 newTreePath
                                        SystemConstants.INTEGER_ROOT_ID,
                                        item,
                                        (parentId) -> this.lambdaQuery()              // 查询出tree_path返回
                                                .select(SysMenu::getTreePath)
                                                .eq(SysMenu::getId, parentId)
                                                .one().getTreePath()
                                )
                        )
                );
    }


    /**
     * 处理对应菜单不同类型的数据
     *
     * @param menuForm 菜单表单
     */
    private void handleMenuType(MenuForm menuForm) {
        String path = menuForm.getPath();
        MenuTypeEnum menuType = menuForm.getType();
        Integer parentId = menuForm.getParentId();
        // 1. 处理对应菜单不同类型的数据
        // 如果是目录
        if (MenuTypeEnum.CATALOG.equals(menuType)) {
            if (SystemConstants.INTEGER_ROOT_ID.equals(parentId) && !path.startsWith("/")) {
                menuForm.setPath("/" + path); // 一级目录需以 / 开头
            }
            menuForm.setComponent("Layout");
            menuForm.setPermission("");
        } else if (MenuTypeEnum.BUTTON.equals(menuType)) {
            menuForm.setKeepAlive(0);
            menuForm.setComponent("");
            menuForm.setHidden(1);
            menuForm.setShowSingleChildren(0);
        }
        // 如果是外链
        else if (MenuTypeEnum.EXT_LINK.equals(menuType)) {
            menuForm.setComponent("");
        }
    }
}

