package com.yf.model.generate.dto;

import com.yf.model.common.ITreeNode;
import lombok.Data;

import java.util.List;

/**
 * 文件属性结构
 *
 * @author : YiFei
 * @since : 2025/4/8 23:30
 */
@Data
public class FileTreeNodeDto implements ITreeNode<String, FileTreeNodeDto> {

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
     * 文件路径
     */
    private String path;
    /**
     * 子文件
     */
    private List<FileTreeNodeDto> children;

}
