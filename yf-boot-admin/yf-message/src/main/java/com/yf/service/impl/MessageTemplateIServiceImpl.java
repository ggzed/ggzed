package com.yf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.mapper.message.MessageTemplateMapper;
import com.yf.model.message.entity.MessageTemplate;
import com.yf.service.IMessageTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 消息模板-MessageTemplateIServiceImpl
 *
 * @author YiFei
 * @since 2024-12-22 12:41:04
 */
@Service("messageTemplateService")
@RequiredArgsConstructor
public class MessageTemplateIServiceImpl extends ServiceImpl<MessageTemplateMapper, MessageTemplate> implements IMessageTemplateService {

}

