package com.yf.model.ai.query;

import com.yf.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Ai 消息记录表-AiMessagePageQuery
 *
 * @author: YiFei
 * @since : 2025年6月10日 10:40:12
 */
@Schema(description = "Ai 消息记录表分页查询对象")
@EqualsAndHashCode(callSuper = true)
@Data
public class AiMessagePageQuery extends BasePageQuery {

    /**
     * 角色集合
     */
    @Schema(description = "角色集合")
    private Set<String> role;

    /**
     * 内容(Markdown格式)
     */
    @Schema(description = "内容(Markdown格式)")
    private String content;

    /**
     * 发送时间Begin
     */
    @Schema(description = "发送时间Start")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTimeBegin;

    /**
     * 发送时间End
     */
    @Schema(description = "发送时间End")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTimeEnd;

}
