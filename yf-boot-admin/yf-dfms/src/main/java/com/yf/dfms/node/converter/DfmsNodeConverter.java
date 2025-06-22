package com.yf.dfms.node.converter;

import com.yf.dfms.node.model.entity.DfmsNode;
import com.yf.dfms.node.model.form.DfmsNodeForm;
import com.yf.dfms.node.model.vo.DfmsNodePageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * 节点信息-DfmsNodeConverter
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:11
 */
@Mapper(componentModel = "spring" )
public interface DfmsNodeConverter {

    Page<DfmsNodePageVO> page2pageVO(Page<DfmsNode> page);
    
    DfmsNode form2entity(DfmsNodeForm dfmsNodeForm);

    DfmsNodeForm entity2form(DfmsNode dfmsNode);
}
