package com.yf.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Ai 消息记录表-AiMessagePageVO
 *
 * @author: YiFei
 * @since : 2025年6月10日 10:40:12
 */
@Schema(description = "Ai 消息记录表PageVO")
@Data
public class AiMessagePageVO {

    /**
     * 主键
     */
    @Schema(description = "主键")
    private Long id;

    /**
     * 角色
     */
    @Schema(description = "角色")
    private String role;

    /**
     * 内容(Markdown格式)
     */
    @Schema(description = "内容(Markdown格式)")
    private String content;

    /**
     * 发送时间
     */
    @Schema(description = "发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

}
