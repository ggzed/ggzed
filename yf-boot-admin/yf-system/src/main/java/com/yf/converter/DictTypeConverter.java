package com.yf.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.model.system.entity.SysDictType;
import com.yf.model.system.form.DictTypeForm;
import com.yf.model.vo.DictTypePageVO;
import org.mapstruct.Mapper;

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
}
