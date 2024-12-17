package com.yf.converter;

import com.yf.model.common.Option;
import com.yf.model.system.entity.SysDept;
import com.yf.model.system.form.DeptForm;
import com.yf.model.vo.DeptPageVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * 部门类转换器
 *
 * @author YiFei
 * @since 2024/5/13 22:56
 */
@Mapper(componentModel = "spring")
public interface DeptConverter {
    List<Option<Integer>> list2options(List<SysDept> list);

    @Mappings(
            @Mapping(target = "label", source = "name")
    )
    Option<Integer> list2option(SysDept dept);

    List<DeptPageVo> list2vo(List<SysDept> list);

    SysDept form2entity(DeptForm deptForm);

    DeptForm entity2form(SysDept sysDept);
}