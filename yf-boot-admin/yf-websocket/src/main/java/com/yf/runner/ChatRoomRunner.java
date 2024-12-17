package com.yf.runner;

import com.yf.configuration.SystemConfiguration;
import com.yf.constants.ChatRoomConstant;
import com.yf.constants.RedisKeyConstants;
import com.yf.model.websocket.dto.ChatRoomMessageDto;
import com.yf.utils.RedisUtil;
import com.yf.websocket.subscriber.ChatRoomSubscriber;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 聊天室初始化
 *
 * @author YiFei
 * @since 2024/5/23 20:21
 */
@Component
@RequiredArgsConstructor
public class ChatRoomRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ChatRoomRunner.class);
    private final SystemConfiguration systemConfiguration;
    private final RedisUtil redisUtil;
    private final RedissonClient redissonClient;
    private final ChatRoomSubscriber chatRoomSubscriber;

    @Override
    public void run(String... args) throws Exception {
        log.info("加载 yf-websocket-redis 模块");
        String machineName = systemConfiguration.getMachineName();
        // 1. 存储机器号到redis
        redisUtil.addToCacheSet(RedisKeyConstants.SYSTEM_MACHINE, machineName);
        // 2. 获取聊天室主题
        RTopic topic = redissonClient.getTopic(ChatRoomConstant.CHAT_ROOM + machineName);
        // 3. 订阅事件
        topic.addListener(ChatRoomMessageDto.class, chatRoomSubscriber);
    }
}
