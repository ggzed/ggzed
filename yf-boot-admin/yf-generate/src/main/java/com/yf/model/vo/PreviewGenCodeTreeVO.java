package com.yf.model.vo;

import com.yf.model.common.ITreeNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * PreviewGenCodeTreeVO
 *
 * @author YiFei
 * @since 2025-04-02 12:53:13
 */
@Data
@Builder
@Schema(description = "PreviewGenCodeTreeVO 类用于表示生成的代码预览树结构，包含代码文件信息及其子节点。")
public class PreviewGenCodeTreeVO implements ITreeNode<String, PreviewGenCodeTreeVO> {

    @Schema(description = "文件路径")
    private String id;

    @Schema(description = "文件路径的父级路径")
    private String parentId;

    @Schema(description = "节点名称，例如文件或文件夹的名称")
    private String name;

    @Schema(description = "指示该节点是否为文件，true 表示文件，false 表示文件夹")
    private Boolean isFile;

    @Schema(description = "代码语言类型，如 java、vue、ts 等")
    private String codeLanguage;

    @Schema(description = "节点内容，通常是代码或文件的相关内容")
    private String content;

    @Schema(description = "子节点列表，包含当前节点的所有子节点")
    private List<PreviewGenCodeTreeVO> children;
}

