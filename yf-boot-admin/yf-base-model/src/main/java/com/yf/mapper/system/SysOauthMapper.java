package com.yf.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.model.system.bo.OauthBo;
import com.yf.model.system.entity.SysOauth;
import com.yf.model.system.query.OauthPageQuery;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Oauth信息-SysUserOauth
 *
 * @author YiFei
 * @since 2024-04-18 16:59:58
 */
public interface SysOauthMapper extends BaseMapper<SysOauth> {

    Page<OauthBo> getOauthPage(@Param("page") Page<OauthBo> page, @Param("queryParams") OauthPageQuery queryParams);
}

