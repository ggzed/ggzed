package com.yf.model.websocket.query;

import com.yf.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页查询聊天室用户数据
 *
 * @author : YiFei
 * @since : 2024/5/29 17:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatRoomUserPageQuery extends BasePageQuery {

    @Schema(description = "关键字(用户名)")
    private String keyword;

}
