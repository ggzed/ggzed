package com.yf.model.vo;

import com.yf.model.common.ITreeNode;
import lombok.Data;

import java.util.List;

/**
 * Templates目录下文件属性结构VO
 *
 * @author : YiFei
 * @since : 2025/4/9 15:17
 */
@Data
public class TemplatesFileTreeNodeVO implements ITreeNode<String, TemplatesFileTreeNodeVO> {
    /**
     * 文件名
     */
    private String id;
    /**
     * 父文件名
     */
    private String parentId;
    /**
     * 文件名称
     */
    private String name;
    /**
     * 具体内容
     */
    private String content;
    /**
     * 子文件
     */
    private List<TemplatesFileTreeNodeVO> children;
}
