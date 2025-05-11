import request from "@/utils/request";
import {AxiosPromise} from "axios";


const API_BASE = '/file';

const API_SUFFIXES = {
    /** 上传文件 */
    UPLOAD: '/upload',
    /** 上传文件 ( 注 : 生产环境不建议使用 ) */
    UNSAFE_UPLOAD: '/unsafe/upload',
};


export class FileAPI {

    static UPLOAD = {
        endpoint: `${API_BASE}${API_SUFFIXES.UPLOAD}`,
        maxFileSize: 20 * 1024 * 1024, // 20M
        allowedFileTypes: ['image/bmp', 'image/png', 'image/jpeg', 'image/gif'],
        request: (savePath: string, file: File): AxiosPromise<string> => {
            // 1. 创建一个FormData对象并附加文件
            const formData = new FormData();
            formData.append('file', file);
            formData.append('savePath', savePath);
            // 2. 请求更改头像
            return request<string>({
                url: FileAPI.UPLOAD.endpoint,
                method: "post",
                data: formData,
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            })
        }
    }

    static UNSAFE_UPLOAD = {
        endpoint: `${API_BASE}${API_SUFFIXES.UNSAFE_UPLOAD}`,
        maxFileSize: 20 * 1024 * 1024, // 20M
        allowedFileTypes: ['image/bmp', 'image/png', 'image/jpeg', 'image/gif'],
        request: (savePath: string, file: File): AxiosPromise<string> => {
            // 1. 创建一个FormData对象并附加文件
            const formData = new FormData();
            formData.append('file', file);
            formData.append('savePath', savePath);
            // 2. 请求更改头像
            return request<string>({
                url: FileAPI.UPLOAD.endpoint,
                method: "post",
                data: formData,
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            })
        }
    }
}
