package com.yf.model.log.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.model.log.enums.LogOperatorStatusEnum;
import com.yf.model.log.enums.OperatorTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志-OperateLog
 *
 * @author YiFei
 * @since 2024-04-15 20:51:58
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "操作日志")
@TableName("operate_log")
public class OperateLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 312369238279141964L;

    /**
     * 日志主键
     */
    @Schema(description = "日志主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 模块标题
     */
    @Schema(description = "模块标题")
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除 ...）
     */
    @Schema(description = "业务类型（0其它 1新增 2修改 3删除 ...）")
    private BusinessTypeEnum businessType;

    /**
     * 方法名称
     */
    @Schema(description = "方法名称")
    private String method;

    /**
     * 请求方式
     */
    @Schema(description = "请求方式")
    private String requestMethod;

    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    @Schema(description = "操作类别（0其它 1后台用户 2手机端用户）")
    private OperatorTypeEnum operatorType;

    /**
     * 操作人员
     */
    @Schema(description = "操作人员")
    private String operatorName;

    /**
     * 请求URL
     */
    @Schema(description = "请求URL")
    private String operatorUrl;

    /**
     * 主机地址
     */
    @Schema(description = "主机地址")
    private String operatorIp;

    /**
     * 操作地点
     */
    @Schema(description = "操作地点")
    private String operatorLocation;

    /**
     * 请求参数
     */
    @Schema(description = "请求参数")
    private String operatorParam;

    /**
     * 浏览器信息
     */
    @Schema(description = "浏览器信息")
    private String operatorBrowser;

    /**
     * 操作系统
     */
    @Schema(description = "操作系统")
    private String operatorOs;

    /**
     * 操作时间
     */
    @Schema(description = "操作时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 返回参数
     */
    @Schema(description = "返回参数")
    private String jsonResult;

    /**
     * 操作状态（1正常 ,0异常）
     */
    @Schema(description = "操作状态（1正常 ,0异常）")
    private LogOperatorStatusEnum status;

    /**
     * 错误消息
     */
    @Schema(description = "错误消息")
    private String errorMsg;

    /**
     * 消耗时间
     */
    @Schema(description = "消耗时间")
    private Long costTime;

}
