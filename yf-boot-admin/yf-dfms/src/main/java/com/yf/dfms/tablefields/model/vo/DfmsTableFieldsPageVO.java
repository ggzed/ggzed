package com.yf.dfms.tablefields.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据表字段信息-DfmsTableFieldsPageVO
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:51
 */
@Schema(description = "数据表字段信息PageVO" )
@Data
public class DfmsTableFieldsPageVO {

    /**
     * 主键
     */
    @Schema(description = "主键")
    private Integer id;

    /**
     * 表格展示列名
     */
    @Schema(description = "表格展示列名")
    private String showName;

    /**
     * 数据库列名
     */
    @Schema(description = "数据库列名")
    private String columnName;

    /**
     * 数据库类型
     */
    @Schema(description = "数据库类型")
    private String columnType;

    /**
     * 数据库字段描述
     */
    @Schema(description = "数据库字段描述")
    private String columnComment;

    /**
     * 状态(1-在线；0-离线)
     */
    @Schema(description = "状态(1-在线；0-离线)")
    private Integer status;

}
