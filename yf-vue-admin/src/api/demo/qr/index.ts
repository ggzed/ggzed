import request from "@/utils/request";
import {AxiosPromise} from "axios";
import {GenQrCodeForm} from "@/api/demo/qr/type";

const API_BASE = '/qr';

const API_SUFFIXES = {
    /** 图片检测 */
    GEN: '/gen',
};


export class QrAPI {
    /**
     * 生成二维码
     * @param file 图片
     */
    static GEN = {
        endpoint: `${API_BASE}${API_SUFFIXES.GEN}`,
        maxFileSize: 10 * 1024 * 1024, // 10M
        allowedFileTypes: ['image/bmp', 'image/png', 'image/jpeg', 'image/gif'],
        request: (genQrCodeForm: GenQrCodeForm): AxiosPromise<string> => {
            // 1. 生成二维码参数构建
            const formData = new FormData();
            formData.append('content', genQrCodeForm.content);
            formData.append('width', genQrCodeForm.width.toString());
            formData.append('height', genQrCodeForm.height.toString());
            formData.append('foreColorHex', genQrCodeForm.foreColorHex);
            formData.append('backColorHex', genQrCodeForm.backColorHex);
            formData.append('margin', genQrCodeForm.margin.toString());
            formData.append('errorCorrection', genQrCodeForm.errorCorrection);
            // 如果有 logo 文件，则添加到 FormData 中
            if (genQrCodeForm.logo) {
                formData.append('logo', genQrCodeForm.logo);
            }
            // 如果有缩放系数，则添加到 FormData 中
            if (genQrCodeForm.ratio) {
                formData.append('ratio', genQrCodeForm.ratio.toString());
            }
            // 2. 请求检测
            return request<string>({
                url: QrAPI.GEN.endpoint,
                method: "post",
                data: formData,
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
        }
    }
}
