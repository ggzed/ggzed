package com.yf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.mapper.message.MessageNotificationMapper;
import com.yf.model.message.entity.MessageNotification;
import com.yf.service.IMessageNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 消息通知-MessageNotificationIServiceImpl
 *
 * @author YiFei
 * @since 2024-12-22 12:41:03
 */
@Service("messageNotificationService")
@RequiredArgsConstructor
public class MessageNotificationIServiceImpl extends ServiceImpl<MessageNotificationMapper, MessageNotification> implements IMessageNotificationService {

}

