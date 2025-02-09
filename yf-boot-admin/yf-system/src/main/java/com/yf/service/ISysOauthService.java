package com.yf.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.model.system.entity.SysOauth;
import com.yf.model.system.form.OauthForm;
import com.yf.model.system.query.OauthPageQuery;
import com.yf.model.vo.OauthPageVO;

import java.util.List;

/**
 * 用户Oauth信息-SysUserOauthService
 *
 * @author YiFei
 * @since 2024-04-18 16:59:58
 */
public interface ISysOauthService extends IService<SysOauth> {
    /**
     * 第三方登录自动注册用户信息
     */
    Long autoRegisterOauthInfo(OauthForm oauthForm);

    /**
     * 获取用户授权信息分页数据
     *
     * @param queryParams 查询参数
     * @return 分页数据
     */
    IPage<OauthPageVO> getOauthPage(OauthPageQuery queryParams);

    /**
     * 删除用户授权信息
     *
     * @param ids 删除的 id 集合
     * @return 是否删除成功
     */
    boolean deleteOauth(List<Long> ids);

    /**
     * 获取系统支持的第三方授权平台列表
     *
     * @return 第三方平台集合
     */
    List<String> getSupportPlatforms();
}

