package com.yf.dfms.db.model.vo;

import com.yf.model.dfms.enums.DfmsDbTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据库信息-DfmsDbPageVO
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:42
 */
@Schema(description = "数据库信息PageVO" )
@Data
public class DfmsDbPageVO {

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
     * 字符集
     */
    @Schema(description = "字符集")
    private String charset;

    /**
     * 排序规则
     */
    @Schema(description = "排序规则")
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
