package com.yf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.model.bo.MenuAndChildrenBo;
import com.yf.model.bo.RouteBo;
import com.yf.model.entity.SysMenu;
import com.yf.model.enums.MenuTypeEnum;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 系统菜单-SysMenu
 *
 * @author YiFei
 * @since 2024-04-23 18:43:35
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 查询角色的全部权限
     *
     * @param roles      角色 Code 集合
     * @param menuType   菜单类型
     * @param buttonType 按钮类型
     * @return 权限集合
     */
    Set<String> listPermissions(@Param("roles") Set<String> roles,
                                @Param("menuType") MenuTypeEnum menuType,
                                @Param("buttonType") MenuTypeEnum buttonType);

    /**
     * 菜单集合
     *
     * @param menuTypeEnum 需要排除的菜单类型
     */
    List<RouteBo> listRoutes(@Param("menuTypeEnum") MenuTypeEnum menuTypeEnum);

    /**
     * 查询菜单以及子菜单
     *
     * @param menuIds 需要查询到的id
     * @return 菜单Id
     */
    List<MenuAndChildrenBo> getMenuAndChildrenByIds(@Param("menuIds") List<Integer> menuIds);
}

