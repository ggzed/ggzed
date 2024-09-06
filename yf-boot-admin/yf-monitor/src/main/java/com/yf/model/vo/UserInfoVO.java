package com.yf.model.vo;

import lombok.Data;

import java.util.Set;

/**
 * 用户信息Vo
 *
 * @author YiFei
 * @since 2024/5/5 20:48
 */
@Data
public class UserInfoVO {

    private Long userId;

    private String username;

    private String nickname;

    private String phoneNumber;

    private String email;

    private String avatar;

    private Set<String> roles;

    private Set<String> permissions;

}
