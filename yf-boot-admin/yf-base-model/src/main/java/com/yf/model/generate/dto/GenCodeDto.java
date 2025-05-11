package com.yf.model.generate.dto;

import com.yf.model.generate.entity.GenTable;
import com.yf.model.generate.entity.GenTableFields;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 代码生成Dto
 *
 * @author : YiFei
 * @since : 2024/6/25 19:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenCodeDto {
    /**
     * 表信息
     */
    private GenTable table;
    /**
     * 表字段信息
     * pk -> [{Fields}]   => 默认只有第一个主键有效
     * query -> [{Fields}]
     * form -> [{Fields}]
     * show -> [{Fields}]
     */
    private Map<String, List<GenTableFields>> mapFields;
}
