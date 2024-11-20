package com.yf.controller.demo.nsfw;

import com.yf.annotation.*;
import com.yf.constraints.MultipartFileValid;
import com.yf.exception.ServiceException;
import com.yf.model.enums.BusinessTypeEnum;
import com.yf.model.enums.LimitTypeEnum;
import com.yf.model.result.Result;
import com.yf.model.result.ResultCode;
import com.yf.utils.NSFWAnalyzerUtils;
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
