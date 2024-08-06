package com.yf.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.converter.OauthConverter;
import com.yf.exception.ServiceException;
import com.yf.mapper.SysOauthMapper;
import com.yf.model.bo.OauthBo;
import com.yf.model.entity.SysOauth;
import com.yf.model.form.OauthForm;
import com.yf.model.query.OauthPageQuery;
import com.yf.model.result.ResultCode;
import com.yf.model.vo.OauthPageVO;
import com.yf.service.ISysOauthService;
import com.yf.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户Oauth信息-SysUserOauthIServiceImpl
 *
 * @author YiFei
 * @since 2024-04-18 16:59:58
 */
@Service("sysOauthService")
@RequiredArgsConstructor
public class SysOauthServiceImpl extends ServiceImpl<SysOauthMapper, SysOauth> implements ISysOauthService {

    private final OauthConverter oauthConverter;
    private final ISysUserService userService;

    /**
     * 第三方登录自动注册用户信息
     *
     * @param oauthForm oauth 表单
     * @return 用户id
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Long autoRegisterOauthInfo(OauthForm oauthForm) {
        String platformUserId = oauthForm.getPlatformUserId();
        // 1. 查询当前第三方登录是否绑定用户
        SysOauth sysOauth = this.lambdaQuery()
                .select(SysOauth::getUserId)
                .eq(SysOauth::getPlatformUserId, platformUserId)
                .one();
        if (sysOauth == null) {
            // 1. 未查询则自动注册用户
            Long userId = userService.autoRegisterUser();
            // 2. 保存oauth信息
            boolean saved = this.save(oauthConverter.form2entity(oauthForm));
            // 3. 返回用户id
            if (saved) {
                return userId;
            }
            throw new ServiceException(ResultCode.AUTH_REGISTER_USER_ERROR);
        } else {
            // 查询出当前用户的 id 返回
            return sysOauth.getUserId();
        }
    }

    /**
     * 获取用户授权信息分页数据
     *
     * @param queryParams 查询参数
     * @return 分页数据
     */
    @Override
    public IPage<OauthPageVO> getOauthPage(OauthPageQuery queryParams) {
        // 1. 创建分页对象
        Page<OauthBo> page = new Page<>(queryParams.getPageNum(), queryParams.getPageSize());
        // 2. 分页查询
        Page<OauthBo> userPage = this.baseMapper.getOauthPage(page, queryParams);
        // 3. bo 转 vo
        return oauthConverter.pageBo2pageVo(userPage);
    }

    /**
     * 删除用户授权信息
     *
     * @param ids 删除的 id 集合
     * @return 是否删除成功
     */
    @Override
    public boolean deleteOauth(List<Long> ids) {
        return this.lambdaUpdate()
                .in(SysOauth::getId, ids)
                .remove();
    }
}

