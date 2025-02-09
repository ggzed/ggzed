package com.yf.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.log.annotation.OperationLog;
import com.yf.model.log.enums.BusinessTypeEnum;
import com.yf.model.system.query.OauthPageQuery;
import com.yf.model.vo.OauthPageVO;
import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
import com.yf.result.PageResult;
import com.yf.result.Result;
import com.yf.service.ISysOauthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户Oauth信息-SysUserOauthController
 *
 * @author: YiFei
 * @since : 2024-04-18 16:59:58
 */
@Tag(name = "用户Oauth信息")
@RestController
@RequestMapping("oauth")
@RequiredArgsConstructor
public class SysOauthController {
    /**
     * 用户Oauth信息-SysUserOauthService
     */
    private final ISysOauthService oauthService;

    @Operation(summary = "查询用户 Oauth 信息")
    @OperationLog(title = "查询用户 Oauth 信息", businessType = BusinessTypeEnum.SEARCH)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:oauth:list')")
    @GetMapping("/page")
    public PageResult<OauthPageVO> getUserOauthPage(@ParameterObject @Validated OauthPageQuery queryParams) {
        IPage<OauthPageVO> result = oauthService.getOauthPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "获取系统支持的第三方授权平台列表")
    @PreventDuplicateSubmit
    @GetMapping("/support/platforms")
    public Result<List<String>> getSupportPlatforms() {
        List<String> result = oauthService.getSupportPlatforms();
        return Result.success(result);
    }


    @Operation(summary = "删除用户 Oauth 信息")
    @OperationLog(title = "删除用户 Oauth 信息", businessType = BusinessTypeEnum.DELETE)
    @PreventDuplicateSubmit
    @PreAuthorize("@permission.checker('system:oauth:delete')")
    @DeleteMapping("/{oauthIds}")
    public Result<Void> deleteUserOauth(@Parameter(description = "用户ID，多个以英文逗号(,)分割")
                                        @PathVariable String oauthIds) {

        boolean result = oauthService.deleteOauth(Arrays.stream(oauthIds.split(","))
                .map(Long::parseLong).collect(Collectors.toList()));
        return Result.judge(result);
    }
}

