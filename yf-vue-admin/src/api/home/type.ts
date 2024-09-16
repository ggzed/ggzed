/**
 * 天气数据的接口，包含城市、天气信息、空气质量和提示
 */
interface WeatherData {
    success: boolean;
    city: string;
    data: WeatherInfo;
    air: AirQuality;
    tip: string;
}

/**
 * 表示具体的天气信息，如日期、星期、气温、风力等
 */
interface WeatherInfo {
    date: string;
    week: string;
    type: string;
    low: string;
    high: string;
    fengxiang: string;
    fengli: string;
    night: NightWeather;
}

/**
 * 表示夜间的天气情况，包括天气类型、风向和风力
 */
interface NightWeather {
    type: string;
    fengxiang: string;
    fengli: string;
}

/**
 * 表示空气质量数据，包括 AQI 和各种污染物的数值
 */
interface AirQuality {
    aqi: number;
    aqi_level: number;
    aqi_name: string;
    co: string;
    no2: string;
    o3: string;
    pm10: string;
    // pm2.5?: string;
    so2: string;
}

interface HotApiVO {
    data: HotList[]
}

interface HotList {
    name?: string;
    subtitle?: string;
    update_time?: string;
    data?: Array<HotItem>;
}

interface HotItem {
    type?: string;
    title?: string;
    hot?: string;
    url?: string;
    mobil_url?: string;
    index?: number;
}

interface VisitTrendVO {
    dates: Array<string>,
    pvList: Array<number>,
    uvList: Array<number>,
    ipList: Array<number>
}

