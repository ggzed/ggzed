import request from "@/utils/request";
import {AxiosPromise} from "axios";

const API_BASE = '/ocr';

const API_SUFFIXES = {
    /** 图片检测 */
    CHECK: '/detection',
};


export class OcrAPI {
    /**
     * 图片文字识别
     * @param file 图片
     */
    static DETECTION = {
        endpoint: `${API_BASE}${API_SUFFIXES.CHECK}`,
        maxFileSize: 10 * 1024 * 1024, // 10M
        allowedFileTypes: ['image/bmp', 'image/png', 'image/jpeg', 'image/gif'],
        request: (file: File): AxiosPromise<string> => {
            // 1. 文件上传
            const formData = new FormData();
            formData.append('file', file);
            // 2. 请求检测
            return request<string>({
                url: OcrAPI.DETECTION.endpoint,
                method: "post",
                data: formData,
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
        }
    }
}
