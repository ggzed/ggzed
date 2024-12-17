package com.yf.controller.monitor;

import com.yf.log.annotation.OperationLog;
import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.model.vo.OnlineUserVO;
import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
import com.yf.result.Result;
import com.yf.service.IOnlineUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

/**
 * 在线用户控制器
 *
 * @author : YiFei
 * @since : 2024/7/27 15:22
 */
@Tag(name = "在线用户")
@RestController
@RequestMapping("online_user")
@RequiredArgsConstructor
public class OnlineUserController {

    private final IOnlineUserService onlineUserService;

    @PreventDuplicateSubmit
    @OperationLog(title = "获取在线用户", businessType = BusinessTypeEnum.SEARCH)
    @PreAuthorize("@permission.checker('monitor:online-user:list')")
    @GetMapping(value = "/page")
    public Result<List<OnlineUserVO>> getOnlineUserPage() {
        // 创建一个持续时间为指定秒数的 Flux 流
        List<OnlineUserVO> onlineUserPage = onlineUserService.getOnlineUserPage();
        return Result.success(onlineUserPage);
    }

    @PreventDuplicateSubmit
    @OperationLog(title = "踢出在线用户", businessType = BusinessTypeEnum.DELETE)
    @PreAuthorize("@permission.checker('monitor:online-user:kick-out')")
    @DeleteMapping(value = "/{userId}/kick-out")
    public Result<Void> kickOutOnlineUser(@PathVariable Long userId) {
        boolean result = onlineUserService.kickOutOnlineUser(userId);
        return Result.judge(result);
    }

    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('monitor:online-user:list')")
    @GetMapping(value = "/user-activity-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> streamUserActivitySSE() {
        // 创建一个持续时间为指定秒数的 Flux 流
        return Flux.interval(Duration.ofSeconds(5))
                .flatMap(sequence -> Mono
                        .fromCallable(onlineUserService::getUserActivityNum)
                        .onErrorReturn(0)
                );
    }


}
