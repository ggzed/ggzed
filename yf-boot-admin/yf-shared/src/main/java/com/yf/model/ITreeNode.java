package com.yf.model;

import java.util.List;

/**
 * 固定树形结构属性
 *
 * @author: YiFei
 * @since : 2023/11/5 20:48
 */
public interface ITreeNode<KEY, T> {
    /**
     * 获取当前元素Id
     *
     * @return 当前元素Id
     */
    KEY getId();

    /**
     * 获取父元素Id
     *
     * @return 父元素Id
     */
    KEY getParentId();

    /**
     * 获取当前元素的 children 属性
     *
     * @return 当前元素的 children 属性
     */
    List<T> getChildren();

    /**
     * 返回值为 void 表示子类不再支持链式表达式
     * 设置 children 值（注: 只有当 children 为 null时设置 new ArrayList()）
     *
     * @param children children 列表
     */
    void setChildren(List<T> children);
}
