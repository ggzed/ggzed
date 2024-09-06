package com.yf.service.impl;

import com.yf.constants.RedisKeyConstants;
import com.yf.converter.OnlineUserConverter;
import com.yf.model.vo.OnlineUserVO;
import com.yf.model.vo.UserInfoVO;
import com.yf.service.IOnlineUserService;
import com.yf.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 在线用户服务实现类
 *
 * @author : YiFei
 * @since : 2024/9/4 19:05
 */
@Service
@RequiredArgsConstructor
public class OnlineUserServiceImpl implements IOnlineUserService {

    private final RedisUtil redisUtil;
    private final OnlineUserConverter onlineUserConverter;

    /**
     * 获取用户在线数量
     *
     * @return 用户数量
     */
    @Override
    public Integer getUserActivityNum() {
        /*
           获取 tokens 数量
           注意使用 KEYS 为实时统计的缺点 :
                阻塞性：KEYS 命令会遍历所有键并返回匹配模式的所有键，这在键数量很大时会对 Redis 造成阻塞，影响其他操作的执行。
                内存消耗：KEYS 命令会返回所有匹配的键，可能会导致内存压力增加，特别是在大规模数据集上。
         */
        return redisUtil.keys(RedisKeyConstants.USER_TOKEN_CACHE_PREFIX + "*").size();
    }

    /**
     * 获取在线用户
     *
     * @return 所有在线用户集合
     */
    @Override
    public List<OnlineUserVO> getOnlineUserPage() {
        // 1. 获取所有用户key
        Collection<String> keys = redisUtil.keys(RedisKeyConstants.SYSTEM_ME_CACHE_PREFIX + "*");
        // 2. 查询所有用户到集合
        List<UserInfoVO> userInfoVOList = redisUtil.getCacheObjects(keys);
        // 3. 转换
        return onlineUserConverter.info2vo(userInfoVOList);
    }

    /**
     * 踢出在线用户
     *
     * @param userId 用户ID
     */
    @Override
    public boolean kickOutOnlineUser(Long userId) {
        // 删除 token
        boolean deletedToken = redisUtil.deleteObject(RedisKeyConstants.USER_TOKEN_CACHE_PREFIX + userId);
        // 删除 权限
        boolean deletedPermission = redisUtil.deleteObject(RedisKeyConstants.USER_PERMISSIONS_CACHE_PREFIX + userId);
        // 删除用户信息
        redisUtil.deleteObject(RedisKeyConstants.SYSTEM_ME_CACHE_PREFIX + userId);
        return deletedToken && deletedPermission;
    }
}
