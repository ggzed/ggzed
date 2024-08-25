import request from "@/utils/request";
import {AxiosPromise} from "axios";

const API_BASE = '/nsfw';

const API_SUFFIXES = {
    /** 图片检测 */
    CHECK: '/check',
};


export class NsfwAPI {
    /**
     * 检测图片
     * @param params 图片
     */
    static CHECK = {
        endpoint: `${API_BASE}${API_SUFFIXES.CHECK}`,
        maxFileSize: 10 * 1024 * 1024, // 10M
        allowedFileTypes: ['image/bmp', 'image/png', 'image/jpeg', 'image/gif'],
        request: (file: File): AxiosPromise<Record<string, string>> => {
            // 1. 文件上传
            const formData = new FormData();
            formData.append('file', file);
            // 2. 请求检测
            return request<Record<string, string>>({
                url: NsfwAPI.CHECK.endpoint,
                method: "post",
                data: formData,
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
        }
    }
}
