package com.yf.service;

import com.yf.model.vo.OnlineUserVO;

import java.util.List;

/**
 * 在线用户服务
 *
 * @author : YiFei
 * @since : 2024/9/4 19:05
 */
public interface IOnlineUserService {
    /**
     * 在线用户实时数据展示
     *
     * @return 用户数量
     */
    Integer getUserActivityNum();

    /**
     * 获取在线用户
     *
     * @return 所有在线用户集合
     */
    List<OnlineUserVO> getOnlineUserPage();

    /**
     * 踢出在线用户
     *
     * @param userId 用户ID
     */
    boolean kickOutOnlineUser(Long userId);
}
