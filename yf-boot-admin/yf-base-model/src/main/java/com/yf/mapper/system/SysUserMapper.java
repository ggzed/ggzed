package com.yf.mapper.system;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.model.common.dto.UserAuthInfo;
import com.yf.model.system.bo.UserBo;
import com.yf.model.system.entity.SysUser;
import com.yf.model.system.form.LoginForm;
import com.yf.model.system.query.UserPageQuery;

/**
 * 用户信息表-SysUser
 *
 * @author YiFei
 * @since 2024-04-18 16:59:58
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 查询构建 用户权限信息 对象
     *
     * @param principal 登录信息
     */
    UserAuthInfo getUserAuthInfo(LoginForm principal);

    /**
     * 分页查询用户数据
     *
     * @param page        分页参数
     * @param queryParams 用户查询参数
     * @return 分页结果
     */
    Page<UserBo> getUserPage(Page<UserBo> page, UserPageQuery queryParams);
}

