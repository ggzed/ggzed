package com.yf.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.model.dto.UserConnectInfo;
import com.yf.model.entity.SocketMessage;
import com.yf.model.enums.ServiceProviderEnum;
import com.yf.model.enums.SocketChannelEnum;
import com.yf.model.enums.YesNoEnum;
import com.yf.model.query.ChatRoomUserPageQuery;
import com.yf.service.IChatRoomService;
import com.yf.service.ISocketMessageService;
import com.yf.utils.RedisUtil;
import com.yf.utils.SecurityUtil;
import com.yf.websocket.base.BaseWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 聊天室服务实现类
 *
 * @author : YiFei
 * @since : 2024/5/29 17:22
 */
@Service("chatRoomService")
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements IChatRoomService {

    private final RedisUtil redisUtil;
    private final ISocketMessageService socketMessageService;

    /**
     * 查询聊天室用户 ( : 应该在本地查询，在缓存中判断是否存在该用户标记为上下线即可 )
     *
     * @param queryParams 查询参数
     * @return 分页数据
     */
    @Override
    public IPage<UserConnectInfo> getRoomUserPage(ChatRoomUserPageQuery queryParams) {
        Long receiverId = SecurityUtil.getUserId();
        // 1. 缓存中查询所有用户
        String redisKey = ServiceProviderEnum.CHAT_ROOM.getLabel() + BaseWebSocketHandler.ALL;
        Set<Object> allUserId = redisUtil.getCacheZSet(redisKey, 0, -1);
        // 2. 根据缓存中用户ID查询对应数据
        List<UserConnectInfo> userConnectInfoList = new ArrayList<>(allUserId.size());
        for (Object userId : allUserId) {
            // 2.1 缓存查询对象
            UserConnectInfo userConnectInfo = redisUtil.getCacheObject(ServiceProviderEnum.CHAT_ROOM.getLabel() + userId);
            // 2.2 查询未读消息
            Long count = socketMessageService.lambdaQuery()
                    .and(wrapper -> wrapper
                            .eq(SocketMessage::getReceiverId, receiverId)
                            .eq(SocketMessage::getSenderId, userConnectInfo.getUserId())
                    )
                    .eq(SocketMessage::getChannel, SocketChannelEnum.PRIVATE)
                    .eq(SocketMessage::getIsRead, YesNoEnum.NO)
                    .count();
            userConnectInfo.setUnreadMessageCount(count);
            userConnectInfo.setOnline(true);
            // 2.3 添加到 List 中
            userConnectInfoList.add(userConnectInfo);
        }
        // 3. 返回分页用户
        Page<UserConnectInfo> page = new Page<>();
        page.setRecords(userConnectInfoList);
        page.setTotal(allUserId.size());
        return page;
    }

    /**
     * 查询聊天室单个用户
     *
     * @param userId 用户ID
     * @return 用户连接信息
     */
    @Override
    public UserConnectInfo getRoomUserOne(Long userId) {
        Long receiverId = SecurityUtil.getUserId();
        // 1 缓存查询对象
        UserConnectInfo userConnectInfo = redisUtil.getCacheObject(ServiceProviderEnum.CHAT_ROOM.getLabel() + userId);
        // 2 查询未读消息
        Long count = socketMessageService.lambdaQuery()
                .and(wrapper -> wrapper
                        .eq(SocketMessage::getReceiverId, receiverId)
                        .eq(SocketMessage::getSenderId, userConnectInfo.getUserId())
                )
                .eq(SocketMessage::getChannel, SocketChannelEnum.PRIVATE)
                .eq(SocketMessage::getIsRead, YesNoEnum.NO)
                .count();
        userConnectInfo.setUnreadMessageCount(count);
        userConnectInfo.setOnline(true);
        return userConnectInfo;
    }
}
