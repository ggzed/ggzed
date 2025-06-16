package com.yf.tools;

import com.yf.constants.AiToolsConstants;
import com.yf.utils.QrCodeUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * 二维码生成工具类
 *
 * @author : YiFei
 * @since : 2025/5/19 23:46
 */
@Component
@RequiredArgsConstructor
public class QrTools {

    private final QrCodeUtils qrCodeUtils;

    /**
     * @param content 二维码的内容
     * @return base64编码的二维码图片
     */
    @Tool(description = "Generates a QR code image from given content.", returnDirect = true)
    public String generateQRCode(
            @ToolParam(description = "Text content or URL to encode in QR code") String content
    ) {
        return AiToolsConstants.IMAGE_TEMPLATE
                .formatted(qrCodeUtils.qrCodeSvgBase64(content), StringUtils.substring(content, 0, 128));
    }

}
