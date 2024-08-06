package com.yf.controller.generate.genCode;

import com.yf.service.IGenTableFieldsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * GenTableFieldsController
 *
 * @author: YiFei
 * @since : 2024-06-14 16:53:13
 */
@Tag(name = "生成字段")
@RestController
@RequestMapping("gen/table/fields")
@RequiredArgsConstructor
public class GenTableFieldsController {
    /**
     * GenTableFieldsService
     */
    private final IGenTableFieldsService genTableFieldsService;

    @Operation(summary = "template")
    @GetMapping("/template")
    public String template() {
        return "template";
    }

}

