package com.yf.controller.websocket;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.log.annotation.OperationLog;
import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.model.websocket.dto.UserConnectInfo;
import com.yf.model.websocket.query.ChatRoomUserPageQuery;
import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
import com.yf.result.PageResult;
import com.yf.result.Result;
import com.yf.service.IChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 聊天室控制层
 *
 * @author : YiFei
 * @since : 2024/5/29 17:24
 */
@Tag(name = "聊天室")
@RestController
@RequestMapping("chat-room")
@RequiredArgsConstructor
public class ChatRoomController {

    private final IChatRoomService chatRoomService;

    /**
     * 目前查询所有缓存用户 （ 1. 用户体量不大 ）
     */
    @Operation(summary = "查询聊天室在线用户")
    @OperationLog(title = "查询聊天室在线用户", businessType = BusinessTypeEnum.SEARCH)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('websocket:chat-room:list')")
    @GetMapping("/page")
    public PageResult<UserConnectInfo> getRoomUserPage(@ParameterObject @Validated ChatRoomUserPageQuery queryParams) {
        IPage<UserConnectInfo> result = chatRoomService.getRoomUserPage(queryParams);
        return PageResult.success(result);
    }


    @Operation(summary = "查询聊天室单个用户")
    @OperationLog(title = "查询聊天室单个用户")
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('websocket:chat-room:list')")
    @GetMapping("/{userId}")
    public Result<UserConnectInfo> getRoomUserOne(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        UserConnectInfo userConnectInfo = chatRoomService.getRoomUserOne(userId);
        return Result.success(userConnectInfo);
    }
}
