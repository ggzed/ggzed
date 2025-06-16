package com.yf.model.ai.query;

import com.yf.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * AI 会话表-AiConversationPageQuery
 *
 * @author: YiFei
 * @since : 2025年6月9日 23:36:21
 */
@Schema(description = "AI 会话表分页查询对象")
@EqualsAndHashCode(callSuper = true)
@Data
public class AiConversationPageQuery extends BasePageQuery {

    /**
     * 会话标题
     */
    @Schema(description = "会话标题")
    private String title;

    /**
     * 最后活动时间（索引，用于排序）Begin
     */
    @Schema(description = "最后活动时间（索引，用于排序）Start")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastActiveTimeBegin;

    /**
     * 最后活动时间（索引，用于排序）End
     */
    @Schema(description = "最后活动时间（索引，用于排序）End")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastActiveTimeEnd;

    /**
     * 创建时间Begin
     */
    @Schema(description = "创建时间Start")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeBegin;

    /**
     * 创建时间End
     */
    @Schema(description = "创建时间End")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeEnd;

}
