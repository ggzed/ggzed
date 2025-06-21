//package com.yf.controller.ai;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.yf.log.annotation.OperationLog;
//import com.yf.model.ai.form.AiConversationForm;
//import com.yf.model.ai.query.AiConversationPageQuery;
//import com.yf.model.log.enums.BusinessTypeEnum;
//import com.yf.model.vo.AiConversationPageVO;
//import com.yf.rate_limiting.annotation.PreventDuplicateSubmit;
//import com.yf.result.PageResult;
//import com.yf.result.Result;
//import com.yf.service.IAiConversationService;
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
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Arrays;
//import java.util.stream.Collectors;
//
///**
// * AI 会话表-AiConversationController
// *
// * @author: YiFei
// * @since : 2025年6月9日 23:36:21
// */
//@Tag(name = "AI 会话表")
//@RestController
//@RequestMapping("ai_conversation")
//@RequiredArgsConstructor
//public class AiConversationController {
//
//    private final IAiConversationService aiConversationService;
//
//    @Operation(summary = "查询 AI 会话表")
//    @OperationLog(title = "查询 AI 会话表", businessType = BusinessTypeEnum.SEARCH)
//    @PreventDuplicateSubmit
//    @PreAuthorize("@permission.checker('ai:conversation:list')")
//    @GetMapping("/page")
//    public PageResult<AiConversationPageVO> getAiConversationPage(@ParameterObject @Validated AiConversationPageQuery queryParams) {
//        IPage<AiConversationPageVO> result = aiConversationService.getAiConversationPage(queryParams);
//        return PageResult.success(result);
//    }
//
//    @Operation(summary = "AI 会话表表单数据")
//    @PreventDuplicateSubmit
//    @PreAuthorize("@permission.checker('ai:conversation:list')")
//    @GetMapping("/{id}/form")
//    public Result<AiConversationForm> getAiConversationForm(@PathVariable Long id) {
//        AiConversationForm aiConversationForm = aiConversationService.getAiConversationForm(id);
//        return Result.judge(aiConversationForm);
//    }
//
//    @Operation(summary = "修改 AI 会话表")
//    @OperationLog(title = "修改 AI 会话表", businessType = BusinessTypeEnum.UPDATE)
//    @PreventDuplicateSubmit
//    @PreAuthorize("@permission.checker('ai:conversation:update')")
//    @PutMapping("/{id}")
//    public Result<Void> updateAiConversation(@Parameter(description = "AI 会话表主键") @PathVariable Long id, @RequestBody @Validated AiConversationForm aiConversationForm) {
//        boolean deleted = aiConversationService.updateAiConversation(id, aiConversationForm);
//        return Result.judge(deleted);
//    }
//
//    @Operation(summary = "删除 AI 会话表")
//    @OperationLog(title = "删除 AI 会话表", businessType = BusinessTypeEnum.DELETE)
//    @PreventDuplicateSubmit
//    @PreAuthorize("@permission.checker('ai:conversation:delete')")
//    @DeleteMapping("/{ids}")
//    public Result<Void> deleteAiConversation(@Parameter(description = "{ids 以 , 分隔") @PathVariable String ids) {
//        boolean deleted = aiConversationService.deleteAiConversation(Arrays.stream(ids.split(","))
//                .map(Long::parseLong).collect(Collectors.toList()));
//        return Result.judge(deleted);
//    }
//
//}
