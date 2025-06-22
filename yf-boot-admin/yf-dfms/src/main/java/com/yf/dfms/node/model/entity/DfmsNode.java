package com.yf.dfms.node.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 节点信息-DfmsNode
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "节点信息" )
@TableName("dfms_node" )
public class DfmsNode implements Serializable {

    @Serial
    private static final long serialVersionUID = 750567451161L;
    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;

    /**
     * IP地址
     */
    @Schema(description = "IP地址")
    private String ip;

    /**
     * 端口
     */
    @Schema(description = "端口")
    private String port;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 角色(1-主节点；2-数据节点)
     */
    @Schema(description = "角色(1-主节点；2-数据节点)")
    private Integer role;

    /**
     * 角色状态(1-在线；0-离线)
     */
    @Schema(description = "角色状态(1-在线；0-离线)")
    private Integer status;

    /**
     * cpu占用
     */
    @Schema(description = "cpu占用")
    private Long cpu;

    /**
     * 内存占用
     */
    @Schema(description = "内存占用")
    private Long memory;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改人Id
     */
    @Schema(description = "修改人Id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
