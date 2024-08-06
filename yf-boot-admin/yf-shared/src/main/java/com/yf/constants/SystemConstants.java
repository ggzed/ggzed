package com.yf.constants;

/**
 * 系统参数定义
 *
 * @author YiFei
 * @since 2024/4/15 12:41
 */
public interface SystemConstants {
    /**
     * 默认管理员 Code （使用地方 -> DesensitizationSerialize ）
     */
    String ADMIN_CODE = "ADMIN";
    /**
     * 系统默认密码
     */
    String SYSTEM_DEFAULT_PASSWORD = "12345678";
    /**
     * 系统根节点
     */
    Integer INTEGER_ROOT_ID = 0;
    /**
     * 系统根节点
     */
    Long LONG_ROOT_ID = 0L;
    /**
     * 默认部门 : 用户体验部门
     */
    Integer DEFAULT_DEPT = 147;
    /**
     * 默认角色 : 用户体验角色
     */
    Integer DEFAULT_ROLE = 138;
    /**
     * 邮箱头模版
     */
    String EMAIL_CODE_TEMPLATE_SUBJECT = "来自%s发送的验证码";
    /**
     * 默认头像存储位置
     */
    String DEFAULT_AVATAR_SAVE_PATH = "avatar";

}
