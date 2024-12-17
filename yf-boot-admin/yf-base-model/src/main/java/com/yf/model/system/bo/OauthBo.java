package com.yf.model.system.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户授权信息BO
 *
 * @author : YiFei
 * @since : 2024/7/26 21:53
 */
@Data
public class OauthBo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

//    /**
//     * 用户昵称
//     */
//    private String nickname;
//
//    /**
//     * 用户手机号
//     */
//    private String phoneNumber;
//
//    /**
//     * 用户邮箱
//     */
//    private String email;

    /**
     * 第三方平台提供者
     */
    private String platformName;

    /**
     * 第三方平台头像
     */
    private String platformUserAvatar;

    /**
     * 第三方平台用户名
     */
    private String platformUsername;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
