package com.yf.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 响应字典类型分页数据
 *
 * @author : YiFei
 * @since : 2024/7/30 1:28
 */
@Data
public class DictTypePageVO {

    /**
     * 主键
     */
    @Schema(description = "主键 ")
    private Integer id;

    /**
     * 字典名称
     */
    @Schema(description = "字典名称")
    private String name;

    /**
     * 字典类型
     */
    @Schema(description = "字典类型")
    private String type;

    /**
     * 状态（1正常 0停用）
     */
    @Schema(description = "状态（1正常 0停用）")
    private Integer status;

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
