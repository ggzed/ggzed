/**
 * UserProfileInfoVO，响应数据
 */
export interface UserProfileInfoVO {
    /**
     * 用户ID
     */
    userId?: string;
    /**
     * 用户名
     */
    username?: string;
    /**
     * 头像地址
     */
    avatar?: string;
    /**
     * 创建时间
     */
    createTime?: Date;
    /**
     * 部门信息
     */
    deptName?: string;
    /**
     * 邮箱
     */
    email?: string;
    /**
     * 性别
     */
    gender?: number;
    /**
     * 用户昵称
     */
    nickname?: string;
    /**
     * 第三方授权信息
     */
    oauthInfo?: UserProfileOauthVo[];
    /**
     * 手机号
     */
    phoneNumber?: string;
    /**
     * 角色信息
     */
    roles?: string[];
}

/**
 * UserProfileOauthVo，第三方授权信息
 */
export interface UserProfileOauthVo {
    /**
     * 主键
     */
    id?: string;
    /**
     * 第三方平台提供者
     */
    platformName?: string;
    /**
     * 第三方平台头像
     */
    platformUserAvatar?: string;
    /**
     * 第三方平台唯一标识
     */
    platformUserId?: string;
    /**
     * 第三方平台用户名
     */
    platformUsername?: string;
}

/**
 * UserProfileForm
 */
export interface UserProfileForm {
    /**
     * 性别(0:未知,1:男;2:女))
     */
    gender?: number;
    /**
     * 昵称
     */
    nickname?: string;
    /**
     * 联系方式
     */
    phoneNumber?: string;
    /**
     * 用户邮箱
     */
    email?: string;
}

/**
 * 重置用户密码 , ResetUserPasswordForm
 */
export interface ResetUserPasswordForm {
    /**
     * 校验新密码
     */
    checkPassword?: string;
    /**
     * 新密码
     */
    newPassword?: string;
    /**
     * 旧密码
     */
    oldPassword?: string;
}

