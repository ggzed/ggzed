package com.yf.dfms.node.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 节点信息-DfmsNodeForm
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:11
 */
@Data
public class DfmsNodeForm {

    /**
     * 名称
     */
    @Schema(description = "名称")
    @NotBlank
    private String name;

    /**
     * IP地址
     */
    @Schema(description = "IP地址")
    @NotBlank
    private String ip;

    /**
     * 端口
     */
    @Schema(description = "端口")
    @NotBlank
    private String port;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @NotBlank
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码")
    @NotBlank
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
