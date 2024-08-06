package com.yf.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.model.Option;
import com.yf.model.entity.SysRole;
import com.yf.model.form.RoleForm;
import com.yf.model.vo.RolePageVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * 角色转换器
 *
 * @author YiFei
 * @since 2024/5/12 22:36
 */
@Mapper(componentModel = "spring")
public interface RoleConverter {

    List<Option<Integer>> list2options(List<SysRole> list);

    // 提供给 list2Option 使用
    @Mappings({
            @Mapping(target = "label", source = "name")
    })
    Option<Integer> role2option(SysRole role);

    Page<RolePageVO> page2pageVo(Page<SysRole> page);

    SysRole form2entity(RoleForm roleForm);

    RoleForm entity2form(SysRole oneSysRole);
}
