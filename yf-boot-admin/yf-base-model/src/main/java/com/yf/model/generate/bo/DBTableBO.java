package com.yf.model.generate.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 数据库表BO
 *
 * @author : YiFei
 * @since : 2025/3/30 17:17
 */
@Data
public class DBTableBO {

    /**
     * 数据库表名
     */
    private String tableName;
    /**
     * 数据库字段
     */
    private String tableComment;
    /**
     * engine
     */
    private String engine;
    /**
     * 数据量
     */
    private Long tableRows;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
