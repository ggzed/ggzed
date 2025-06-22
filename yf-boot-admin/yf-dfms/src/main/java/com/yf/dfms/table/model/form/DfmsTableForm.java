package com.yf.dfms.table.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据表信息-DfmsTableForm
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:49
 */
@Data
public class DfmsTableForm {

    /**
     * 数据库表名
     */
    @Schema(description = "数据库表名")
    @NotBlank
    private String tableName;

    /**
     * 数据库表描述
     */
    @Schema(description = "数据库表描述")
    @NotBlank
    private String tableComment;

    /**
     * ddl
     */
    @Schema(description = "ddl")
    @NotBlank
    private String ddl;

    /**
     * 状态(1-在线；0-离线)
     */
    @Schema(description = "状态(1-在线；0-离线)")
    private Integer status;

}
