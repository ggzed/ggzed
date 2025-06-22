package com.yf.dfms.tablefields.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据表字段信息-DfmsTableFieldsForm
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:51
 */
@Data
public class DfmsTableFieldsForm {

    /**
     * 表格展示列名
     */
    @Schema(description = "表格展示列名")
    @NotBlank
    private String showName;

    /**
     * 数据库列名
     */
    @Schema(description = "数据库列名")
    @NotBlank
    private String columnName;

    /**
     * 数据库类型
     */
    @Schema(description = "数据库类型")
    @NotBlank
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
