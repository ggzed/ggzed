package com.yf.model.query;

import com.yf.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 用户授权信息分页查询参数
 *
 * @author : YiFei
 * @since : 2024/7/26 21:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OauthPageQuery extends BasePageQuery {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "第三方平台名称")
    private String platformName;

    @Schema(description = "绑定时间-开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "绑定时间-结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
