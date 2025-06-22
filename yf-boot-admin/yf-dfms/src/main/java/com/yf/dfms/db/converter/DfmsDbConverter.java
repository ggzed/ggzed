package com.yf.dfms.db.converter;

import com.yf.dfms.db.model.entity.DfmsDb;
import com.yf.dfms.db.model.form.DfmsDbForm;
import com.yf.dfms.db.model.vo.DfmsDbPageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * 数据库信息-DfmsDbConverter
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:42
 */
@Mapper(componentModel = "spring" )
public interface DfmsDbConverter {

    Page<DfmsDbPageVO> page2pageVO(Page<DfmsDb> page);
    
    DfmsDb form2entity(DfmsDbForm dfmsDbForm);

    DfmsDbForm entity2form(DfmsDb dfmsDb);
}
