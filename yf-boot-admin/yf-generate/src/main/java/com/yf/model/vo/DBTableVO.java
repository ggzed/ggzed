package com.yf.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 可导入数据库表展示
 *
 * @author : YiFei
 * @since : 2025/3/30 16:42
 */
@Data
public class DBTableVO {

    /**
     * 数据库表名
     */
    @Schema(description = "数据库表名")
    private String tableName;
    /**
     * 数据库字段
     */
    @Schema(description = "数据库字段")
    private String tableComment;
    /**
     * engine
     */
    @Schema(description = "engine")
    private String engine;
    /**
     * 数据量
     */
    @Schema(description = "数据量")
    private Long tableRows;
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
