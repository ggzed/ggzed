package com.yf.converter;

import com.yf.model.vo.OnlineUserVO;
import com.yf.model.vo.UserInfoVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 在线用户信息转换
 *
 * @author : YiFei
 * @since : 2024/9/5 10:53
 */
@Mapper(componentModel = "spring")
public interface OnlineUserConverter {
    List<OnlineUserVO> info2vo(List<UserInfoVO> userInfoVOList);
}
