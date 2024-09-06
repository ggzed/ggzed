package com.yf.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.model.dto.UserAuthInfo;
import com.yf.model.entity.SysUser;
import com.yf.model.form.LoginForm;
import com.yf.model.form.UserForm;
import com.yf.model.query.UserPageQuery;
import com.yf.model.vo.UserInfoVO;
import com.yf.model.vo.UserPageVO;

import java.util.List;

/**
 * 用户信息表-SysUserService
 *
 * @author YiFei
 * @since 2024-04-18 16:59:58
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 分页查询用户
     *
     * @param queryParams 查询参数
     * @return 用户分页数据
     */
    IPage<UserPageVO> getUserPage(UserPageQuery queryParams);

    /**
     * 获取授权信息
     *
     * @param principal 认证主体
     * @return 用户授权信息
     */
    UserAuthInfo getUserAuthInfo(LoginForm principal);

    /**
     * 获取当前登录用户信息
     *
     * @return 当前用户信息
     */
    UserInfoVO getCurrentUserInfo(Long userId);

    /**
     * 新增用户
     *
     * @param userForm 用户表单信息
     * @return 是否新增用户成功
     */
    Long saveUser(UserForm userForm);

    /**
     * 删除用户 : 根据 userIds
     *
     * @return 是否删除成功
     */
    boolean deleteUser(List<Long> userIds);

    /**
     * 修改用户 : 根据 id
     *
     * @param userId   用户id
     * @param userForm 表单
     * @return 是否修改成功
     */
    boolean updateUser(Long userId, UserForm userForm);

    /**
     * 修改用户密码 （管理端）
     *
     * @param userId   用户id
     * @param password 密码
     * @return 是否修改成功
     */
    boolean resetUserPassword(Long userId, String password);

    /**
     * 修改用户状态
     *
     * @param userId 用户Id
     * @param status 状态
     * @return 是否修改成功
     */
    boolean updateUserStatus(Long userId, Boolean status);

    /**
     * 用户表单数据
     *
     * @param userId 用户Id
     * @return 用户表单数据
     */
    UserForm getUserForm(Long userId);

    /**
     * 自动注册用户 : 1. 传入空则自动注册 , 2. 传入 userForm 则 填充唯一字段不为空的数据, 例如 ↓
     *
     * @param userForm 用户表单 -> 只会获取 username , email , phoneNumber
     * @return 是否注册成功
     */
    Long autoRegisterUser(UserForm userForm);

    Long autoRegisterUser();
}

