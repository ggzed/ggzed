package com.yf.model.system.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yf.model.system.enums.GenderEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * UserBo
 *
 * @author YiFei
 * @since 2024/5/12 17:01
 */
@Data
public class UserBo {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 账户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 性别(1->男；2->女)
     */
    private GenderEnum gender;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态: 1->启用;0->禁用
     */
    private Integer status;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 角色名称，多个使用英文逗号(,)分割
     */
    private String roleNames;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
