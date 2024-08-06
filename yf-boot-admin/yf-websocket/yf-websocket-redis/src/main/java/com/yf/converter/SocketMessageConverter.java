package com.yf.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.model.entity.SocketMessage;
import com.yf.model.vo.SocketMessageVO;
import org.mapstruct.Mapper;

/**
 * SocketMessage 转换器
 *
 * @author : YiFei
 * @since : 2024/5/29 18:14
 */
@Mapper(componentModel = "spring")
public interface SocketMessageConverter {
    Page<SocketMessageVO> page2pageVo(Page<SocketMessage> page);
}
