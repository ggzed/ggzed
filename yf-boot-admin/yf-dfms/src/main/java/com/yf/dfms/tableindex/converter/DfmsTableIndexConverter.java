package com.yf.dfms.tableindex.converter;

import com.yf.dfms.tableindex.model.entity.DfmsTableIndex;
import com.yf.dfms.tableindex.model.form.DfmsTableIndexForm;
import com.yf.dfms.tableindex.model.vo.DfmsTableIndexPageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * 数据表索引信息-DfmsTableIndexConverter
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:54
 */
@Mapper(componentModel = "spring" )
public interface DfmsTableIndexConverter {

    Page<DfmsTableIndexPageVO> page2pageVO(Page<DfmsTableIndex> page);
    
    DfmsTableIndex form2entity(DfmsTableIndexForm dfmsTableIndexForm);

    DfmsTableIndexForm entity2form(DfmsTableIndex dfmsTableIndex);
}
