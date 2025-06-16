package com.yf.model.dto;

import com.yf.annotation.ToolCallResultDesc;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * 用户基础信息
 */
@Data
@Builder
public class UserInfoDTO {

    @ToolCallResultDesc("Username")
    private String username;

    @ToolCallResultDesc("Nickname")
    private String nickname;

    @ToolCallResultDesc("Gender")
    private String gender;

    @ToolCallResultDesc("Phone Number")
    private String phoneNumber;

    @ToolCallResultDesc("Email")
    private String email;

    @ToolCallResultDesc("Roles")
    private Set<String> roles;
}