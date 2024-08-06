package com.yf.configuration;

import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.io.File;
import java.nio.charset.Charset;

/**
 * @author 翼飞
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "qr")
public class QRCodeConfiguration {
    /**
     * 二维码宽度
     */
    private int width = 300;

    /**
     * 二维码高度
     */
    private int height = 300;

    /**
     * 设置前景色（二维码颜色）
     */
    private String foreColorHex = "#000000";

    /**
     * 设置二维码背景色，默认白色，null表示透明
     */
    private String backColorHex = "#FFFFFF";

    /**
     * 二维码边距
     */
    private int margin = 1;

    /**
     * 二维码纠错级别
     */
    private ErrorCorrectionLevel errorCorrection = ErrorCorrectionLevel.H;

    /**
     * 二维码编码字符集
     */
    private String charset = "UTF-8";

    /**
     * 二维码中的 Logo 文件路径
     */
    private String logoFilePath = "";

    /**
     * 二维码中 Logo 的缩放系数
     */
    private int ratio = 5;

    @Bean
    public QrConfig qrConfig() {

        QrConfig qrConfig = QrConfig.create()
                // 设置二维码宽度
                .setWidth(width)
                // 设置二维码高度
                .setHeight(height)
                // 设置前景色（二维码颜色）
                .setForeColor(Color.decode(foreColorHex))
                // 设置二维码背景色，默认白色，null表示透明
                .setBackColor(Color.decode(backColorHex))
                // 设置二维码颜色
                .setMargin(margin)
                // 设置二维码纠错级别
                .setErrorCorrection(errorCorrection)
                // 设置二维码编码字符集
                .setCharset(Charset.forName(charset));

        // logo 为空表示不加载中间图片
        if (logoFilePath != null && !logoFilePath.isEmpty()) {
            // 设置二维码中的 Logo 文件
            qrConfig.setImg(new File(logoFilePath))
                    // 设置二维码中 Logo 的缩放系数
                    .setRatio(ratio);
        }
        return qrConfig;
    }
}