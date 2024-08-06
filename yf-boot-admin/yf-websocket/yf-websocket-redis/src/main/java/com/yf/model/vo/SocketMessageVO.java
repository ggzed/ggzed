package com.yf.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.yf.model.enums.SocketChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息VO
 *
 * @author : YiFei
 * @since : 2024/5/28 11:08
 */
@Data
@Builder
public class SocketMessageVO {

    @Schema(description = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "发送消息的用户id( 0为系统消息 )")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long senderId;

    @Schema(description = "接收消息的用户id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long receiverId;

    @Schema(description = "消息所在频道(0:系统频道,1:公共频道...)")
    private SocketChannelEnum channel;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
