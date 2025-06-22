package com.yf.dfms.tablefields.converter;

import com.yf.dfms.tablefields.model.entity.DfmsTableFields;
import com.yf.dfms.tablefields.model.form.DfmsTableFieldsForm;
import com.yf.dfms.tablefields.model.vo.DfmsTableFieldsPageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * 数据表字段信息-DfmsTableFieldsConverter
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:51
 */
@Mapper(componentModel = "spring" )
public interface DfmsTableFieldsConverter {

    Page<DfmsTableFieldsPageVO> page2pageVO(Page<DfmsTableFields> page);
    
    DfmsTableFields form2entity(DfmsTableFieldsForm dfmsTableFieldsForm);

    DfmsTableFieldsForm entity2form(DfmsTableFields dfmsTableFields);
}
