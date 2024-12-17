package com.yf.model.system.bo;

import com.yf.model.common.ITreeNode;
import com.yf.model.system.dto.RouteMeta;
import lombok.Data;

import java.util.List;

/**
 * 路由 Bo
 *
 * @author YiFei
 * @since 2024/4/29 19:29
 */
@Data
public class RouteBo implements ITreeNode<Integer, RouteBo> {

    private Integer id;

    private Integer parentId;

    private String path;

    private String component;

    private String redirect;

    private String name;

    private RouteMeta meta;

    private List<RouteBo> children;
}
