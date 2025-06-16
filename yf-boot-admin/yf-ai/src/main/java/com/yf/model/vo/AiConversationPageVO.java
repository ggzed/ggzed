package com.yf.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI 会话表-AiConversationPageVO
 *
 * @author: YiFei
 * @since : 2025年6月9日 23:36:21
 */
@Schema(description = "AI 会话表PageVO")
@Data
public class AiConversationPageVO {

    /**
     * 主键
     */
    @Schema(description = "主键")
    private Long id;

    /**
     * 会话标题
     */
    @Schema(description = "会话标题")
    private String title;

    /**
     * 最后活动时间（索引，用于排序）
     */
    @Schema(description = "最后活动时间（索引，用于排序）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastActiveTime;

}
