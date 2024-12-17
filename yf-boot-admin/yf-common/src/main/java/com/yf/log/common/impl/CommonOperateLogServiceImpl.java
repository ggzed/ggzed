package com.yf.log.common.impl;

import com.yf.log.common.ICommonOperateLogService;
import com.yf.mapper.log.OperateLogMapper;
import com.yf.model.log.entity.OperateLog;
import com.yf.security.model.dto.SysUserDetails;
import com.yf.security.utils.SecurityUtil;
import com.yf.utils.AddressUtil;
import com.yf.utils.IpUtil;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 公共操作日志服务
 *
 * @author : YiFei
 * @since : 2024/12/17 15:20
 */
@Service
@RequiredArgsConstructor
public class CommonOperateLogServiceImpl implements ICommonOperateLogService {

    private final OperateLogMapper operateLogMapper;

    /**
     * 请选填 :
     * operate_log.title    模块标题
     * operate_log.business_type 业务类型
     * operate_log.method     方法名称
     * operate_log.operator_type 操作类型
     * operate_log.operator_param 操作参数
     * operate_log.json_result 返回结果
     * operate_log.status     操作状态
     * operate_log.error_msg  错误消息
     * operate_log.cost_time  操作耗时
     *
     * @param operateLog 操作日志
     */
    @Override
    @Async
    public void asyncSaveOperateLog(OperateLog operateLog) {
        // 填充日志必要信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            SysUserDetails user = SecurityUtil.getUser();
            if (user != null) {
                operateLog.setOperatorName(user.getUsername()); // 操作人员名称
            }
            // 填充操作日志信息
            operateLog.setOperatorIp(IpUtil.getIpAddr(request)); // 获取客户端 IP
            operateLog.setOperatorBrowser(request.getHeader("User-Agent")); // 获取浏览器信息
            operateLog.setOperatorUrl(StringUtils.substring(request.getRequestURI(), 0, 255));
            // 获取 user-agent 方便后续获取 os，browser
            String headerUserAgent = request.getHeader("User-Agent");
            // 解析 userAgent
            UserAgent userAgent = UserAgent.parseUserAgentString(headerUserAgent);
            // 填写 userAgent 信息 （浏览器及操作系统）
            operateLog.setOperatorBrowser(userAgent.getBrowser().getName());
            operateLog.setOperatorOs(userAgent.getOperatingSystem().getName());
            // 解析用户操作地址
            operateLog.setOperatorLocation(AddressUtil.getRealAddressByIP(operateLog.getOperatorIp()));
        }

        // 2. 保存日志
        operateLogMapper.insert(operateLog);
    }
}
