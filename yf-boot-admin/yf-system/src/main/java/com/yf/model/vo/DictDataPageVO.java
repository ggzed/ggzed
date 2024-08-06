package com.yf.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 响应字典数据分页数据
 *
 * @author : YiFei
 * @since : 2024/7/30 1:38
 */
@Data
public class DictDataPageVO {

    private Integer id;

    /**
     * 字典项名称
     */
    @Schema(description = "字典项名称")
    private String name;

    /**
     * 字典项值
     */
    @Schema(description = "字典项值")
    private String value;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 状态(1:正常;0:禁用)
     */
    @Schema(description = "状态(1:正常;0:禁用)")
    private Integer status;

    /**
     * 是否默认(1:是;0:否)
     */
    @Schema(description = "是否默认(1:是;0:否)")
    private Integer defaulted;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
