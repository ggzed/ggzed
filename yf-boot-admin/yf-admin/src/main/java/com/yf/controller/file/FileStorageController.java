package com.yf.controller.file;

import com.yf.constants.RedisKeyConstants;
import com.yf.oss.constraints.MultipartFileValid;
import com.yf.oss.storage.FileStorageService;
import com.yf.rate_limiting.annotation.RateLimiter;
import com.yf.rate_limiting.annotation.RateLimiters;
import com.yf.rate_limiting.annotation.RateRule;
import com.yf.rate_limiting.model.enums.LimitTypeEnum;
import com.yf.result.Result;
import com.yf.result.ResultCode;
import com.yf.security.utils.SecurityUtil;
import com.yf.utils.RedisUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 文件上传服务
 *
 * @author yiFei
 * @since 2023/12/2 13:37
 */
@Validated
@RestController
@RequestMapping("file")
@Tag(name = "文件上传服务")
@RequiredArgsConstructor
public class FileStorageController {

    /**
     * key : save_path , value : 需要的权限
     */
    public static final Map<String, String> permissionCheckMap = Map.of(
            "demo/markdown", "demo:markdown:list"
    );

    private final FileStorageService fileStorageService;
    private final RedisUtil redisUtil;

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    @RateLimiters(rateLimiters = {
            @RateLimiter(limitTypeEnum = LimitTypeEnum.IP,
                    rateRules = {
                            @RateRule,
                            @RateRule(limit = 100, timeDuration = 1, timeUnit = TimeUnit.DAYS)
                    }),
    })
    public Result<String> uploadFile(
            @RequestParam(required = false, defaultValue = "")
            @Pattern(regexp = "^(?!/).*$", message = "存储路径不能以'/'开头") String savePath,
            @RequestParam @MultipartFileValid MultipartFile file) {
        // 1. 校验权限
        String permission = permissionCheckMap.get(savePath);
        if (permission == null) {
            return Result.failed(ResultCode.AUTH_ACCESS_UNAUTHORIZED);
        } else {
            Long userId = SecurityUtil.getUserId();
            Set<String> permissionsCache = redisUtil.getCacheObject(RedisKeyConstants.USER_PERMISSIONS_CACHE_PREFIX + userId);
            if (!permissionsCache.contains(permission)) {
                return Result.failed(ResultCode.AUTH_ACCESS_UNAUTHORIZED);
            }
        }
        // 2. 保存图片
        return Result.success(fileStorageService.uploadFile(savePath, file));
    }
//
//    @PostMapping("/uploads")
//    @Operation(summary = "上传多个文件")
//    public Result<String[]> uploadFiles(
//            @RequestParam(required = false, defaultValue = "")
//            @Pattern(regexp = "^(?!/).*$", message = "存储路径不能以'/'开头") String savePath,
//            @RequestParam @MultipartFileValid MultipartFile[] files) {
//        return Result.success(fileStorageService.uploadFiles(savePath, files));
//    }
//
//    @DeleteMapping("/delete")
//    @Operation(summary = "删除文件")
//    public Result<String> deleteFile(
//            @RequestParam(required = false, defaultValue = "")
//            @Pattern(regexp = "^(?!/).*$", message = "存储路径不能以'/'开头") String savePath,
//            @RequestParam String fileName) {
//        return Result.judge(fileStorageService.deleteFile(savePath, fileName));
//    }

}
