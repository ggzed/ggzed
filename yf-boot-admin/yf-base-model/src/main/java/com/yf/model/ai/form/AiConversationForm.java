package com.yf.model.ai.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * AI 会话表-AiConversationForm
 *
 * @author: YiFei
 * @since : 2025年6月9日 23:36:21
 */
@Data
public class AiConversationForm {

    /**
     * 会话标题
     */
    @Schema(description = "会话标题")
    private String title;

}
