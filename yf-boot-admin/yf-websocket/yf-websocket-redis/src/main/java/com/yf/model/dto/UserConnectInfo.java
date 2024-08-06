package com.yf.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户连接信息
 *
 * @author YiFei
 * @since 2024/5/23 18:31
 */
@Data
@Schema(description = "用户连接信息")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserConnectInfo {
    /**
     * 用户Id
     */
    @Schema(description = "用户ID")
    private Long userId;
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;
    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String nickname;
    /**
     * 头像地址
     */
    @Schema(description = "头像地址")
    private String avatar;

    /**
     * 是否在线
     */
    @Schema(description = "是否在线")
    private Boolean online;

    /**
     * 消息数量
     */
    @Schema(description = "未读消息数量")
    private Long unreadMessageCount;

    /**
     * 连接到机器名
     */
    @JsonIgnore
    @Schema(description = "连接到机器名")
    private String machineName;
    /**
     * 连接时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "连接时间")
    private LocalDateTime connectTime;
}
