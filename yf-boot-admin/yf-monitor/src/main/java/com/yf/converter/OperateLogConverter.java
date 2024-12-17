package com.yf.converter;

import com.yf.model.log.bo.OperationLogBO;
import com.yf.model.vo.OperationLogVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 操作日志转换器
 *
 * @author : YiFei
 * @since : 2024/12/12 21:33
 */
@Mapper(componentModel = "spring")
public interface OperateLogConverter {
    List<OperationLogVO> bos2vos(List<OperationLogBO> operationLogBOS);
}
