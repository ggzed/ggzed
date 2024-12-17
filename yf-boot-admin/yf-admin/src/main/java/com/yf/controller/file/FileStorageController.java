package com.yf.controller.file;

import com.yf.oss.storage.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final FileStorageService fileStorageService;

//    @PostMapping("/upload")
//    @Operation(summary = "上传文件")
//    public Result<String> uploadFile(
//            @RequestParam(required = false, defaultValue = "")
//            @Pattern(regexp = "^(?!/).*$", message = "存储路径不能以'/'开头") String savePath,
//            @RequestParam @MultipartFileValid MultipartFile file) {
//        return Result.success(fileStorageService.uploadFile(savePath, file));
//    }
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
