package com.yf.model.generate.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 同步数据库bo
 *
 * @author : YiFei
 * @since : 2024/6/14 17:27
 */
@Data
public class SyncGenTableBo {
    /**
     * 数据库表名
     */
    private String tableName;
    /**
     * 数据库字段
     */
    private String tableComment;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
