package com.yf.dfms.db.model.form;

import com.yf.model.dfms.enums.DfmsDbTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据库信息-DfmsDbForm
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:42
 */
@Data
public class DfmsDbForm {

    /**
     * 名称
     */
    @Schema(description = "名称")
    @NotBlank
    private String name;

    /**
     * 字符集
     */
    @Schema(description = "字符集")
    @NotBlank
    private String charset;

    /**
     * 排序规则
     */
    @Schema(description = "排序规则")
    @NotBlank
    private String collation;

    /**
     * 大小
     */
    @Schema(description = "大小")
    private String size;

    /**
     * 表数量
     */
    @Schema(description = "表数量")
    private Long tables;

    /**
     * 类型(1-pgsql；2-timescale；3-vector；4-geospatial)
     */
    @Schema(description = "类型(1-pgsql；2-timescale；3-vector；4-geospatial)")
    private Integer type;

    /**
     * 保留策略(timescale)
     */
    @Schema(description = "保留策略(timescale)")
    private String retentionPolicy;

    /**
     * 序列数(timescale)
     */
    @Schema(description = "序列数(timescale)")
    private Long sequenceNumber;

    /**
     * 数据点数(timescale)
     */
    @Schema(description = "数据点数(timescale)")
    private Long dataPointsNumber;

    /**
     * 维度(vector)
     */
    @Schema(description = "维度(vector)")
    private String dimension;

    /**
     * 向量数量(vector)
     */
    @Schema(description = "向量数量(vector)")
    private Long vectorNumber;

    /**
     * 索引类型(vector)
     */
    @Schema(description = "索引类型(vector)")
    private String indexType;

    /**
     * 状态(1-在线；0-离线)
     */
    @Schema(description = "状态(1-在线；0-离线)")
    private Integer status;

}
