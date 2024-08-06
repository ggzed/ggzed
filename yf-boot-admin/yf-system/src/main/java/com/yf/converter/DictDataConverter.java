package com.yf.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.model.Option;
import com.yf.model.entity.SysDictData;
import com.yf.model.form.DictDataForm;
import com.yf.model.vo.DictDataPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * DictData转换器
 *
 * @author YiFei
 * @since 2024/5/21 12:41
 */
@Mapper(componentModel = "spring")
public interface DictDataConverter {
    List<Option<Integer>> list2options(List<SysDictData> list);

    @Mappings(
            {@Mapping(target = "id", source = "value"), @Mapping(target = "label", source = "name")}
    )
    Option<Integer> list2option(SysDictData menu);

    Page<DictDataPageVO> page2pageVO(Page<SysDictData> page);

    DictDataForm entity2form(SysDictData dictData);

    SysDictData form2entity(DictDataForm dictDataForm);
}
