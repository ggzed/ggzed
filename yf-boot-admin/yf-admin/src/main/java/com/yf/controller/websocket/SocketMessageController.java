package com.yf.controller.websocket;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.model.vo.SocketMessageVO;
import com.yf.model.websocket.query.SocketMessagePageQuery;
import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
import com.yf.result.PageResult;
import com.yf.result.Result;
import com.yf.service.ISocketMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Socket消息控制层
 *
 * @author : YiFei
 * @since : 2024/5/29 17:25
 */
@Tag(name = "Socket消息")
@RestController
@RequestMapping("message")
@RequiredArgsConstructor
public class SocketMessageController {

    private final ISocketMessageService socketMessageService;

    /**
     * TODO 修改为私人操作
     *
     * @param queryParams
     * @return
     */
    @Operation(summary = "查询Socket消息")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('websocket:message:list')")
    @GetMapping("/page")
    public PageResult<SocketMessageVO> getSocketMessagePage(@ParameterObject @Validated SocketMessagePageQuery queryParams) {
        IPage<SocketMessageVO> result = socketMessageService.getSocketMessagePage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "修改Socket消息状态")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('websocket:message:list')")
    @PatchMapping("/{senderId}/read")
    public Result<?> updateMessageIsRead(
            @Parameter(description = "修改senderId消息为已读") @PathVariable Long senderId) {
        boolean success = socketMessageService.updateMessageIsRead(senderId);
        return Result.judge(success);
    }
}
