package com.yf.controller.demo.nsfw;

import com.yf.exception.ServiceException;
import com.yf.file.constraints.MultipartFileValid;
import com.yf.file.utils.NSFWAnalyzerUtils;
import com.yf.log.annotation.OperationLog;
import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
import com.yf.rate_limiting.annotation.RateLimiter;
import com.yf.rate_limiting.annotation.RateLimiters;
import com.yf.rate_limiting.annotation.RateRule;
import com.yf.rate_limiting.model.enums.LimitTypeEnum;
import com.yf.result.Result;
import com.yf.result.ResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 图片安全检测-控制器
 *
 * @author : YiFei
 * @since : 2024/8/20 21:33
 */
@Tag(name = "图片安全检测")
@RestController
@RequestMapping("nsfw")
@RequiredArgsConstructor
public class NsfwController {

    private final NSFWAnalyzerUtils nsfwAnalyzerUtils;

    @RateLimiters(rateLimiters = {
            @RateLimiter(
                    limitTypeEnum = LimitTypeEnum.IP,
                    rateRules = {
                            @RateRule
                    }
            ),
            @RateLimiter(
                    limitTypeEnum = LimitTypeEnum.USER_ID,
                    rateRules = @RateRule(limit = 60, timeUnit = TimeUnit.MINUTES)
            )
    })
    @Operation(summary = "图片检测")
    @OperationLog(title = "图片检测", businessType = BusinessTypeEnum.SEARCH, isSaveRequestData = false)
    @PreventDuplicateSubmit
    @PostMapping("/check")
    public Result<Map<String, String>> nsfwCheck(@Validated @MultipartFileValid MultipartFile file) {
        try {
            return Result.success(nsfwAnalyzerUtils.getNsfwPredictions(file));
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FILE_ANALYZER_ERROR);
        }
    }

}
