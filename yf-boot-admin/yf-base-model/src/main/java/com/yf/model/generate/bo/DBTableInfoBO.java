package com.yf.model.generate.bo;

import lombok.Data;

import java.util.List;

@Data
public class DBTableInfoBO {

    /**
     * 数据库表名
     */
    private String tableName;
    /**
     * 数据库备注
     */
    private String tableComment;

    /**
     * 字段
     */
    private List<DBTableFieldInfoBO> fieldList;
}
