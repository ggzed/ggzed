package com.yf.tools;

import cn.hutool.core.bean.BeanUtil;
import com.yf.constants.VvHanConstants;
import com.yf.converter.ToolCallResultDescConverter;
import com.yf.model.dto.HoroscopeResponseDTO;
import com.yf.model.dto.ToolContextDTO;
import com.yf.model.dto.WeatherResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * VvHanTools
 *
 * @author : YiFei
 * @since : 2025/5/19 00:26
 */
@Component
@RequiredArgsConstructor
public class VvHanTools {

    private final RestClient restClient = RestClient.create();

    /**
     * 获取天气信息
     *
     * @param toolContext ToolContext对象，包含IP地址
     * @return WeatherResponseDTO 对象，包含天气信息
     */
    @Tool(description = """
            Retrieves real-time weather data
            Returns comprehensive weather details including:
            - Location information (city)
            - Current weather conditions
            - Temperature metrics
            - Wind speed and direction
            - Air quality index
            - Relevant activity suggestions
            Data source: https://api.vvhan.com/api/weather""",
            resultConverter = ToolCallResultDescConverter.class)
    public WeatherResponseDTO getCurrentWeather(ToolContext toolContext) {
        ToolContextDTO toolContextDTO = BeanUtil.toBean(toolContext.getContext(), ToolContextDTO.class);
        return restClient.get()
                .uri(VvHanConstants.VV_HAN_HOST + VvHanConstants.BaseInfoEnums.WEATHER.getUrl() + "?ip={ip}", toolContextDTO.getIp())
                .retrieve()
                .body(WeatherResponseDTO.class);
    }

    @Tool(description = """
            Retrieves detailed horoscope information for specific zodiac signs.
            Provides comprehensive fortune data including:
            - Daily/Weekly/Monthly forecasts
            - Lucky numbers, colors and compatible constellations
            - Numerical ratings (1-5) and percentage indexes
            - Activity recommendations and warnings
            - Detailed interpretations for love, career, wealth and health
            Data source: https://api.vvhan.com/api/horoscope""",
            resultConverter = ToolCallResultDescConverter.class)
    public HoroscopeResponseDTO.HoroscopeInfoDTO getHoroscopeInfo(
            @ToolParam(description = "Zodiac sign in lowercase English: "
                    + "aries, taurus, gemini, cancer, leo, virgo, "
                    + "libra, scorpio, sagittarius, capricorn, aquarius, pisces")
            String type,
            @ToolParam(description = "Time range for horoscope: "
                    + "today, nextday (tomorrow), week, month")
            String time) {
        // 适配响应内容
        HoroscopeResponseDTO response = restClient.get()
                .uri(VvHanConstants.VV_HAN_HOST + VvHanConstants.HoroscopeEnums.HOROSCOPE.getUrl() + "?type={type}&time={time}", type, time)
                .retrieve()
                .body(HoroscopeResponseDTO.class);

        return (response != null && response.getSuccess()) ? response.getData() : null;
    }

}