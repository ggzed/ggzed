package com.yf.dfms.table.converter;

import com.yf.dfms.table.model.entity.DfmsTable;
import com.yf.dfms.table.model.form.DfmsTableForm;
import com.yf.dfms.table.model.vo.DfmsTablePageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * 数据表信息-DfmsTableConverter
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:49
 */
@Mapper(componentModel = "spring" )
public interface DfmsTableConverter {

    Page<DfmsTablePageVO> page2pageVO(Page<DfmsTable> page);
    
    DfmsTable form2entity(DfmsTableForm dfmsTableForm);

    DfmsTableForm entity2form(DfmsTable dfmsTable);
}
