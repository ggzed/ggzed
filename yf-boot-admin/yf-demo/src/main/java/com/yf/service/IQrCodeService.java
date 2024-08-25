package com.yf.service;

import com.yf.model.form.GenQrCodeForm;

/**
 * 生成二维码服务类
 *
 * @author : YiFei
 * @since : 2024/8/22 16:31
 */
public interface IQrCodeService {
    /**
     * 生成二维码
     *
     * @param genQrCodeForm 生成参数
     * @return base64编码
     */
    String generateQrCode(GenQrCodeForm genQrCodeForm);
}
