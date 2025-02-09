import {LoginParams} from "@/api/auth/type";

class UsernamePasswordLogin {
    private username: string | undefined;
    private password: string | undefined;
    private verifyCode: string | undefined;
    private verifyCodeKey: string | undefined;

    constructor(params: LoginParams) {
        this.username = params.username;
        this.password = params.password;
        this.verifyCode = params.verifyCode;
        this.verifyCodeKey = params.verifyCodeKey;
    }
}

class PhoneLogin {
    private phoneNumber: string | undefined;
    private smsCode: string | undefined;

    constructor(params: LoginParams) {
        this.phoneNumber = params.phoneNumber;
        this.smsCode = params.smsCode;
    }
}

class EmailLogin {
    private email: string | undefined;
    private emailCode: string | undefined;

    constructor(params: LoginParams) {
        this.email = params.email;
        this.emailCode = params.emailCode;
    }
}

class OauthLogin {
    private oauth: {
        code?: string;
        auth_code?: string;
        state?: string;
        authorization_code?: string;
        oauth_token?: string;
        oauth_verifier?: string
    } | undefined;

    constructor(params: LoginParams) {
        this.oauth = params.oauth;
    }
}

export class AuthFactory {
    static createLoginForm(type: string, params: LoginParams) {
        switch (type) {
            case 'USERNAME_PASSWORD':
                return new UsernamePasswordLogin(params);
            case 'PHONE':
                return new PhoneLogin(params);
            case 'EMAIL':
                return new EmailLogin(params);
            case "GITEE" :
            case "GITHUB" :
            case "WECHAT" :
            case "WEIBO" :
            case "QQ" :
            case "DINGTALK" :
            case "BAIDU" :
            case "ALIPAY" :
            case "AMAP" :
            case "TENCENT" :
            case "GOOGLE" :
            case "FACEBOOK" :
            case "TWITTER" :
            case "LINKEDIN" :
            case "INSTAGRAM" :
            case "YAHOO" :
            case "MICROSOFT" :
            case "YANDEX" :
            case "OKRU" :
            case "VK" :
            case "TELEGRAM" :
            case "WHATSAPP" :
            case "SKYPE" :
            case "LINE" :
            case "KAKAO" :
            case "NAVER" :
            case "PAYPAL" :
            case "APPLE" :
            case "GOOGLE_MAPS" :
            case "YANDEX_MAPS" :
            case "BAIDU_MAPS" :
            case "TWITTER_MAPS" :
            case "GOOGLE_DRIVE" :
            case "DROPBOX" :
            case "BOX" :
            case "ONEDRIVE" :
            case "MEETUP" :
            case "SLACK" :
            case "DISCORD" :
            case "STEAM" :
            case "SPOTIFY" :
            case "APPLE_MUSIC":
                return new OauthLogin(params);
            default:
                throw new Error('Unsupported login type');
        }
    }
}
