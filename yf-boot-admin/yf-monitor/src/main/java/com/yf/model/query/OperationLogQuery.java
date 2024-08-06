package com.yf.model.query;

import com.yf.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 操作日志查询参数
 *
 * @author : YiFei
 * @since : 2024/7/27 16:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OperationLogQuery extends BasePageQuery {
    /**
     * 模块标题
     */
    @Schema(description = "模块标题")
    private String title;
    /**
     * 业务类型（0其它 1新增 2修改 3删除 ...）
     */
    @Schema(description = "业务类型（0其它 1新增 2修改 3删除 ...）")
    private Integer businessType;
    /**
     * 方法名称
     */
    @Schema(description = "方法名称")
    private String method;
    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    @Schema(description = "操作类别（0其它 1后台用户 2手机端用户）")
    private Integer operatorType;
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
     * 操作状态（0正常 1异常）
     */
    @Schema(description = "操作状态（0正常 1异常）")
    private Integer status;
    /**
     * 操作时间区间-开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    /**
     * 操作时间区间-结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    /**
     * 消耗时间区间
     */
    @Schema(description = "消耗区间（大于）, 毫秒为单位")
    private Long costTime;

}
