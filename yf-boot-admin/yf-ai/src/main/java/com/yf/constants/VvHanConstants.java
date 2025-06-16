package com.yf.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * @author : YiFei
 * @since : 2025/5/19 23:17
 */
public interface VvHanConstants {

    String VV_HAN_HOST = "https://api.vvhan.com";

    @Getter
    @AllArgsConstructor
    enum ImageEnums {

        // 风景图片
        @ToolParam(description = "Landscape map")
        WALLPAPER("Landscape map", "/api/wallpaper/views?type=json"),
        // 二次元图片
        @ToolParam(description = "Two-dimensional diagrams")
        ACG("Two-dimensional diagrams", "/api/wallpaper/acg?type=json"),

        ;

        private String label;
        private String url;
    }

    @Getter
    @AllArgsConstructor
    enum AvatarEnums {

        // 动漫头像
        @ToolParam(description = "Anime avatars")
        WALLPAPER("Anime avatars", "/api/avatar/dm?type=json"),

        // 二次元图片
        @ToolParam(description = "Two-dimensional diagrams")
        ACG("Two-dimensional diagrams", "/api/wallpaper/acg?type=json"),

        // 男生头像
        @ToolParam(description = "Boy avatar")
        BOY("Boy avatar", "/api/avatar/boy?type=json"),

        // 女生头像
        @ToolParam(description = "Girl avatar")
        GIRL("Girl avatar", "/api/avatar/girl?type=json"),

        ;

        private String label;
        private String url;
    }

    @Getter
    @AllArgsConstructor
    enum MusicEnums {
        // 热歌
        @ToolParam(description = "Hot song charts")
        WALLPAPER("Hot song charts", "/api/wyMusic/热歌榜?type=json"),

        ;

        private String label;
        private String url;
    }

    @Getter
    @AllArgsConstructor
    enum HoroscopeEnums {
        // 星座运势
        @ToolParam(description = "Constellation analysis")
        HOROSCOPE("Constellation analysis", "/api/horoscope"),

        ;

        private String label;
        private String url;
    }

    @Getter
    @AllArgsConstructor
    enum BaseInfoEnums {
        // 天气
        @ToolParam(description = "Get the weather")
        WEATHER("Get the weather", "/api/weather"),

        ;

        private String label;
        private String url;
    }
}
