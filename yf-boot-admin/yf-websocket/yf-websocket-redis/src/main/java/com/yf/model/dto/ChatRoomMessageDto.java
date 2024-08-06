package com.yf.model.dto;

import com.yf.model.enums.MessageProviderEnum;
import com.yf.model.enums.ServiceProviderEnum;
import com.yf.model.enums.SocketChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 发送消息Dto
 *
 * @author YiFei
 * @since 2024/5/23 21:01
 */
@Data
@Builder
@AllArgsConstructor
public class ChatRoomMessageDto{

    @Schema(description = "发送消息的用户id")
    private Long senderId;

    @Schema(description = "接收消息的用户id")
    private Long receiverId;

    @Schema(description = "消息所在频道(0:系统频道,1:公共频道...)")
    private SocketChannelEnum channel;

    @Schema(description = "消息提供者(0:user,1:system,2:ai....)")
    private MessageProviderEnum messageProvider;

    @Schema(description = "服务提供者(0:chat_room,1:data_dashboard...)")
    private ServiceProviderEnum serviceProvider;

    @Schema(description = "消息内容")
    private String content;
}
