package com.yf.model.message.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.yf.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 消息模板-MessageTemplate
 *
 * @author YiFei
 * @since 2024-12-22 12:41:03
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "消息模板")
@TableName("message_template")
public class MessageTemplate extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 442149713852353700L;

    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;

    /**
     * 模板名
     */
    @Schema(description = "模板名")
    private String name;

    /**
     * 消息标题
     */
    @Schema(description = "消息标题")
    private String title;

    /**
     * 主体
     */
    @Schema(description = "主体")
    private String subject;

    /**
     * 消息内容格式
     */
    @Schema(description = "消息内容格式")
    private String content;

    /**
     * 消息展示类型 ( 'MessageBox', 'Drawer', 'Notification' )
     */
    @Schema(description = "消息展示类型 ( 'MessageBox', 'Drawer', 'Notification' ) ")
    private Integer displayType;

    /**
     * 消息语言（'en'）
     */
    @Schema(description = "消息语言（'en'）")
    private Integer language;

    /**
     * 模板类型 ( 'System' , 'SMS' , 'Email','QQBot','WechatBot')
     */
    @Schema(description = "模板类型 ( 'System' , 'SMS' , 'Email','QQBot','WechatBot')")
    private Object type;

    /**
     * 角色状态(1-正常；0-停用)
     */
    @Schema(description = "角色状态(1-正常；0-停用)")
    private Integer status;

    /**
     * 逻辑删除标识(0:未删除;1:已删除)
     */
    @Schema(description = "逻辑删除标识(0:未删除;1:已删除)")
    private Integer deleted;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    private Long createBy;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改人")
    private Long updateBy;

}
