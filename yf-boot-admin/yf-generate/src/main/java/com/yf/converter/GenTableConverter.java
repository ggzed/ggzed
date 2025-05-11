package com.yf.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.model.generate.bo.DBTableBO;
import com.yf.model.generate.entity.GenTable;
import com.yf.model.generate.form.GenTableForm;
import com.yf.model.vo.DBTableVO;
import org.mapstruct.Mapper;

/**
 * GenTable 转换器
 *
 * @author : YiFei
 * @since : 2025/3/30 16:58
 */
@Mapper(componentModel = "spring")
public interface GenTableConverter {
    Page<DBTableVO> pageBO2VO(IPage<DBTableBO> dbTableBOIPage);

    GenTable form2Entity(GenTableForm form);

    GenTableForm entity2Form(GenTable entity);
}
