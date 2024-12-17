package com.yf.converter;

import com.yf.model.generate.bo.SyncGenTableBo;
import com.yf.model.generate.entity.GenTable;
import org.mapstruct.Mapper;

/**
 * GenTable转换器
 *
 * @author : YiFei
 * @since : 2024/6/14 17:28
 */
@Mapper(componentModel = "spring")
public interface GenTableConverter {
    GenTable syncBo2entity(SyncGenTableBo syncGenTableBo);
}
