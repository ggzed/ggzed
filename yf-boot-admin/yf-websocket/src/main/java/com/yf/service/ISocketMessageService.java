package com.yf.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.model.vo.SocketMessageVO;
import com.yf.model.websocket.entity.SocketMessage;
import com.yf.model.websocket.query.SocketMessagePageQuery;

/**
 * websocket消息记录表-SocketMessageService
 *
 * @author YiFei
 * @since 2024-05-23 23:13:50
 */

public interface ISocketMessageService extends IService<SocketMessage> {
    /**
     * 分页查询socket消息
     *
     * @param queryParams 查询参数
     * @return 分页数据
     */
    IPage<SocketMessageVO> getSocketMessagePage(SocketMessagePageQuery queryParams);

    /**
     * 修改Socket消息状态
     *
     * @param senderId 需要修改的 senderId
     * @return 是否修改成功
     */
    boolean updateMessageIsRead(Long senderId);
}

