import axios, {AxiosPromise} from "axios";
import request from "@/utils/request";
import {EChartsOption} from "echarts";

const API_BASE = '';

const API_SUFFIXES = {
    /** 每日热点数据API */
    HOT_LIST: "https://api.vvhan.com/api/hotlist/all",
    /** 天气情况 */
    WEATHER: "https://api.vvhan.com/api/weather",
    /**  访问趋势 */
    VISIT_TREND: "/log/visit-trend",
    /** 图片接口 */
    IMAGES_API: [
        "https://api.vvhan.com/api/bing?rand=sj", // bing 随机图
        "https://api.vvhan.com/api/wallpaper/views", // 风景图
        "https://api.vvhan.com/api/wallpaper/acg", // 二次元图
        "https://api.vvhan.com/api/wallpaper/pcGirl", // cosplay/福利姬图
        "https://api.vvhan.com/api/wallpaper/mobileGirl", // cosplay/福利姬图
        "https://api.vvhan.com/api/avatar/recommend", // 精选头像
        "https://api.vvhan.com/api/avatar/dm", // 动漫头像
        "https://api.vvhan.com/api/avatar/boy", // 男生头像
        "https://api.vvhan.com/api/avatar/girl", // 女生头像
        "https://api.vvhan.com/api/avatar/niche", // 小众头像
    ],
};

export class HomeAPI {

    static HOT_LIST = {
        endpoint: `${API_SUFFIXES.HOT_LIST}`,
        request: (): AxiosPromise<HotApiVO> => {
            return axios.get<HotApiVO>(HomeAPI.HOT_LIST.endpoint);
        }
    }

    static WEATHER = {
        endpoint: `${API_SUFFIXES.WEATHER}`,
        request: (): AxiosPromise<WeatherData> => {
            return axios.get<WeatherData>(HomeAPI.WEATHER.endpoint);
        }
    }

    static IMAGES_API = {
        endpoint: API_SUFFIXES.IMAGES_API
    }

    static VISIT_TREND = {
        endpoint: `${API_SUFFIXES.VISIT_TREND}`,
        chartOptions: (): EChartsOption => {
            return {
                tooltip: {
                    trigger: "axis",
                },
                legend: {
                    data: ["浏览量(PV)", "用户量(UV)", "访客量(IP)"],
                    bottom: 0,
                },
                grid: {
                    left: "1%",
                    right: "5%",
                    bottom: "10%",
                    containLabel: true,
                },
                xAxis: {
                    type: "category",
                    data: [],
                },
                yAxis: {
                    type: "value",
                    splitLine: {
                        show: true,
                        lineStyle: {
                            type: "dashed",
                        },
                    },
                },
                toolbox: {
                    show: true,
                    feature: {
                        saveAsImage: {show: true}
                    }
                },
                series: [
                    {
                        name: "浏览量(PV)",
                        type: "line",
                        data: [],
                        areaStyle: {
                            color: "rgba(64, 158, 255, 0.1)",
                        },
                        smooth: true,
                        itemStyle: {
                            color: "#4080FF",
                        },
                        lineStyle: {
                            color: "#4080FF",
                        },
                    },
                    {
                        name: "用户量(UV)",
                        type: "line",
                        data: [],
                        areaStyle: {
                            color: "rgba(103, 194, 58, 0.1)",
                        },
                        smooth: true,
                        itemStyle: {
                            color: "#67C23A",
                        },
                        lineStyle: {
                            color: "#67C23A",
                        },
                    }, {
                        name: "访客量(IP)",
                        type: "line",
                        data: [],
                        areaStyle: {
                            color: "rgba(103, 194, 58, 0.1)",
                        },
                        smooth: true,
                        itemStyle: {
                            color: "#67C23A",
                        },
                        lineStyle: {
                            color: "#67C23A",
                        },
                    },
                ],
            }
        },
        request: (startDate: string, endDate: string): AxiosPromise<VisitTrendVO> => {
            return request<VisitTrendVO>({
                url: HomeAPI.VISIT_TREND.endpoint,
                method: "get",
                params: {startDate, endDate},
            });
        }
    }
}
