import {StoreTypeEnum} from "@/store/type";
import {Oauth} from "@/api/auth/type";


export const useOauthStore = defineStore(StoreTypeEnum.Oauth, {
    state: (): {
        type?: string,   // 登录类型
        params?: Oauth,    // 登录参数
        oauthRedirectPath?: string,  // 授权成功后跳转的地址
    } => {
        return {
            type: undefined,
            params: undefined,
            oauthRedirectPath: undefined,
        }
    },
    persist: {
        paths: ['oauthRedirectPath']
    },
    actions: {
        setOauthRedirectUri(oauthRedirectPath: string) {
            this.oauthRedirectPath = oauthRedirectPath;
        },
        setType(type: string) {
            this.type = type;
        },
        setParams(oauth: Oauth) {
            this.params = oauth;
        },
        resetOauth() {
            this.type = undefined;
            this.params = undefined;
            this.oauthRedirectPath = undefined;
        }
    }
})
