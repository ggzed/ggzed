package com.yf.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 访问趋势VO
 *
 * @author : YiFei
 * @since : 2024/9/15 23:04
 */
@Schema(description = "访问趋势VO")
@Data
public class VisitTrendVO {

    @Schema(description = "日期列表")
    private List<String> dates;

    @Schema(description = "浏览量(PV)")
    private List<Integer> pvList;

    @Schema(description = "用户量(UV)")
    private List<Integer> uvList;

    @Schema(description = "访客量(IP)")
    private List<Integer> ipList;
}
