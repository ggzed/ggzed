package com.yf.utils;

import com.yf.models.TesseractOcrModelService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * 使用 Tesseract OCR 进行图像文本识别的工具类。
 *
 * @author : YiFei
 * @since : 2024/7/13 15:41
 */
@Component
@RequiredArgsConstructor
public class TesseractOcrUtils {

    private final TesseractOcrModelService tesseractOcrModelService;

    /**
     * 使用 Tesseract OCR 对上传的图像文件进行文本识别。
     *
     * @param file 上传的图像文件（MultipartFile）。
     * @return 从图像中提取的文本。
     */
    @SneakyThrows
    public String recognizeImage(MultipartFile file) {
        // 将上传的图像文件读取为 BufferedImage
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

        // 使用从服务中获取的 Tesseract OCR 实例执行 OCR
        return tesseractOcrModelService.getTesseract().doOCR(bufferedImage);
    }
}
