package com.yf.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.yf.chat.memory.AiMessageChatMemory;
import com.yf.model.dto.ToolContextDTO;
import com.yf.security.utils.SecurityUtil;
import com.yf.service.IDeepSeekService;
import com.yf.tools.BaseInfoTools;
import com.yf.tools.QrTools;
import com.yf.tools.VvHanTools;
import com.yf.utils.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * AiService
 *
 * @author : YiFei
 * @since : 2024/6/7 9:30
 */
@Service("deepSeekService")
@RequiredArgsConstructor
public class DeepSeekServiceImpl implements IDeepSeekService {

    private final DeepSeekChatModel deepSeekChatModel;
    private final QrTools qrTools;
    private final VvHanTools vvHanTools;
    private final BaseInfoTools baseInfoTools;
    private final HttpServletRequest request;
    private final AiMessageChatMemory aiMessageChatMemory;

    /**
     * 调用 DeepSeek 模型
     *
     * @param prompt prompt
     * @return 流式响应
     */
    @Override
    public Flux<String> callStream(String prompt) {
        // 初始化用户信息
        ToolContextDTO toolContextDTO = initToolContext();
        // 调用 DeepSeek 模型
        Flux<String> resultFlux = ChatClient.builder(deepSeekChatModel)
                .defaultSystem("Minimize tokens consumption")
                .defaultTools(vvHanTools, qrTools, baseInfoTools)
                .defaultToolContext(BeanUtil.beanToMap(toolContextDTO))
                .defaultAdvisors(SimpleLoggerAdvisor.builder().build())
                .build()
                .prompt(Prompt.builder().content(prompt).build())
                .stream()
                .content();

        // 结束后的附加信息
        Flux<String> postFlux = Flux.just("消息来自于系统 YF");

        return Flux.concat(resultFlux, postFlux);
    }

    /**
     * 初始化工具上下文
     *
     * @return ToolContextDTO
     */
    private ToolContextDTO initToolContext() {
        return ToolContextDTO.builder()
                .userId(SecurityUtil.getUserId())
                .roles(SecurityUtil.getUserRoles())
                .ip(IpUtil.getIpAddr(request))
                .headerUserAgent(request.getHeader(HttpHeaders.USER_AGENT))
                .build();
    }

}
