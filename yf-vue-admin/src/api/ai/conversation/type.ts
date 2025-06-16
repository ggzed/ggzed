/**
 * AiConversationPageQuery, 查询条件
 */
export interface AiConversationPageQuery extends PageQuery {
    /**
     * 会话标题
     */
    title?: string;

    /**
     * 最后活动时间（索引，用于排序）开始
     */
    lastActiveTimeStart?: string;

    /**
     * 最后活动时间（索引，用于排序）结束
     */
    lastActiveTimeEnd?: string;

    /**
     * 创建时间开始
     */
    createTimeStart?: string;

    /**
     * 创建时间结束
     */
    createTimeEnd?: string;

}

/**
 * AiConversationPageVO, 展示集合
 */
export interface AiConversationPageVO {
    /**
     * 会话标题
     */
    title?: string;

    /**
     * 最后活动时间（索引，用于排序）
     */
    lastActiveTime?: string;

}

/**
 * AiConversationForm, 表单数据
 */
export interface AiConversationForm {
    /**
     * 会话标题
     */
    title?: string;

}
