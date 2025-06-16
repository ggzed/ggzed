package com.yf.mapper.ai;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.model.ai.entity.AiMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * Ai 消息记录表-AiMessageMapper
 *
 * @author: YiFei
 * @since : 2025年6月9日 23:42:26
 */
@Mapper
public interface AiMessageMapper extends BaseMapper<AiMessage> {

}
