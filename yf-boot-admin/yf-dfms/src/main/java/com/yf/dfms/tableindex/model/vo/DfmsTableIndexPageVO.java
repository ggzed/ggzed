package com.yf.dfms.tableindex.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据表索引信息-DfmsTableIndexPageVO
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:54
 */
@Schema(description = "数据表索引信息PageVO" )
@Data
public class DfmsTableIndexPageVO {

    /**
     * 主键
     */
    @Schema(description = "主键")
    private Integer id;

    /**
     * 索引名
     */
    @Schema(description = "索引名")
    private String indexName;

    /**
     * 列
     */
    @Schema(description = "列")
    private String columns;

    /**
     * 索引类型
     */
    @Schema(description = "索引类型")
    private String indexType;

    /**
     * 是否唯一(1-是；0-否)
     */
    @Schema(description = "是否唯一(1-是；0-否)")
    private Integer isOnly;

    /**
     * 状态(1-在线；0-离线)
     */
    @Schema(description = "状态(1-在线；0-离线)")
    private Integer status;

}
