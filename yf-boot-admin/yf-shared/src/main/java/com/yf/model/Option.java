package com.yf.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 数据序列化返回给前端会调用所有get方法
 *
 * @author YiFei
 * @since 2024/5/12 22:02
 */
@Schema(description = "下拉选项对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Option<T extends Number> implements ITreeNode<T, Option<T>> {

    @Schema(name = "value", description = "选项的值")
    @JsonProperty("value")
    private T id;

    @Schema(description = "父元素Id", hidden = true)
    @JsonIgnore
    private T parentId;

    @Schema(description = "选项的标签")
    private String label;

    @Schema(description = "子选项列表")
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private List<Option<T>> children;
}
