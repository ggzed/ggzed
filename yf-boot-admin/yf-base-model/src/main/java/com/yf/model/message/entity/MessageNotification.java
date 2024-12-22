package com.yf.model.message.entity;

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
 * 消息通知-MessageNotification
 *
 * @author YiFei
 * @since 2024-12-22 12:41:03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "消息通知")
@TableName("message_notification")
public class MessageNotification implements Serializable {

    @Serial
    private static final long serialVersionUID = 277141459331447759L;

    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 消息标题
     */
    @Schema(description = "消息标题")
    private String title;

    /**
     * 发送人
     */
    @Schema(description = "发送人")
    private Long senderId;

    /**
     * 接收人
     */
    @Schema(description = "接收人")
    private Long receiverId;

    /**
     * 消息状态('未读', '已读', '已删除', '已忽略')
     */
    @Schema(description = "消息状态('未读', '已读', '已删除', '已忽略')")
    private Integer messageStatus;

    /**
     * 消息模板ID
     */
    @Schema(description = "消息模板ID")
    private Integer messageTemplateId;

    /**
     * 动态标题
     */
    @Schema(description = "动态标题")
    private String dynamicTitle;

    /**
     * 动态主题
     */
    @Schema(description = "动态主题")
    private String dynamicSubject;

    /**
     * 动态内容
     */
    @Schema(description = "动态内容")
    private String dynamicContent;

    /**
     * 预定发送时间 ( 仅用于定时发送 )
     */
    @Schema(description = "预定发送时间 ( 仅用于定时发送 )")
    private LocalDateTime scheduledSendTime;

    /**
     * 实际发送时间
     */
    @Schema(description = "实际发送时间")
    private LocalDateTime actualSendTime;

    /**
     * 消息推送状态('未推送', '已推送', '推送中', '推送失败')
     */
    @Schema(description = "消息推送状态('未推送', '已推送', '推送中', '推送失败')")
    private Integer pushStatus;

    /**
     * 重试次数
     */
    @Schema(description = "重试次数")
    private Integer retryCount;

    /**
     * 发送错误原因
     */
    @Schema(description = "发送错误原因")
    private String errorReason;

}
