package com.yf.converter;


import com.yf.model.common.Option;
import com.yf.model.system.bo.RouteBo;
import com.yf.model.system.entity.SysMenu;
import com.yf.model.system.form.MenuForm;
import com.yf.model.vo.MenuPageVO;
import com.yf.model.vo.RouteVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * 菜单转换器
 *
 * @author YiFei
 * @since 2024/4/29 21:47
 */
@Mapper(componentModel = "spring")
public interface MenuConverter {
    // 路由 Bo 转 Vo
    List<RouteVO> routeBo2Vo(List<RouteBo> routeBos);

    List<Option<Integer>> list2options(List<SysMenu> list);

    @Mappings(
            @Mapping(target = "label", source = "name")
    )
    Option<Integer> list2option(SysMenu menu);

    List<MenuPageVO> list2vo(List<SysMenu> list);

    SysMenu form2entity(MenuForm menuForm);

    MenuForm entity2form(SysMenu sysMenu);
}
