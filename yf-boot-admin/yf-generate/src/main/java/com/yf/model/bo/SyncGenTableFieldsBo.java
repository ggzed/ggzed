package com.yf.model.bo;

import lombok.Data;

/**
 * 同步表字段Bo
 *
 * @author : YiFei
 * @since : 2024/6/25 11:20
 */
@Data
public class SyncGenTableFieldsBo {

    private String columnName;
    private String columnComment;
    private String columnType;
    private Integer isPk;
    private Integer isRequired;
    private Integer isIncrement;
    private Integer sort;
    
}
