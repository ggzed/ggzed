package com.yf.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.model.websocket.dto.UserConnectInfo;
import com.yf.model.websocket.query.ChatRoomUserPageQuery;

/**
 * 聊天室服务类
 *
 * @author : YiFei
 * @since : 2024/5/29 17:21
 */
public interface IChatRoomService {
    /**
     * 查询聊天室用户 ( : 应该在本地查询，在缓存中判断是否存在该用户标记为上下线即可 )
     *
     * @param queryParams 查询参数
     * @return 分页数据
     */
    IPage<UserConnectInfo> getRoomUserPage(ChatRoomUserPageQuery queryParams);

    /**
     * 查询聊天室单个用户
     *
     * @param userId 用户ID
     * @return 用户连接信息
     */
    UserConnectInfo getRoomUserOne(Long userId);
}
