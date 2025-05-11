package com.yf.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.model.common.Option;
import com.yf.model.system.entity.SysDictType;
import com.yf.model.system.form.DictTypeForm;
import com.yf.model.vo.DictTypePageVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * DictType转换器
 *
 * @author YiFei
 * @since 2024/5/21 12:41
 */
@Mapper(componentModel = "spring")
public interface DictTypeConverter {

    Page<DictTypePageVO> page2pageVo(Page<SysDictType> page);

    DictTypeForm entity2form(SysDictType dictType);

    SysDictType form2entity(DictTypeForm dictTypeForm);

    List<Option<String>> entity2options(List<SysDictType> dictTypes);

    @Mappings(
            {@Mapping(target = "id", source = "type"), @Mapping(target = "label", source = "name")}
    )
    Option<String> entity2option(SysDictType dictType);
}
