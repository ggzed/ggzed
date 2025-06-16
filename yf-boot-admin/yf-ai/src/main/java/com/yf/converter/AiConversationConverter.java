package com.yf.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.model.ai.entity.AiConversation;
import com.yf.model.ai.form.AiConversationForm;
import com.yf.model.vo.AiConversationPageVO;
import org.mapstruct.Mapper;

/**
 * AI 会话表-AiConversationConverter
 *
 * @author: YiFei
 * @since : 2025年6月9日 23:36:21
 */
@Mapper(componentModel = "spring")
public interface AiConversationConverter {

    Page<AiConversationPageVO> page2pageVO(Page<AiConversation> page);

    AiConversation form2entity(AiConversationForm aiConversationForm);

    AiConversationForm entity2form(AiConversation aiConversation);
}
