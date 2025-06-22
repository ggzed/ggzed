package com.yf.dfms.db.model.query;

import com.yf.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 数据库信息-DfmsDbPageQuery
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:42
 */
@Schema(description = "数据库信息分页查询对象" )
@EqualsAndHashCode(callSuper = true)
@Data
public class DfmsDbPageQuery extends BasePageQuery {

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
     * 类型(1-pgsql；2-timescale；3-vector；4-geospatial)集合
     */
    @Schema(description = "类型(1-pgsql；2-timescale；3-vector；4-geospatial)集合")
    private Set<Integer> type;

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
     * 索引类型(vector)集合
     */
    @Schema(description = "索引类型(vector)集合")
    private Set<String> indexType;

    /**
     * 状态(1-在线；0-离线)集合
     */
    @Schema(description = "状态(1-在线；0-离线)集合")
    private Set<Integer> status;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private Long createBy;

    /**
    * 创建时间Begin
    */
    @Schema(description = "创建时间Start")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeBegin;

    /**
    * 创建时间End
    */
    @Schema(description = "创建时间End")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeEnd;

    /**
    * 修改人IdBegin
    */
    @Schema(description = "修改人IdStart")
    private Long updateByBegin;

    /**
    * 修改人IdEnd
    */
    @Schema(description = "修改人IdEnd")
    private Long updateByEnd;

    /**
    * 修改时间Begin
    */
    @Schema(description = "修改时间Start")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTimeBegin;

    /**
    * 修改时间End
    */
    @Schema(description = "修改时间End")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTimeEnd;

}
