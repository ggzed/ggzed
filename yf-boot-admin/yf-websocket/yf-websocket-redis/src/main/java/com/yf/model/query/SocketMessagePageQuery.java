package com.yf.model.query;

import com.yf.base.BasePageQuery;
import com.yf.model.enums.SocketChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * socket消息分页查询
 *
 * @author : YiFei
 * @since : 2024/5/29 18:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SocketMessagePageQuery extends BasePageQuery {

    @Schema(description = "关键字(消息内容) :目前未做该功能")
    private String keyword;

    @Schema(description = "接收消息的用户id")
    private Long receiverId;

    @Schema(description = "消息所在频道(0:系统频道,1:公共频道...)")
    private SocketChannelEnum channel;

}
