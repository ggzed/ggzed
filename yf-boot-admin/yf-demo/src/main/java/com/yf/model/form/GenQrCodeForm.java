package com.yf.model.form;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.yf.file.constraints.MultipartFileValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class GenQrCodeForm {

    @Schema(description = "生成二维码的内容")
    @NotBlank(message = "二维码内容不能为空")
    @Size(max = 2000, message = "二维码内容不建议过长")
    private String content;

    @Schema(description = "二维码宽度（像素）")
    @NotNull(message = "二维码宽度不能为空")
    @Min(value = 100, message = "二维码宽度不能小于100")
    @Max(value = 1000, message = "二维码宽度不能超过1000")
    private Integer width;

    @Schema(description = "二维码高度（像素）")
    @NotNull(message = "二维码高度不能为空")
    @Min(value = 100, message = "二维码高度不能小于100")
    @Max(value = 1000, message = "二维码高度不能超过1000")
    private Integer height;

    @Schema(description = "二维码前景色（十六进制颜色代码）")
    @NotBlank(message = "二维码前景色不能为空")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "二维码前景色应为有效的十六进制颜色代码")
    private String foreColorHex;

    @Schema(description = "二维码背景色（十六进制颜色代码）")
    @NotBlank(message = "二维码背景色不能为空")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "二维码背景色应为有效的十六进制颜色代码")
    private String backColorHex;

    @Schema(description = "二维码边距")
    @NotNull(message = "二维码边距不能为空")
    @Min(value = 0, message = "二维码边距最小为0")
    private Integer margin;

    @Schema(description = "二维码纠错级别（如L、M、Q、H）")
    @NotNull(message = "二维码纠错级别不能为空")
    private ErrorCorrectionLevel errorCorrection;

    @Schema(description = "Logo文件（可选）")
    @MultipartFileValid(required = false)
    private MultipartFile logo;

    @Schema(description = "Logo缩放系数（可选）")
    @Min(value = 5, message = "Logo缩放系数不能小于5")
    @Max(value = 20, message = "Logo缩放系数不能超过20")
    private Integer ratio;
}
