export interface OauthPageQuery extends PageQuery {
    /**
     * 用户名
     */
    username?: string;
    /**
     * 第三方平台名称
     */
    platformName?: string;
    /**
     * 绑定时间-开始时间
     */
    startTime?: string;
    /**
     * 绑定时间-结束时间
     */
    endTime?: string;
}

/**
 * OauthPageVO，用户授权分页数据展示
 */
export interface OauthPageVO {
    /**
     * 主键
     */
    id?: string;
    /**
     * 用户名
     */
    username?: string;
    /**
     * 第三方平台提供者
     */
    platformName?: string;
    /**
     * 第三方平台头像
     */
    platformUserAvatar?: string;
    /**
     * 第三方平台用户名
     */
    platformUsername?: string;
    /**
     * 创建时间
     */
    createTime?: string;
}
