import {AxiosPromise} from "axios";
import request from "@/utils/request";
import {OauthPageQuery, OauthPageVO} from "@/api/system/oauth/type";

const API_BASE = '/oauth';

const API_SUFFIXES = {
    /** 分页获取授权数据 */
    PAGE: '/page',
    /** 删除授权信息 */
    DELETE: '/{oauthIds}',
    /** 获取系统支持的第三方授权 */
    SUPPORT_PLATFORMS: '/support/platforms',
};

export class OauthAPI {
    /**
     * 分页查询用户数据
     * @param query 分页参数
     */
    static PAGE = {
        endpoint: `${API_BASE}${API_SUFFIXES.PAGE}`,
        permission: "system:oauth:list",
        request: (query: OauthPageQuery): AxiosPromise<PageResult<OauthPageVO[]>> => {
            return request<PageResult<OauthPageVO[]>>({
                url: OauthAPI.PAGE.endpoint,
                method: "get",
                params: query,
            })
        }
    };
    /**
     * 删除用户
     * @param oauthIds 用户id集合 ,  以 "," 分隔
     */
    static DELETE = {
        endpoint: (oauthIds: string): string => {
            return `${API_BASE}${API_SUFFIXES.DELETE.replace("{oauthIds}", oauthIds)}`;
        },
        permission: "system:oauth:delete",
        request: (oauthIds: string): AxiosPromise<void> => {
            return request<void>({
                    url: OauthAPI.DELETE.endpoint(oauthIds),
                    method: "delete"
                }
            )
        }
    }
    /**
     * 获取系统支持的第三方授权
     */
    static SUPPORT_PLATFORMS = {
        endpoint: `${API_BASE}${API_SUFFIXES.SUPPORT_PLATFORMS}`,
        request: (): AxiosPromise<string[]> => {
            return request<string[]>({
                    url: OauthAPI.SUPPORT_PLATFORMS.endpoint,
                    method: "get"
                }
            )
        }
    }

}
