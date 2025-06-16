package com.yf.model.dto;

import com.yf.annotation.ToolCallResultDesc;
import lombok.Builder;
import lombok.Data;

/**
 * 星座响应
 */
@Data
public class HoroscopeResponseDTO {

    private Boolean success;
    private HoroscopeInfoDTO data;

    @Data
    @Builder
    public static class HoroscopeInfoDTO {
        @ToolCallResultDesc("Horoscope name in Chinese")
        private String title;

        @ToolCallResultDesc("Date of the horoscope forecast")
        private String time;

        @ToolCallResultDesc("Recommended and discouraged activities")
        private TodoDTO todo;

        @ToolCallResultDesc("Numerical fortune ratings (1-5 stars)")
        private FortuneDTO fortune;

        @ToolCallResultDesc("Fortune indexes in percentage")
        private IndexDTO index;

        @ToolCallResultDesc("Short fortune summary")
        private String shortcomment;

        @ToolCallResultDesc("Detailed fortune interpretations")
        private FortuneTextDTO fortunetext;

        @ToolCallResultDesc("Time range type of the horoscope")
        private String type;

        @ToolCallResultDesc("Update date of the horoscope")
        private String uptype;

        @ToolCallResultDesc("Lucky number")
        private String luckynumber;

        @ToolCallResultDesc("Lucky color")
        private String luckycolor;

        @ToolCallResultDesc("Compatible constellation")
        private String luckyconstellation;

        @Data
        @Builder
        public static class TodoDTO {
            @ToolCallResultDesc("Recommended activities")
            private String yi;

            @ToolCallResultDesc("Activities to avoid")
            private String ji;
        }

        @Data
        @Builder
        public static class FortuneDTO {
            @ToolCallResultDesc("Overall fortune rating (1-5)")
            private Integer all;

            @ToolCallResultDesc("Love fortune rating")
            private Integer love;

            @ToolCallResultDesc("Career fortune rating")
            private Integer work;

            @ToolCallResultDesc("Wealth fortune rating")
            private Integer money;

            @ToolCallResultDesc("Health fortune rating")
            private Integer health;
        }

        @Data
        @Builder
        public static class IndexDTO {
            @ToolCallResultDesc("Overall fortune index")
            private String all;

            @ToolCallResultDesc("Love fortune index")
            private String love;

            @ToolCallResultDesc("Career fortune index")
            private String work;

            @ToolCallResultDesc("Wealth fortune index")
            private String money;

            @ToolCallResultDesc("Health fortune index")
            private String health;
        }

        @Data
        @Builder
        public static class FortuneTextDTO {
            @ToolCallResultDesc("Detailed overall fortune interpretation")
            private String all;

            @ToolCallResultDesc("Detailed love fortune interpretation")
            private String love;

            @ToolCallResultDesc("Detailed career fortune interpretation")
            private String work;

            @ToolCallResultDesc("Detailed wealth fortune interpretation")
            private String money;

            @ToolCallResultDesc("Detailed health fortune interpretation")
            private String health;
        }
    }
}