package com.yf.tools;

import cn.hutool.core.bean.BeanUtil;
import com.yf.converter.ToolCallResultDescConverter;
import com.yf.mapper.system.SysUserMapper;
import com.yf.model.dto.RequestInfoDTO;
import com.yf.model.dto.ToolContextDTO;
import com.yf.model.dto.UserInfoDTO;
import com.yf.model.system.entity.SysUser;
import com.yf.model.system.enums.GenderEnum;
import com.yf.utils.AddressUtil;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 用户基础信息工具
 *
 * @author : YiFei
 * @since : 2025/5/21 0:22
 */
@Component
@RequiredArgsConstructor
public class BaseInfoTools {

    private final SysUserMapper userMapper;

    /**
     * 获取用户身份信息
     *
     * @param toolContext ToolContext对象，包含用户ID和角色信息
     * @return UserInfoDTO 对象，包含用户名、昵称、性别、角色、邮箱和电话号码等信息
     */
    @Tool(description = "Retrieves user identity information including profile data (username, nickname, gender), "
            + "RBAC roles from security context, and contact channels (email, phone). "
            + "Requires user ID and role set as input parameters.",
            resultConverter = ToolCallResultDescConverter.class)
    public UserInfoDTO getUserIdentityInfo(ToolContext toolContext) {
        ToolContextDTO toolContextDTO = BeanUtil.toBean(toolContext.getContext(), ToolContextDTO.class);

        SysUser user = userMapper.selectById(toolContextDTO.getUserId());
        String genderLabel = GenderEnum.MALE.getValue().equals(user.getGender())
                ? GenderEnum.MALE.getLabel()
                : GenderEnum.FEMALE.getLabel();

        return UserInfoDTO.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .gender(genderLabel)
                .roles(toolContextDTO.getRoles())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    /**
     * 获取客户端环境信息
     *
     * @param toolContext ToolContext对象，包含客户端IP地址和User-Agent字符串
     * @return RequestInfoDTO 对象，包含IP地址、地理位置、浏览器类型和操作系统信息
     */
    @Tool(description = "Retrieves client environment details including IP-based geolocation, "
            + "browser type/version, and operating system by parsing User-Agent header. "
            + "Requires client IP and User-Agent string as input parameters.",
            resultConverter = ToolCallResultDescConverter.class)
    public RequestInfoDTO getClientEnvironmentInfo(ToolContext toolContext) {
        ToolContextDTO toolContextDTO = BeanUtil.toBean(toolContext.getContext(), ToolContextDTO.class);
        String geoLocation = AddressUtil.getRealAddressByIP(toolContextDTO.getIp());
        UserAgent parsedAgent = UserAgent.parseUserAgentString(toolContextDTO.getHeaderUserAgent());

        return RequestInfoDTO.builder()
                .ip(toolContextDTO.getIp())
                .address(geoLocation)
                .browser(parsedAgent.getBrowser().getName())
                .operatingSystem(parsedAgent.getOperatingSystem().getName())
                .build();
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间的ISO 8601格式字符串，包含时区信息
     */
    @Tool(description = "Retrieves current server time in ISO 8601 format with timezone information. "
            + "No input parameters required.")
    public String getCurrentServerTime() {
        return LocalDateTime.now()
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }
}
