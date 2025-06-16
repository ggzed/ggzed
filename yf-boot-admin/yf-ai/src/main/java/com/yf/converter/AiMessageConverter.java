package com.yf.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.model.ai.entity.AiMessage;
import com.yf.model.ai.form.AiMessageForm;
import com.yf.model.vo.AiMessagePageVO;
import org.mapstruct.Mapper;

/**
 * Ai 消息记录表-AiMessageConverter
 *
 * @author: YiFei
 * @since : 2025年6月10日 10:40:12
 */
@Mapper(componentModel = "spring")
public interface AiMessageConverter {

    Page<AiMessagePageVO> page2pageVO(Page<AiMessage> page);

    AiMessage form2entity(AiMessageForm aiMessageForm);

    AiMessageForm entity2form(AiMessage aiMessage);
}
