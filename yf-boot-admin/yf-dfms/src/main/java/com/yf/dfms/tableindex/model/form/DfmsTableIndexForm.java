package com.yf.dfms.tableindex.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据表索引信息-DfmsTableIndexForm
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:54
 */
@Data
public class DfmsTableIndexForm {

    /**
     * 索引名
     */
    @Schema(description = "索引名")
    @NotBlank
    private String indexName;

    /**
     * 列
     */
    @Schema(description = "列")
    @NotBlank
    private String columns;

    /**
     * 索引类型
     */
    @Schema(description = "索引类型")
    @NotBlank
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
