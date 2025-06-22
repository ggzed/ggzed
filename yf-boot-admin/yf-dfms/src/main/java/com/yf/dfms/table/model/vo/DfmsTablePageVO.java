package com.yf.dfms.table.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据表信息-DfmsTablePageVO
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:49
 */
@Schema(description = "数据表信息PageVO" )
@Data
public class DfmsTablePageVO {

    /**
     * 主键
     */
    @Schema(description = "主键")
    private Integer id;

    /**
     * 数据库表名
     */
    @Schema(description = "数据库表名")
    private String tableName;

    /**
     * 数据库表描述
     */
    @Schema(description = "数据库表描述")
    private String tableComment;

    /**
     * ddl
     */
    @Schema(description = "ddl")
    private String ddl;

    /**
     * 状态(1-在线；0-离线)
     */
    @Schema(description = "状态(1-在线；0-离线)")
    private Integer status;

}
