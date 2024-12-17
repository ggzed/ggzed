package com.yf.configuration;


import com.yf.graphic.model.enums.CaptchaType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.awt.*;

/**
 * 验证码配置
 *
 * @author yiFei
 * @since 2024/3/6 20:58
 */
@Data
@Component
@ConfigurationProperties(prefix = "captcha")
public class CaptchaConfiguration {
    /**
     * 验证码类型
     */
    private CaptchaType type = CaptchaType.GIF;
    /**
     * 验证码超时时间 , 单位 s
     */
    private int timeout = 60;
    /**
     * 验证码宽度，表示生成的验证码图片的宽度，单位为像素。
     */
    private int width = 144;

    /**
     * 验证码高度，表示生成的验证码图片的高度，单位为像素。
     */
    private int height = 48;

    /**
     * 验证码长度，表示生成的验证码字符串的长度，即验证码字符的个数。
     */
    private int length = 4;

    /**
     * 字体名称，表示生成验证码时所采用的字体名称。
     */
    private String fontName = "Serif";

    /**
     * 字体样式，表示生成验证码时所采用的字体样式，如粗体、斜体等。
     * - Font.PLAIN: 普通样式。  0
     * - Font.BOLD: 粗体样式。 1
     * - Font.ITALIC: 斜体样式。 2
     * - Font.BOLD | Font.ITALIC: 粗斜体样式。 3
     */
    private int fontStyle = Font.PLAIN;

    /**
     * 字体大小，表示生成验证码时所采用的字体大小，单位为像素。
     */
    private int fontSize = 16;
}