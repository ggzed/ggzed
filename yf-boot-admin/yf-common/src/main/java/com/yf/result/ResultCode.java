package com.yf.result;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 错误码 （ 符号 + 错误数字 ）
 * I : 普通错误级别 ( 90%以上使用 I 开头 )
 * D : Debug 级别
 * W : 系统警告级别
 * E : 高级错误级别
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ResultCode implements IResultCode, Serializable {

    SUCCESS("0000", "操作成功"),

    AUTH_TOKEN_INVALID("I001", "登录令牌(token)无效"),
    AUTH_ACCESS_UNAUTHORIZED("I002", "抱歉 , 您无访问权限"),
    AUTH_PARAMETER_ERROR("I004", "授权参数错误"),
    AUTH_REGISTER_USER_ERROR("E499", "自动注册失败"),
    AUTH_LOGIN_ERROR("I005", "登录失败"),
    AUTH_CODE_ERROR("I006", "验证码错误"),
    AUTH_CODE_EXPIRED("I006", "验证码过期"),
    AUTH_USER_NOT_LOGIN("I007", "用户未登录"),
    AUTH_USER_ELSEWHERE_LOGIN("I008", "账号在别处登录"),
    AUTH_TOKEN_EXPIRED("I009", "由于账户长时间未操作,自动退出登录"),
    AUTH_LOGIN_TIMEOUT("I010", "登录超时"),
    AUTH_KICK_OUT("I011", "用户被踢出"),
    AUTH_MALICIOUS_LOGIN("W006", "恶意登录"),
    AUTH_USER_INFO_ERROR("W003", "用户信息被删除,请联系管理员"),
    AUTH_USER_NOT_FOUND("I012", "未找到改用户"),
    AUTH_BIND_THIRD_PARTY_ERROR("W009", "绑定第三方平台失败,请联系管理员"),
    AUTH_ALREADY_BIND_ERROR("I018", "账号已经被绑定,请解绑后尝试"),

    USER_NAME_DUPLICATE("I101", "用户名重复"),
    USER_PHONE_NUMBER_DUPLICATE("I102", "手机号重复"),
    USER_EMAIL_DUPLICATE("I103", "邮箱重复"),
    USER_RESET_PASSWORD("I104", "校验密码不一致"),
    USER_RESET_OLD_PASSWORD("I105", "旧密码错误"),
    USER_AUTO_REGISTER("I106", "用户自动注册失败"),
    USER_PASSWORD_LENGTH("I107", "密码长度在 8 ~ 16"),
    USER_UPDATE_USERNAME_TIMES("I108", "7天内只能修改一次用户名"),

    DEMO_USER_OPERATION("I199", "演示用户信息禁止更改,您可以自己注册账号进行操作,或联系管理员生成账号,谢谢!"),

    PHONE_CODE_NOT_SUPPORTED("I363", "手机号验证码发送功能未实现"),

    ROLE_CODE_DUPLICATE("I111", "含有相同角色编码的角色"),
    ROLE_BIND_USER("I112", "有用户绑定被删除角色,请解绑后删除"),
    ROLE_NOT_ASSIGNED("W011", "用户未分配角色，无法登录，请联系管理员"),
    ROLE_ADMIN_NOT_MODIFY("I110", "禁止修改管理员角色信息"),

    DEPT_BIND_USER("I111", "有用户绑定被删除部门,请解绑后删除"),

    DICT_TYPE_DUPLICATE("I112", "含有相同type的字典数据"),

    REQUEST_RESOURCE_NOT_FOUND("I003", "请求资源不存在"),
    REQUEST_OTHER_OPERATION("I998", "数据正在其他人被操作中"),
    REQUEST_MORE_ERROR("I999", "请求太快，请稍后再试"),
    REQUEST_RATE_LIMIT("W001", "您操作太快,请稍后再试"),
    REQUEST_PARAMETER_ERROR("I901", "请求参数错误"),

    SOCKET_REJECT_CONNECT("W010", "socket拒绝连接"),

    AI_NOT_FOUND_ROLE("W020", "AI错误角色"),

    FILE_UPLOAD_ERROR("W199", "文件上传失败"),
    FILE_DELETE_ERROR("W200", "文件删除失败"),
    FILE_DELETE_NOT_FOUND("W201", "删除失败,没有这样的文件或目录"),
    FILE_NOT_FOUND("I404", "No static resource"),
    FILE_VIOLATIONS("I408", "文件内容违规,不符合平台规定(如有异议请联系管理员)"),
    FILE_MAX_UPLOAD_SIZE("I418", "文件大小超过平台阈值"),
    FILE_ANALYZER_ERROR("I409", "文件解析错误,文件可能更改原后缀名"),

    GEN_TABLE_ALREADY_EXIST("I201", "生成表已经存在"),
    GEN_TABLE_NOT_EXIST("I202", "生成表不存在"),
    GEN_TABLE_FIELDS_NOT_EXIST("I203", "生成表字段不存在"),
    GEN_CODE_ZIP_ERROR("W204", "代码生成失败 , 请检查配置是否出错"),
    TEMPLATE_NOT_EXIST("E201", "模板不存在"),

    DB_SERVER_NOT_ENABLED("I500", "数据库服务并未开启"),

    FAIL("E500", "操作失败"),
    SYSTEM_EXECUTION_ERROR("E500", "系统执行错误"),
    ;


    private String code;

    private String msg;

}