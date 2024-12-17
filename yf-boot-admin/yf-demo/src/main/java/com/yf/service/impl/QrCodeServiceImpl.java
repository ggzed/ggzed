package com.yf.service.impl;

import cn.hutool.extra.qrcode.QrConfig;
import com.yf.exception.ServiceException;
import com.yf.model.form.GenQrCodeForm;
import com.yf.oss.utils.NSFWAnalyzerUtils;
import com.yf.result.ResultCode;
import com.yf.service.IQrCodeService;
import com.yf.utils.QrCodeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 生成二维码服务类-实现
 *
 * @author : YiFei
 * @since : 2024/8/22 16:31
 */
@Service("qrCodeService")
@RequiredArgsConstructor
public class QrCodeServiceImpl implements IQrCodeService {

    private final QrCodeUtils qrCodeUtils;
    private final NSFWAnalyzerUtils nsfwAnalyzerUtils;

    /**
     * 生成二维码
     *
     * @param genQrCodeForm 生成参数
     * @return base64编码
     */
    @Override
    public String generateQrCode(GenQrCodeForm genQrCodeForm) {
        QrConfig qrConfig = QrConfig.create()
                // 设置二维码宽度
                .setWidth(genQrCodeForm.getWidth())
                // 设置二维码高度
                .setHeight(genQrCodeForm.getHeight())
                // 设置前景色（二维码颜色）
                .setForeColor(Color.decode(genQrCodeForm.getForeColorHex()))
                // 设置二维码背景色，默认白色，null表示透明
                .setBackColor(Color.decode(genQrCodeForm.getBackColorHex()))
                // 设置二维码颜色
                .setMargin(genQrCodeForm.getMargin())
                // 设置二维码纠错级别
                .setErrorCorrection(genQrCodeForm.getErrorCorrection())
                // 设置二维码编码字符集
                .setCharset(StandardCharsets.UTF_8);
        // 设置 logo
        MultipartFile logo = genQrCodeForm.getLogo();
        Integer ratio = genQrCodeForm.getRatio();
        if (logo != null && ratio != null) {
            try {
                // 进行 NSFW 内容检测
                if (nsfwAnalyzerUtils.isNsfwFile(logo)) {
                    throw new ServiceException(ResultCode.FILE_VIOLATIONS);
                }

                // 将 MultipartFile 转换为 BufferedImage
                BufferedImage logoImage = ImageIO.read(logo.getInputStream());
                if (logoImage != null) {
                    qrConfig.setImg(logoImage).setRatio(ratio);
                }
            } catch (IOException e) {
                // 处理图像读取异常
                throw new ServiceException(ResultCode.FILE_VIOLATIONS);
            }
        }

        return qrCodeUtils.qrCodeSvgBase64(genQrCodeForm.getContent(), qrConfig);
    }
}
