package com.yf.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.converter.AiConversationConverter;
import com.yf.mapper.ai.AiConversationMapper;
import com.yf.model.ai.entity.AiConversation;
import com.yf.model.ai.entity.AiMessage;
import com.yf.model.ai.form.AiConversationForm;
import com.yf.model.ai.query.AiConversationPageQuery;
import com.yf.model.vo.AiConversationPageVO;
import com.yf.security.utils.SecurityUtil;
import com.yf.service.IAiConversationService;
import com.yf.service.IAiMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * AI 会话表-AiConversationServiceImpl
 *
 * @author: YiFei
 * @since : 2025年6月9日 23:36:21
 */
@Service("aiConversationService")
@RequiredArgsConstructor
public class AiConversationServiceImpl extends ServiceImpl<AiConversationMapper, AiConversation> implements IAiConversationService {

    private final AiConversationConverter aiConversationConverter;
    private final IAiMessageService aiMessageService;

    /**
     * 查询AI 会话表
     *
     * @param queryParams 分页参数
     * @return 分页数据
     */
    @Override
    public IPage<AiConversationPageVO> getAiConversationPage(AiConversationPageQuery queryParams) {
        // 1. 分页查询数据
        Page<AiConversation> page = this.getPageData(queryParams);
        // 2. 转换为 vo 后返回
        return aiConversationConverter.page2pageVO(page);
    }

    /**
     * 获取分页数据
     *
     * @param query 查询参数
     * @return Page
     */
    private Page<AiConversation> getPageData(AiConversationPageQuery query) {
        // 1. 查询数据
        return this.lambdaQuery()
                .like(StringUtils.hasText(query.getTitle()), AiConversation::getTitle, query.getTitle())
                .between(query.getLastActiveTimeBegin() != null && query.getLastActiveTimeEnd() != null, AiConversation::getLastActiveTime,
                        query.getLastActiveTimeBegin(),
                        query.getLastActiveTimeEnd())
                .between(query.getCreateTimeBegin() != null && query.getCreateTimeEnd() != null, AiConversation::getCreateTime,
                        query.getCreateTimeBegin(),
                        query.getCreateTimeEnd())
                // 为防止数据泄露，非管理员仅允许管理自己的数据
                .eq(!SecurityUtil.isAdmin(), AiConversation::getUserId, SecurityUtil.getUserId())
                .page(query.toPage());
    }

    /**
     * 获取AI 会话表表单数据
     *
     * @param id AI 会话表表主键
     * @return AI 会话表表单数据
     */
    @Override
    public AiConversationForm getAiConversationForm(Long id) {
        // 1. 查询对应数据
        AiConversation aiConversation = this.lambdaQuery()
                .eq(AiConversation::getId, id)
                .one();
        // 2. entity 2 form
        return aiConversationConverter.entity2form(aiConversation);
    }

    /**
     * 删除AI 会话表
     *
     * @param ids 主键集合
     * @return 是否删除成功
     */
    @Override
    public boolean deleteAiConversation(List<Long> ids) {
        // 删除会话信息
        this.lambdaUpdate()
                .in(AiConversation::getId, ids)
                .remove();
        // 删除关联的消息记录
        aiMessageService.lambdaUpdate()
                .in(AiMessage::getConversationId, ids)
                .remove();
        return true;
    }

    /**
     * 修改AI 会话表信息
     *
     * @param id                 AI 会话表Id
     * @param aiConversationForm AI 会话表表单数据
     * @return 是否修改成功
     */
    @Override
    public boolean updateAiConversation(Long id, AiConversationForm aiConversationForm) {
        // 1. form 转 entity
        AiConversation aiConversation = aiConversationConverter.form2entity(aiConversationForm);
        // 2. 修改数据
        this.lambdaUpdate()
                .eq(AiConversation::getId, id)
                .update(aiConversation);
        return true;
    }
}
