package com.yf.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.model.ai.entity.AiMessage;
import com.yf.model.ai.form.AiMessageForm;
import com.yf.model.ai.query.AiMessagePageQuery;
import com.yf.model.vo.AiMessagePageVO;

import java.util.List;

/**
 * Ai 消息记录表-AiMessageService
 *
 * @author: YiFei
 * @since : 2025年6月10日 10:40:12
 */
public interface IAiMessageService extends IService<AiMessage> {

    /**
     * 分页查询Ai 消息记录表
     *
     * @param queryParams 查询参数
     * @return Ai 消息记录表分页数据
     */
    IPage<AiMessagePageVO> getAiMessagePage(Long conversationId, AiMessagePageQuery queryParams);

    /**
     * 删除Ai 消息记录表
     *
     * @param ids Ai 消息记录表id集合
     * @return 是否删除成功
     */
    boolean deleteAiMessage(List<Long> ids);

    /**
     * Ai 消息记录表表单数据
     *
     * @param id Ai 消息记录表主键
     * @return Ai 消息记录表表单数据
     */
    AiMessageForm getAiMessageForm(Long id);

    /**
     * 保存Ai 消息记录表
     *
     * @param aiMessageForm Ai 消息记录表表单
     * @return Ai 消息记录表主键
     */
    Long saveAiMessage(AiMessageForm aiMessageForm);

    /**
     * 修改Ai 消息记录表
     *
     * @param id            Ai 消息记录表主键
     * @param aiMessageForm Ai 消息记录表表单
     * @return 是否修改成功
     */
    boolean updateAiMessage(Long id, AiMessageForm aiMessageForm);
}
