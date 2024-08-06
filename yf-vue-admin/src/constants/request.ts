/**
 * 请求常量类
 */
export class RequestConstant {
    /**
     * 请求头
     */
    static readonly Header = {
        AuthorizationPrefix: "Bearer "
    }
    /**
     * 后端返回 code
     */
    static readonly Code = {
        /** 成功 */
        SUCCESS: "0000",
        /** 令牌无效 */
        AUTH_TOKEN_INVALID: "I001",
        /** 账号在别处登录 */
        AUTH_USER_ELSEWHERE_LOGIN: "I008",
        /** 登录令牌过期 */
        AUTH_TOKEN_EXPIRED: "I009",
        /** 用户信息被删除 */
        AUTH_USER_INFO_ERROR: "W003",
        /** 系统执行错误 */
        SYSTEM_EXECUTION_ERROR: "E500"
    };

}
