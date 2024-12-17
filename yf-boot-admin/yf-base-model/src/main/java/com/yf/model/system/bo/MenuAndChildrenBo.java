package com.yf.model.system.bo;

import lombok.Data;

/**
 * 修改菜单Bo
 *
 * @author : YiFei
 * @since : 2024/6/8 11:31
 */
@Data
public class MenuAndChildrenBo {
    /**
     * 菜单id
     */
    private Integer id;
    /**
     * 菜单父元素
     */
    private Integer parentId;
}
