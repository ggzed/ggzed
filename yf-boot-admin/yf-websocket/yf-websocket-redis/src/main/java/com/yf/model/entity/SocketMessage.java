package com.yf.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yf.model.enums.MessageProviderEnum;
import com.yf.model.enums.ServiceProviderEnum;
import com.yf.model.enums.SocketChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * websocket消息记录表-SocketMessage
 *
 * @author YiFei
 * @since 2024-05-23 23:13:50
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "websocket消息记录表")
@TableName("socket_message")
public class SocketMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = -71752503645326190L;

    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 发送消息的用户id( 0为系统消息 )
     */
    @Schema(description = "发送消息的用户id( 0为系统消息 )")
    private Long senderId;

    /**
     * 接收消息的用户id
     */
    @Schema(description = "接收消息的用户id")
    private Long receiverId;

    /**
     * 消息所在频道(0:系统频道,1:公共频道...)
     */
    @Schema(description = "消息所在频道(0:系统频道,1:公共频道...)")
    private SocketChannelEnum channel;

    /**
     * 消息提供者(0:user,1:system,2:ai....)
     */
    @Schema(description = "消息提供者(0:user,1:system,2:ai....)")
    private MessageProviderEnum messageProvider;

    /**
     * 服务提供者(0:chat_room,1:data_dashboard...)
     */
    @Schema(description = "服务提供者(0:chat_room,1:data_dashboard...)")
    private ServiceProviderEnum serviceProvider;

    /**
     * 是否读取(1：是 , 0 : 否)
     */
    @Schema(description = "是否读取(1：是 , 0 : 否)")
    private Integer isRead;

    /**
     * 消息内容
     */
    @Schema(description = "消息内容")
    private String content;

    /**
     * 是否发送(1: 是，0：否)
     */
    @Schema(description = "是否发送(1: 是，0：否)")
    private Integer isSend;

    /**
     * 错误信息
     */
    @Schema(description = "错误信息")
    private String errorMsg;

    /**
     * 发送时间
     */
    @Schema(description = "发送时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
