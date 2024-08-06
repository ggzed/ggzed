/**
 * 路由常量类
 */
export class RouteConstant {
    /**
     * 路由守卫白名单
     */
    public static readonly GUARDS_WHITE_LIST: string[] = ["/login", "/oauth-redirect"];
    /**
     * 路由首页路径
     */
    public static readonly HOME_PATH: string = "/home";
    /**
     * 路由登录路径
     */
    public static readonly LOGIN_PATH: string = "/login";
    /**
     * 路由404路径
     */
    public static readonly NOT_FOUND_PATH: string = "/404";
    /**
     * 路由500路径
     */
    public static readonly SERVER_ERROR_PATH: string = "/500";
}



