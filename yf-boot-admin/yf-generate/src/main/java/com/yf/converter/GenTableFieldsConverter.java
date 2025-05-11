package com.yf.converter;

import com.yf.model.generate.entity.GenTableFields;
import com.yf.model.generate.form.GenTableFieldsForm;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * GenTableFields 转换器
 *
 * @author : YiFei
 * @since : 2025/4/10 12:20
 */
@Mapper(componentModel = "spring")
public interface GenTableFieldsConverter {

    Collection<GenTableFields> forms2Entities(List<GenTableFieldsForm> forms);

    List<GenTableFieldsForm> entities2Forms(List<GenTableFields> list);
}
