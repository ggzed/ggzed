import request from "@/utils/request";
import {AxiosPromise} from "axios";
import {OperationLogQuery, OperationLogVO} from "@/api/monitor/operation-log/type";

const API_BASE = '/log';

const API_SUFFIXES = {
    /** 获取操作日志分页数据 */
    PAGE: '/page',
    /** 删除日志信息 */
    DELETE: '/{logIds}',
};

export class OperationLogAPI {
    /**
     * 获取分页数据
     */
    static PAGE = {
        endpoint: `${API_BASE}${API_SUFFIXES.PAGE}`,
        permission: 'monitor:operation-log:list',
        request: (params: OperationLogQuery): AxiosPromise<PageResult<OperationLogVO[]>> => {
            return request<PageResult<OperationLogVO[]>>({
                url: OperationLogAPI.PAGE.endpoint,
                method: "get",
                params: params,
            });
        }
    };

    /**
     * 删除日志
     * @param logIds 使用 , 分割的日志ID
     */
    static DELETE = {
        endpoint: (logIds: string): string => {
            return `${API_BASE}${API_SUFFIXES.DELETE.replace("{logIds}", logIds.toString())}`;
        },
        permission: 'monitor:operation-log:delete',
        request: (logIds: string): AxiosPromise<void> => {
            return request<void>({
                url: OperationLogAPI.DELETE.endpoint(logIds),
                method: "delete",
            });
        }
    };
}
