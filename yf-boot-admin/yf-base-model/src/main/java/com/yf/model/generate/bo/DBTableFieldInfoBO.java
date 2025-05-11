package com.yf.model.generate.bo;

import lombok.Data;

@Data
public class DBTableFieldInfoBO {
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 字段类型
     */
    private String fieldType;
    /**
     * 字段注释
     */
    private String fieldComment;
    /**
     * 是否主键
     */
    private Boolean isPk;
    /**
     * 是否为必须
     */
    private Boolean isRequired;
    /**
     * 是否自增
     */
    private Boolean isIncrement;
    /**
     * 排序
     */
    private Integer sort;

}