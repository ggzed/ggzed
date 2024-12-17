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
 * 用户Oauth信息-SysUserOauth
 *
 * @author YiFei
 * @since 2024-04-18 16:59:58
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户Oauth信息")
@TableName("sys_oauth")
public class SysOauth extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -35370684683200956L;

    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private Long userId;

    /**
     * 第三方平台提供者
     */
    @Schema(description = "第三方平台提供者")
    private String platformName;

    /**
     * 第三方平台唯一标识
     */
    @Schema(description = "第三方平台唯一标识")
    private String platformUserId;

    /**
     * 第三方平台头像
     */
    @Schema(description = "第三方平台头像")
    private String platformUserAvatar;

    /**
     * 第三方平台用户名
     */
    @Schema(description = "第三方平台用户名")
    private String platformUsername;

}
