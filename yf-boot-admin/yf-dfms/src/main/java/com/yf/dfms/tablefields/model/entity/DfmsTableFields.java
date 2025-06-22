package com.yf.dfms.tablefields.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据表字段信息-DfmsTableFields
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "数据表字段信息" )
@TableName("dfms_table_fields" )
public class DfmsTableFields implements Serializable {

    @Serial
    private static final long serialVersionUID = 750567491974L;
    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;

    /**
     * 所属表ID
     */
    @Schema(description = "所属表ID")
    private Integer tableId;

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

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改人Id
     */
    @Schema(description = "修改人Id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
