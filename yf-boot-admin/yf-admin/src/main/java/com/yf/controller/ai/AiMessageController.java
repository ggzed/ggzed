//package com.yf.controller.ai;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.yf.log.annotation.OperationLog;
//import com.yf.model.ai.form.AiMessageForm;
//import com.yf.model.ai.query.AiMessagePageQuery;
//import com.yf.model.log.enums.BusinessTypeEnum;
//import com.yf.model.vo.AiMessagePageVO;
//import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
//import com.yf.result.PageResult;
//import com.yf.result.Result;
//import com.yf.service.IAiMessageService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springdoc.core.annotations.ParameterObject;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Arrays;
//import java.util.stream.Collectors;
//
///**
// * Ai 消息记录表-AiMessageController
// *
// * @author: YiFei
// * @since : 2025年6月10日 10:40:12
// */
//@Tag(name = "Ai 消息记录表")
//@RestController
//@RequestMapping("ai_message")
//@RequiredArgsConstructor
//public class AiMessageController {
//
//    private final IAiMessageService aiMessageService;
//
//    @Operation(summary = "查询Ai 消息记录表")
//    @OperationLog(title = "查询Ai 消息记录表", businessType = BusinessTypeEnum.SEARCH)
//    @PreventDuplicateSubmit
//    @PreAuthorize("@permission.checker('ai:message:list')")
//    @GetMapping("/{conversationId}/page")
//    public PageResult<AiMessagePageVO> getAiMessagePage(
//            @PathVariable Long conversationId,
//            @ParameterObject @Validated AiMessagePageQuery queryParams) {
//        IPage<AiMessagePageVO> result = aiMessageService.getAiMessagePage(conversationId, queryParams);
//        return PageResult.success(result);
//    }
//
//    @Operation(summary = "Ai 消息记录表表单数据")
//    @PreventDuplicateSubmit
//    @PreAuthorize("@permission.checker('ai:message:list')")
//    @GetMapping("/{id}/form")
//    public Result<AiMessageForm> getAiMessageForm(@PathVariable Long id) {
//        AiMessageForm aiMessageForm = aiMessageService.getAiMessageForm(id);
//        return Result.judge(aiMessageForm);
//    }
//
//    @Operation(summary = "新增Ai 消息记录表")
//    @OperationLog(title = "新增Ai 消息记录表", businessType = BusinessTypeEnum.INSERT)
//    @PreventDuplicateSubmit
//    @PreAuthorize("@permission.checker('ai:message:save')")
//    @PostMapping
//    public Result<Long> saveAiMessage(@RequestBody @Validated AiMessageForm aiMessageForm) {
//        Long result = aiMessageService.saveAiMessage(aiMessageForm);
//        return Result.judge(result);
//    }
//
//    @Operation(summary = "修改Ai 消息记录表")
//    @OperationLog(title = "修改Ai 消息记录表", businessType = BusinessTypeEnum.UPDATE)
//    @PreventDuplicateSubmit
//    @PreAuthorize("@permission.checker('ai:message:update')")
//    @PutMapping("/{id}")
//    public Result<Void> updateAiMessage(@Parameter(description = "Ai 消息记录表主键") @PathVariable Long id, @RequestBody @Validated AiMessageForm aiMessageForm) {
//        boolean deleted = aiMessageService.updateAiMessage(id, aiMessageForm);
//        return Result.judge(deleted);
//    }
//
//    @Operation(summary = "删除Ai 消息记录表")
//    @OperationLog(title = "删除Ai 消息记录表", businessType = BusinessTypeEnum.DELETE)
//    @PreventDuplicateSubmit
//    @PreAuthorize("@permission.checker('ai:message:delete')")
//    @DeleteMapping("/{ids}")
//    public Result<Void> deleteAiMessage(@Parameter(description = "{ids 以 , 分隔") @PathVariable String ids) {
//        boolean deleted = aiMessageService.deleteAiMessage(Arrays.stream(ids.split(","))
//                .map(Long::parseLong).collect(Collectors.toList()));
//        return Result.judge(deleted);
//    }
//
//}
