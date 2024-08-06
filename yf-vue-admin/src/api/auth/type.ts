/**
 * 登录参数
 */
export interface LoginParams {
    username?: string;
    phoneNumber?: string;
    email?: string;
    password?: string;
    verifyCode?: string;
    verifyCodeKey?: string;
    smsCode?: string;
    emailCode?: string;
    oauth?: Oauth
}

export interface Oauth {
    code?: string;
    auth_code?: string;
    state?: string;
    authorization_code?: string;
    oauth_token?: string;
    oauth_verifier?: string;
}

/**
 * 刷新token参数
 */
export interface RefreshTokenParams {
    accessToken: string;
    refreshToken: string;
}

/**
 * 验证码响应
 */
export interface CaptchaResult {
    verifyCodeKey: string;
    captchaImgBase64: string;
}

/**
 * 登录响应
 */
export interface LoginResult {
    /** 访问token */
    accessToken?: string;
    /** 刷新token */
    refreshToken?: string;
    /** 过期时间(单位：毫秒) */
    expires?: string;
}
