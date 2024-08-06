package com.yf.converter;

import com.yf.model.bo.SyncGenTableFieldsBo;
import com.yf.model.entity.GenTableFields;
import org.mapstruct.Mapper;

/**
 * 表字段转换器
 *
 * @author : YiFei
 * @since : 2024/6/25 12:17
 */
@Mapper(componentModel = "spring")
public interface GenTableFieldsConverter {
    GenTableFields bo2entity(SyncGenTableFieldsBo tableFieldsBo);
}
