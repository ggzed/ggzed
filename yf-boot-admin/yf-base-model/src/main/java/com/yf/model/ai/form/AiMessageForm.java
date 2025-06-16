package com.yf.model.ai.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Ai 消息记录表-AiMessageForm
 *
 * @author: YiFei
 * @since : 2025年6月10日 10:40:12
 */
@Data
public class AiMessageForm {

    /**
     * 会话Id
     */
    @Schema(description = "会话Id")
    @NotBlank
    private Long conversationId;

    /**
     * 角色
     */
    @Schema(description = "角色")
    @NotBlank
    private String role;

    /**
     * 内容(Markdown格式)
     */
    @Schema(description = "内容(Markdown格式)")
    private String content;

}
