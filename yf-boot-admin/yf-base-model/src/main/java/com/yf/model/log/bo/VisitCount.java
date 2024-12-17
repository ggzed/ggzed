package com.yf.model.log.bo;

import lombok.Data;

/**
 * 访问趋势计数
 *
 * @author : YiFei
 * @since : 2024/9/16 1:05
 */
@Data
public class VisitCount {
    private String date;
    private Integer pvCount;
    private Integer uvCount;
    private Integer ipCount;
}
