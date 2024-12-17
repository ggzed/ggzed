package com.yf.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.model.system.bo.UserBo;
import com.yf.model.system.entity.SysUser;
import com.yf.model.system.form.UserForm;
import com.yf.model.system.form.UserProfileForm;
import com.yf.model.vo.UserInfoVO;
import com.yf.model.vo.UserPageVO;
import com.yf.model.vo.UserProfileInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * 用户转换器
 *
 * @author YiFei
 * @since 2024/5/5 21:33
 */
@Mapper(componentModel = "spring")
public interface UserConverter {
    // sysUser 转 userInfo
    @Mappings({
            @Mapping(target = "userId", source = "id"),
    })
    UserInfoVO user2userInfoVo(SysUser user);

    SysUser userForm2entity(UserForm userForm);

    Page<UserPageVO> pageBo2pageVo(Page<UserBo> userPage);

    UserForm entity2form(SysUser oneSysUser);

    @Mappings({
            @Mapping(target = "userId", source = "id"),
    })
    UserProfileInfoVO user2profileVo(SysUser sysUser);

    SysUser profileForm2entity(UserProfileForm form);
}
