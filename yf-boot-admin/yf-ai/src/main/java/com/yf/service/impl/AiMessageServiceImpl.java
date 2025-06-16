package com.yf.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.converter.AiMessageConverter;
import com.yf.mapper.ai.AiMessageMapper;
import com.yf.model.ai.entity.AiMessage;
import com.yf.model.ai.form.AiMessageForm;
import com.yf.model.ai.query.AiMessagePageQuery;
import com.yf.model.vo.AiMessagePageVO;
import com.yf.service.IAiMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Ai 消息记录表-AiMessageServiceImpl
 *
 * @author: YiFei
 * @since : 2025年6月10日 10:40:12
 */
@Service("aiMessageService")
@RequiredArgsConstructor
public class AiMessageServiceImpl extends ServiceImpl<AiMessageMapper, AiMessage> implements IAiMessageService {

    private final AiMessageConverter aiMessageConverter;

    /**
     * 查询Ai 消息记录表
     *
     * @param queryParams 分页参数
     * @return 分页数据
     */
    @Override
    public IPage<AiMessagePageVO> getAiMessagePage(Long conversationId, AiMessagePageQuery queryParams) {
        // 1. 分页查询数据
        Page<AiMessage> page = this.getPageData(conversationId, queryParams);
        // 2. 转换为 vo 后返回
        return aiMessageConverter.page2pageVO(page);
    }

    /**
     * 获取分页数据
     *
     * @param conversationId
     * @param query          查询参数
     * @return Page
     */
    private Page<AiMessage> getPageData(Long conversationId, AiMessagePageQuery query) {
        // 1. 查询数据
        return this.lambdaQuery()
                .eq(AiMessage::getConversationId, conversationId)
                .in(!CollectionUtils.isEmpty(query.getRole()), AiMessage::getRole, query.getRole())
                .like(StringUtils.hasText(query.getContent()), AiMessage::getContent, query.getContent())
                .between(query.getSendTimeBegin() != null && query.getSendTimeEnd() != null, AiMessage::getSendTime,
                        query.getSendTimeBegin(),
                        query.getSendTimeEnd())
                .page(query.toPage());
    }

    /**
     * 获取Ai 消息记录表表单数据
     *
     * @param id Ai 消息记录表表主键
     * @return Ai 消息记录表表单数据
     */
    @Override
    public AiMessageForm getAiMessageForm(Long id) {
        // 1. 查询对应数据
        AiMessage aiMessage = this.lambdaQuery()
                .eq(AiMessage::getId, id)
                .one();
        // 2. entity 2 form
        return aiMessageConverter.entity2form(aiMessage);
    }

    /**
     * 新增Ai 消息记录表
     *
     * @param aiMessageForm Ai 消息记录表表单
     * @return 主键
     */
    @Override
    public Long saveAiMessage(AiMessageForm aiMessageForm) {
        // 1. form 转 entity
        AiMessage aiMessage = aiMessageConverter.form2entity(aiMessageForm);
        // 2. 存储数据
        this.save(aiMessage);
        // 3. 返回主键
        return aiMessage.getId();
    }

    /**
     * 删除Ai 消息记录表
     *
     * @param ids 主键集合
     * @return 是否删除成功
     */
    @Override
    public boolean deleteAiMessage(List<Long> ids) {
        this.lambdaUpdate()
                .in(AiMessage::getId, ids)
                .remove();
        return true;
    }

    /**
     * 修改Ai 消息记录表信息
     *
     * @param id            Ai 消息记录表Id
     * @param aiMessageForm Ai 消息记录表表单数据
     * @return 是否修改成功
     */
    @Override
    public boolean updateAiMessage(Long id, AiMessageForm aiMessageForm) {
        // 1. form 转 entity
        AiMessage aiMessage = aiMessageConverter.form2entity(aiMessageForm);
        // 2. 修改数据
        this.lambdaUpdate()
                .eq(AiMessage::getId, id)
                .eq(AiMessage::getConversationId, aiMessageForm.getConversationId())
                .update(aiMessage);
        return true;
    }
}
