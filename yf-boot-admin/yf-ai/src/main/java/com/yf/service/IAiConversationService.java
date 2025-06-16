package com.yf.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.model.ai.entity.AiConversation;
import com.yf.model.ai.form.AiConversationForm;
import com.yf.model.ai.query.AiConversationPageQuery;
import com.yf.model.vo.AiConversationPageVO;

import java.util.List;

/**
 * AI 会话表-AiConversationService
 *
 * @author: YiFei
 * @since : 2025年6月9日 23:36:21
 */
public interface IAiConversationService extends IService<AiConversation> {

    /**
     * 分页查询AI 会话表
     *
     * @param queryParams 查询参数
     * @return AI 会话表分页数据
     */
    IPage<AiConversationPageVO> getAiConversationPage(AiConversationPageQuery queryParams);

    /**
     * 删除AI 会话表
     *
     * @param ids AI 会话表id集合
     * @return 是否删除成功
     */
    boolean deleteAiConversation(List<Long> ids);

    /**
     * AI 会话表表单数据
     *
     * @param id AI 会话表主键
     * @return AI 会话表表单数据
     */
    AiConversationForm getAiConversationForm(Long id);

    /**
     * 修改AI 会话表
     *
     * @param id                 AI 会话表主键
     * @param aiConversationForm AI 会话表表单
     * @return 是否修改成功
     */
    boolean updateAiConversation(Long id, AiConversationForm aiConversationForm);
}
