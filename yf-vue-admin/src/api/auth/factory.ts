import {LoginParams} from "@/api/auth/type";
import {ThirdPartyEnum} from "@/enums/ThirdPartyEnum";

export type LoginType = 'USERNAME_PASSWORD' | 'PHONE' | 'EMAIL' | ThirdPartyEnum;

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
    static createLoginForm(type: LoginType, params: LoginParams) {
        switch (type) {
            case 'USERNAME_PASSWORD':
                return new UsernamePasswordLogin(params);
            case 'PHONE':
                return new PhoneLogin(params);
            case 'EMAIL':
                return new EmailLogin(params);
            case 'GITEE':
            case 'GITHUB':
                return new OauthLogin(params);
            default:
                throw new Error('Unsupported login type');
        }
    }
}
