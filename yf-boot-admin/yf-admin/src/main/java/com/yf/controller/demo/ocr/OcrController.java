package com.yf.controller.demo.ocr;

import com.yf.annotation.*;
import com.yf.constraints.MultipartFileValid;
import com.yf.exception.ServiceException;
import com.yf.model.enums.BusinessTypeEnum;
import com.yf.model.enums.LimitTypeEnum;
import com.yf.model.result.Result;
import com.yf.model.result.ResultCode;
import com.yf.models.TesseractOcrModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.util.concurrent.TimeUnit;

/**
 * Ocr-控制器
 *
 * @author : YiFei
 * @since : 2024/8/20 21:29
 */
@Tag(name = "图片文字识别")
@RestController
@RequestMapping("ocr")
@RequiredArgsConstructor
public class OcrController {

    private final TesseractOcrModelService tesseractOcrModelService;

    @RateLimiters(rateLimiters = {
            @RateLimiter(
                    limitTypeEnum = LimitTypeEnum.IP,
                    rateRules = {
                            @RateRule
                    }
            ),
            @RateLimiter(
                    limitTypeEnum = LimitTypeEnum.USER_ID,
                    rateRules = @RateRule(limit = 60, timeUnit = TimeUnit.MINUTES),
                    addToBlacklist = true
            )
    })
    @Operation(summary = "图片文字识别")
    @OperationLog(title = "图片文字识别", businessType = BusinessTypeEnum.SEARCH, isSaveRequestData = false)
    @PreventDuplicateSubmit
    @PostMapping("/detection")
    public Result<String> ocrDetection(@Validated @MultipartFileValid MultipartFile file) {
        try {
            /*
            图片调整推荐 :
                二值化：将图像转换为黑白，有助于提高对比度。
                去噪：去除图像中的噪声。
                旋转矫正：确保图像中的文本是水平的。
             */
            Tesseract tesseract = tesseractOcrModelService.getTesseract();
            return Result.success(tesseract.doOCR(ImageIO.read(file.getInputStream())));
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FILE_ANALYZER_ERROR);
        }
    }
}
