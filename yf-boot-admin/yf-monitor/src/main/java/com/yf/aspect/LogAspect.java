package com.yf.aspect;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import com.yf.annotation.OperationLog;
import com.yf.model.dto.SysUserDetails;
import com.yf.model.entity.OperateLog;
import com.yf.model.enums.LogOperatorStatusEnum;
import com.yf.service.IOperateLogService;
import com.yf.utils.AddressUtil;
import com.yf.utils.IpUtil;
import com.yf.utils.SecurityUtil;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 日志记录
 *
 * @author YiFei
 * @since 2024/4/15 15:27
 */
@Slf4j
@Aspect
@Order(10)
@Component
@RequiredArgsConstructor
public class LogAspect {
    public static final String UNKNOWN_USER = "未知用户";
    /**
     * 任务延时毫秒数
     */
    private static final long OPERATE_DELAY_TIME = 60;
    /**
     * 计算方法消耗时间
     */
    private static final ThreadLocal<Long> TIME_THREAD_LOCAL = new NamedThreadLocal<>("Cost Time");
    /**
     * 异步任务线程池
     */
    private final ScheduledExecutorService scheduledExecutorService;
    /**
     * 日记记录服务
     */
    private final IOperateLogService operateLogService;

    /**
     * 处理请求前执行（计算开始时间）
     */
    @Before(value = "@annotation(controllerLog)")
    public void boBefore(JoinPoint joinPoint, OperationLog controllerLog) {
        TIME_THREAD_LOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理完请求后执行（方法正常返回）
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, OperationLog controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作 （方法异常返回）
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, OperationLog controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    /**
     * 处理日志信息
     */
    private void handleLog(JoinPoint joinPoint, OperationLog controllerLog, Exception e, Object jsonResult) {
        try {
            OperateLog operateLog = new OperateLog();
            // 1. 计算消耗时间
            operateLog.setCostTime(System.currentTimeMillis() - TIME_THREAD_LOCAL.get());
            // 2. 填写请求头信息
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            // 3. 获取当前线程的用户
            SysUserDetails user = SecurityUtil.getUser();
            // ===============  根据以上信息填写日志对象  ===============
            // 1.1 获取请求方式
            operateLog.setRequestMethod(request.getMethod());
            // 1.2 获取请求 ip
            operateLog.setOperatorIp(IpUtil.getIpAddr(request));
            // 1.3 获取请求 url
            operateLog.setOperatorUrl(StringUtils.substring(request.getRequestURI(), 0, 255));
            // 2. 解析方法名
            operateLog.setMethod(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()");
            // 3. 获取 user-agent 方便后续获取 os，browser
            String headerUserAgent = request.getHeader("User-Agent");
            // 3.1 解析 userAgent
            UserAgent userAgent = UserAgent.parseUserAgentString(headerUserAgent);
            // 3.2 填写 userAgent 信息 （浏览器及操作系统）
            operateLog.setOperatorBrowser(userAgent.getBrowser().getName());
            operateLog.setOperatorOs(userAgent.getOperatingSystem().getName());
            // 4. 解析@Log参数
            getControllerMethodDescription(joinPoint, request, controllerLog, operateLog, jsonResult);
            // 5. 记录是否操作成功
            if (ObjectUtils.isEmpty(e)) {
                operateLog.setStatus(LogOperatorStatusEnum.SUCCESS);
            } else {
                operateLog.setStatus(LogOperatorStatusEnum.ERROR);
                operateLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 6. 解析当前线程操作的用户
            if (ObjectUtils.isEmpty(user)) {
                operateLog.setOperatorName(UNKNOWN_USER);
            } else {
                // 6.2 设置当前线程操作的用户名
                operateLog.setOperatorName(user.getUsername());
            }
            scheduledExecutorService.schedule(saveLogTask(operateLog), OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
        } catch (Exception error) {
            log.error("记录日志时发生错误 ： ", error);
        } finally {
            TIME_THREAD_LOCAL.remove();
        }
    }

    /**
     * 保存日志信息，以及获取ip真实地址的耗时任务
     *
     * @param operateLog 日志信息
     * @return 任务
     */
    private Runnable saveLogTask(final OperateLog operateLog) {
        return () -> {
            // 7. 获取 ip 真实地址
            operateLog.setOperatorLocation(AddressUtil.getRealAddressByIP(operateLog.getOperatorIp()));
            // 8. 保存日志信息到数据库
            operateLogService.save(operateLog);
        };
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse || o instanceof BindingResult;
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param request       请求信息
     * @param controllerLog 日志注解
     * @param operateLog    操作日志
     */
    private void getControllerMethodDescription(JoinPoint joinPoint, HttpServletRequest request, OperationLog controllerLog, OperateLog operateLog, Object jsonResult) {
        // 1. 解析 @Log 信息
        operateLog.setTitle(controllerLog.title());
        operateLog.setBusinessType(controllerLog.businessType());
        operateLog.setOperatorType(controllerLog.operatorType());
        // 2. 是否保存请求参数
        if (controllerLog.isSaveRequestData()) {
            // 2.1. 获取 request 中的参数
            Map<String, String[]> parameterMap = request.getParameterMap();
            String method = request.getMethod();
            // 2.2. 对不同请求方式进行参数过滤
            if (CollectionUtils.isEmpty(parameterMap) && (HttpMethod.PUT.name().equals(method) || HttpMethod.POST.name().equals(method))) {
                // 2.1.1 排除 joinPoint.getArgs() 含有的敏感字段
                String params = excludeParam(joinPoint.getArgs(), controllerLog.excludeParamNames());
                operateLog.setOperatorParam(StringUtils.substring(params, 0, 2000));
            } else {
                // 2.1.2 排除 其他请求 含有的敏感字段
                HashMap<String, String[]> params = new HashMap<>(parameterMap);
                for (String excludeParam : controllerLog.excludeParamNames()) {
                    params.remove(excludeParam);
                }
                operateLog.setOperatorParam(StringUtils.substring(JSONUtil.toJsonStr(params), 0, 2000));
            }
        }
        // 3. 是否保存响应参数
        if (controllerLog.isSaveResponseData()) {
            // 3.1 响应参数设置（0 , 2000 ，防止超出数据库最大限制）
            operateLog.setJsonResult(StringUtils.substring(JSONUtil.toJsonStr(jsonResult), 0, 2000));
        }
    }

    /**
     * 过滤敏感字段
     *
     * @param args              参数对象
     * @param excludeParamNames 需要排除的参数名
     */
    private String excludeParam(Object[] args, String[] excludeParamNames) {
        // 1. 对象为空直接返回
        if (ObjectUtils.isEmpty(args)) return "";
        // 2. 遍历对象排除对应属性
        for (int i = 0; i < args.length; i++) {
            // 2.1 解析对象字段
            if (args[i] == null || isFilterObject(args[i])) {
                // 2.1.1 不能进行解析的属性置空
                args[i] = null;
                continue;
            }
            Class<?> aClass = args[i].getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            // 2.2 记录每个对象的排除数量
            int count = excludeParamNames.length;
            for (Field declaredField : declaredFields) {
                // 2.3 判断对象字段名是否被排除   ArrayUtils.contains也可以
                if (ArrayUtil.contains(excludeParamNames, declaredField.getName())) {
                    --count;
                    declaredField.setAccessible(true);
                    try {
                        declaredField.set(args[i], null);
                    } catch (IllegalAccessException e) {
                        log.error("排除字段发生错误 {}", declaredField.getName());
                    }
                }
                // 2.4 排除完字段终止本次循环
                if (count == 0) break;
            }
        }
        // 3. 返回 json 字符串
        return JSONUtil.toJsonStr(args);
    }
}
