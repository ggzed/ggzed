package com.yf.model.ai.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * AI 会话表-AiConversation
 *
 * @author: YiFei
 * @since : 2025年6月9日 23:36:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "AI 会话表")
@TableName("ai_conversation")
public class AiConversation implements Serializable {

    @Serial
    private static final long serialVersionUID = 749483381388L;
    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户Id
     */
    @Schema(description = "用户Id")
    private Long userId;

    /**
     * 会话标题
     */
    @Schema(description = "会话标题")
    private String title;

    /**
     * 最后活动时间（索引，用于排序）
     */
    @Schema(description = "最后活动时间（索引，用于排序）")
    private LocalDateTime lastActiveTime;

    /**
     * 逻辑删除标识(0:未删除;1:已删除)
     */
    @Schema(description = "逻辑删除标识(0:未删除;1:已删除)")
    private Integer deleted;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
