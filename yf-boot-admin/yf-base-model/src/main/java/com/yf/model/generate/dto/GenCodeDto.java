package com.yf.model.generate.dto;

import com.yf.model.generate.entity.GenTable;
import com.yf.model.generate.entity.GenTableFields;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 代码生成Dto
 *
 * @author : YiFei
 * @since : 2024/6/25 19:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenCodeDto {
    /**
     * 表信息
     */
    private GenTable table;
    /**
     * 表字段信息
     */
    private List<GenTableFields> fields;
}
