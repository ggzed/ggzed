package com.yf.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.model.bo.OauthBo;
import com.yf.model.entity.SysOauth;
import com.yf.model.form.OauthForm;
import com.yf.model.vo.OauthPageVO;
import com.yf.model.vo.UserProfileOauthVo;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * OauthConverter
 *
 * @author : YiFei
 * @since : 2024/7/22 22:16
 */
@Mapper(componentModel = "spring")
public interface OauthConverter {
    SysOauth form2entity(OauthForm oauthForm);

    List<UserProfileOauthVo> list2profileOauthVo(List<SysOauth> list);

    Page<OauthPageVO> pageBo2pageVo(Page<OauthBo> userPage);
}
