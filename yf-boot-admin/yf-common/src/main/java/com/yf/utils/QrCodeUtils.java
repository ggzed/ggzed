package com.yf.utils;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

/**
 * 二维码生成工具类
 *
 * @author yiFei
 * @since 2024/1/3 15:25
 */
@Component
@RequiredArgsConstructor
public class QrCodeUtils {

    private final QrConfig qrConfig;

    @SneakyThrows
    public String qrCodeSvgBase64(String content) {
        // 1. 创建字节流，通过字节流生成 base64
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 2. 通过 hutool 生成验证码 SVG 图片 写入 字节流
        QrCodeUtil.generate(content, qrConfig, QrCodeUtil.QR_TYPE_SVG, outputStream);
        // 3. 将字节数组转换为Base64字符串
        String base64Encoded = Base64.encodeBase64String(outputStream.toByteArray());
        // 4. 关闭字节流
        outputStream.close();
        return "data:image/svg+xml;base64," + base64Encoded;
    }

    @SneakyThrows
    public String qrCodeSvgBase64(String content, QrConfig qrConfig) {
        // 1. 创建字节流，通过字节流生成 base64
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 2. 通过 hutool 生成验证码 SVG 图片 写入 字节流
        QrCodeUtil.generate(content, qrConfig, QrCodeUtil.QR_TYPE_SVG, outputStream);
        // 3. 将字节数组转换为Base64字符串
        String base64Encoded = Base64.encodeBase64String(outputStream.toByteArray());
        // 4. 关闭字节流
        outputStream.close();
        return "data:image/svg+xml;base64," + base64Encoded;
    }

}
