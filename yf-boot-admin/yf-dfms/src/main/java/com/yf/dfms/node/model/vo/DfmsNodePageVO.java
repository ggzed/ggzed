package com.yf.dfms.node.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 节点信息-DfmsNodePageVO
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:11
 */
@Schema(description = "节点信息PageVO" )
@Data
public class DfmsNodePageVO {

    /**
     * 主键
     */
    @Schema(description = "主键")
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

}
