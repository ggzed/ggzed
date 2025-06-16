package com.yf.model.ai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Ai 消息记录表-AiMessage
 *
 * @author: YiFei
 * @since : 2025年6月10日 10:40:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ai 消息记录表")
@TableName("ai_message")
public class AiMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 749523212726L;
    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 会话ID
     */
    @Schema(description = "会话ID")
    private Long conversationId;

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
    private LocalDateTime sendTime;

}
