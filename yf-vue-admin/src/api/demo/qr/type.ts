export interface GenQrCodeForm {
    /**
     * 生成二维码的内容
     */
    content: string;

    /**
     * 二维码宽度（像素）
     */
    width: number;

    /**
     * 二维码高度（像素）
     */
    height: number;

    /**
     * 二维码前景色（十六进制颜色代码）
     */
    foreColorHex: string;

    /**
     * 二维码背景色（十六进制颜色代码）
     */
    backColorHex: string;

    /**
     * 二维码边距
     */
    margin: number;

    /**
     * 二维码纠错级别（如L、M、Q、H）
     */
    errorCorrection: ErrorCorrectionLevel;

    /**
     * Logo文件（可选）
     */
    logo?: File;

    /**
     * Logo缩放系数（可选）
     */
    ratio?: number;
}

/**
 * 二维码纠错级别（如L、M、Q、H）
 */
export enum ErrorCorrectionLevel {
    L = 'L',
    M = 'M',
    Q = 'Q',
    H = 'H',
}
