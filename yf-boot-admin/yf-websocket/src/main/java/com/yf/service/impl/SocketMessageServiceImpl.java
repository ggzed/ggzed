package com.yf.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.converter.SocketMessageConverter;
import com.yf.mapper.websocket.SocketMessageMapper;
import com.yf.model.common.enums.YesNoEnum;
import com.yf.model.vo.SocketMessageVO;
import com.yf.model.websocket.entity.SocketMessage;
import com.yf.model.websocket.enums.SocketChannelEnum;
import com.yf.model.websocket.query.SocketMessagePageQuery;
import com.yf.security.utils.SecurityUtil;
import com.yf.service.ISocketMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * websocket消息记录表-SocketMessageIServiceImpl
 *
 * @author YiFei
 * @since 2024-05-23 23:13:50
 */
@Service("socketMessageService")
@RequiredArgsConstructor
public class SocketMessageServiceImpl extends ServiceImpl<SocketMessageMapper, SocketMessage> implements ISocketMessageService {

    private final SocketMessageConverter messageConverter;

    /**
     * 分页查询socket消息
     *
     * @param queryParams 查询参数
     * @return 分页数据
     */
    @Override
    public IPage<SocketMessageVO> getSocketMessagePage(SocketMessagePageQuery queryParams) {
        // 1. 分页数据查询 （ 有两个聚合索引 ）
        Long senderId = SecurityUtil.getUserId();
        Long receiverId = queryParams.getReceiverId();
        SocketChannelEnum channel = queryParams.getChannel();

        Page<SocketMessage> page = this.lambdaQuery()   //
                .and(senderId != null && receiverId != null, wrapper -> {
                    wrapper.eq(SocketMessage::getSenderId, senderId)                         // 发送者ID
                            .eq(SocketMessage::getReceiverId, receiverId)                    // 接收者ID
                            .or()                                                            // 接收者和发送者都要查询
                            .eq(SocketMessage::getSenderId, receiverId)                      // 接收者ID
                            .eq(SocketMessage::getReceiverId, senderId);                     // 发送者ID
                })                                                                           // 发送者和接受者ID
                .eq(channel != null, SocketMessage::getChannel, channel)             // 对比频道
                .page(queryParams.lambdaMpPage(SocketMessage::getCreateTime, false));   // 时间排序
        // 2. 转换数据返回
        return messageConverter.page2pageVo(page);
    }

    /**
     * 修改Socket消息状态
     *
     * @param senderId 需要修改的 senderId
     * @return 是否修改成功
     */
    @Override
    public boolean updateMessageIsRead(Long senderId) {
        Long receiverId = SecurityUtil.getUserId();
        this.lambdaUpdate()
                .and(wrapper -> wrapper
                        .eq(SocketMessage::getSenderId, senderId)
                        .eq(SocketMessage::getReceiverId, receiverId))      // sender 发送给当前用户的消息
                .eq(SocketMessage::getChannel, SocketChannelEnum.PRIVATE)
                .set(SocketMessage::getIsRead, YesNoEnum.YES)           // 修改为已读
                .update();
        return true;
    }
}

