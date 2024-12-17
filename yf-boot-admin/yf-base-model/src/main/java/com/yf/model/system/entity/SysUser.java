package com.yf.model.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yf.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户信息表-SysUser
 *
 * @author YiFei
 * @since 2024-04-18 16:59:58
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户信息表")
@TableName("sys_user")
public class SysUser extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 511334958726898391L;

    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 性别(0:未知,1:男;2:女))
     */
    @Schema(description = "性别(0:未知,1:男;2:女))")
    private Integer gender;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID")
    private Integer deptId;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像")
    private String avatar;

    /**
     * 联系方式
     */
    @Schema(description = "联系方式")
    private String phoneNumber;

    /**
     * 用户状态((1:正常;0:禁用))
     */
    @Schema(description = "用户状态((1:正常;0:禁用))")
    private Integer status;

    /**
     * 用户邮箱
     */
    @Schema(description = "用户邮箱")
    private String email;

    /**
     * 逻辑删除标识(0:未删除;1:已删除)
     */
    @Schema(description = "逻辑删除标识(0:未删除;1:已删除)")
    private Integer deleted;

}
