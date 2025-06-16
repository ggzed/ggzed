/**
 * AiMessagePageQuery, 查询条件
 */
export interface AiMessagePageQuery extends PageQuery {
    /**
     * 角色集合
     */
    role?: string[];

    /**
     * 内容(Markdown格式)
     */
    content?: string;

    /**
     * 发送时间开始
     */
    sendTimeStart?: string;

    /**
     * 发送时间结束
     */
    sendTimeEnd?: string;

}

/**
 * AiMessagePageVO, 展示集合
 */
export interface AiMessagePageVO {
    /**
     * 角色
     */
    role?: string;

    /**
     * 内容(Markdown格式)
     */
    content?: string;

    /**
     * 发送时间
     */
    sendTime?: string;

}

/**
 * AiMessageForm, 表单数据
 */
export interface AiMessageForm {
    /**
     * 会话Id
     */
    conversationId?: string;

    /**
     * 角色
     */
    role?: string;

    /**
     * 内容(Markdown格式)
     */
    content?: string;

}
