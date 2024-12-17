package com.yf.controller.demo.qr;

import com.yf.log.annotation.OperationLog;
import com.yf.model.form.GenQrCodeForm;
import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
import com.yf.rate_limiting.annotation.RateLimiter;
import com.yf.rate_limiting.annotation.RateLimiters;
import com.yf.rate_limiting.annotation.RateRule;
import com.yf.rate_limiting.model.enums.LimitTypeEnum;
import com.yf.result.Result;
import com.yf.service.IQrCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 二维码
 *
 * @author : YiFei
 * @since : 2024/8/21 0:51
 */
@Tag(name = "二维码")
@RestController
@RequestMapping("qr")
@RequiredArgsConstructor
public class QrCodeController {

    private final IQrCodeService qrCodeService;

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
    @Operation(summary = "生成二维码")
    @OperationLog(title = "生成二维码", businessType = BusinessTypeEnum.TEST, isSaveRequestData = false)
    @PreventDuplicateSubmit
    @PostMapping("/gen")
    public Result<String> generateQrCode(@Validated @ParameterObject GenQrCodeForm genQrCodeForm) {
        return Result.success(qrCodeService.generateQrCode(genQrCodeForm));
    }
}
