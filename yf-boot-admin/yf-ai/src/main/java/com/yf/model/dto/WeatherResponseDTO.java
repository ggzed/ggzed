package com.yf.model.dto;

import com.yf.annotation.ToolCallResultDesc;
import lombok.Data;

@Data
public class WeatherResponseDTO {

    @ToolCallResultDesc("City")
    private String city;

    @ToolCallResultDesc("Weather data")
    private WeatherDataDTO data;

    @ToolCallResultDesc("Air quality")
    private AirQualityDTO air;

    @ToolCallResultDesc("Helpful tips")
    private String tip;

    @Data
    public static class WeatherDataDTO {
        @ToolCallResultDesc("Date")
        private String date;

        @ToolCallResultDesc("Day of the week")
        private String week;

        @ToolCallResultDesc("Weather type")
        private String type;

        @ToolCallResultDesc("Lowest temperature")
        private String low;

        @ToolCallResultDesc("Highest temperature")
        private String high;

        @ToolCallResultDesc("Wind direction")
        private String fengxiang;

        @ToolCallResultDesc("Wind strength level")
        private String fengli;

        @ToolCallResultDesc("Nighttime weather")
        private NightWeatherDTO night;
    }

    @Data
    public static class NightWeatherDTO {
        @ToolCallResultDesc("Weather type at night")
        private String type;

        @ToolCallResultDesc("Wind direction at night")
        private String fengxiang;

        @ToolCallResultDesc("Wind strength at night")
        private String fengli;
    }

    @Data
    public static class AirQualityDTO {
        @ToolCallResultDesc("AQI index")
        private int aqi;

        @ToolCallResultDesc("AQI level")
        private int aqi_level;

        @ToolCallResultDesc("Air quality description")
        private String aqi_name;

        @ToolCallResultDesc("Carbon monoxide (CO)")
        private String co;

        @ToolCallResultDesc("Nitrogen dioxide (NO₂)")
        private String no2;

        @ToolCallResultDesc("Ozone (O₃)")
        private String o3;

        @ToolCallResultDesc("PM10 concentration")
        private String pm10;

        @ToolCallResultDesc("PM2.5 concentration")
        private String pm25;

        @ToolCallResultDesc("Sulfur dioxide (SO₂)")
        private String so2;
    }
}