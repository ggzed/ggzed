import {EChartsOption} from "echarts";
import {AxiosPromise} from "axios";
import request from "@/utils/request";
import {OnlineUserVO} from "@/api/monitor/online-user/type";

const API_BASE = '/online_user';

const API_SUFFIXES = {
    /** 在线用户实时流动 */
    STREAM_SSE: '/user-activity-sse',
    /** kick-out 踢出在线用户 */
    KICK_OUT: '/{userId}/kick-out',
    /** 查询在线用户 */
    PAGE: '/page'
};

export class OnlineUserAPI {

    /**
     * 查询在线用户
     */
    static PAGE = {
        endpoint: `${API_BASE}${API_SUFFIXES.PAGE}`,
        permission: 'monitor:online-user:list',
        request: (): AxiosPromise<OnlineUserVO[]> => {
            return request<OnlineUserVO[]>({
                url: OnlineUserAPI.PAGE.endpoint,
                method: "get"
            });
        }
    }

    /**
     * 踢出在线用户
     */
    static KICK_OUT = {
        endpoint: (userId: string) => {
            return `${API_BASE}${API_SUFFIXES.KICK_OUT.replace("{userId}", userId)}`
        },
        permission: 'monitor:online-user:kick-out',
        request: (userId: string): AxiosPromise<void> => {
            return request<void>({
                url: OnlineUserAPI.KICK_OUT.endpoint(userId),
                method: "delete"
            });
        }
    }

    /**
     * 在线用户实时数据展示
     */
    static STREAM_SSE = {
        endpoint: (token: string) => {
            return `${import.meta.env.VITE_APP_API_URL}${API_BASE}${API_SUFFIXES.STREAM_SSE}?Authorization=${token}`
        },
        permission: 'monitor:online-user:list',
        chartOptions: (): EChartsOption => {
            return {
                title: {
                    text: '在线用户统计',   // 图表标题
                    left: 'center'        // 标题居中显示
                },
                xAxis: {
                    type: 'category',
                    data: ['35s', '30s', '25s', '20s', '15s', '10s', '5s'] // 可以根据需要修改为时间段或其他表示
                },
                yAxis: {
                    type: 'value'
                },
                series: [
                    {
                        data: ["0", "0", "0", "0", "0", "0", "0"],  // 数据
                        type: 'line'
                    }
                ]
            }
        }
    }


}
