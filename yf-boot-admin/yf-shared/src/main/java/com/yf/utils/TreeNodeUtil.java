package com.yf.utils;


import com.yf.model.ITreeNode;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 树形结构工具类
 * 注 ： 不能省略 ids 或者 rootId , 会导致 Long -> 1L != Integer -> 1 (如果系统非常确定使用Long或者Integer可以设置)
 *
 * @author : YiFei
 * @since : 2023/9/21 20:18
 */
public class TreeNodeUtil {

    /**
     * 父子模块分组，父元素名称
     */
    private static final String PARENT_NAME = "parent";
    /**
     * 父子模块分组，子元素名称
     */
    private static final String CHILDREN_NAME = "children";
    /**
     * 树形结构拼接符号
     */
    private static final String TREE_STRUCTURE_DELIMITER = ",";

    private TreeNodeUtil() {
    }

    public static <T extends ITreeNode<T>> List<T> buildTree(List<T> dataList, Collection<? extends Number> ids) {
        return buildTree(dataList, ids, (data) -> data, (item) -> true);
    }

    public static <T extends ITreeNode<T>> List<T> buildTree(List<T> dataList, Collection<? extends Number> ids, Function<T, T> map) {
        return buildTree(dataList, ids, map, (item) -> true);
    }

    /**
     * 数据集合构建成树形结构 （ 注: 如果最开始的 ids 不在 dataList 中，不会进行任何处理 ）
     *
     * @param dataList 数据集合
     * @param ids      父元素的 Id 集合
     * @param map      调用者提供 Function<T, T> 由调用着决定数据最终呈现形势
     * @param filter   调用者提供 Predicate<T> true 表示过滤 （ 注: 如果将父元素过滤掉等于剪枝 ）
     * @param <T>      extends ITreeNode
     * @return 树形结构对象
     */
    public static <T extends ITreeNode<T>> List<T> buildTree(List<T> dataList, Collection<? extends Number> ids, Function<T, T> map, Predicate<T> filter) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        // 1. 将数据分为 父子结构
        Map<String, List<T>> nodeMap = dataList.stream()
                .filter(filter)
                .collect(Collectors.groupingBy(item -> ids.contains(item.getParentId()) ? PARENT_NAME : CHILDREN_NAME));

        List<T> parent = nodeMap.getOrDefault(PARENT_NAME, Collections.emptyList());
        List<T> children = nodeMap.getOrDefault(CHILDREN_NAME, Collections.emptyList());
        // 1.1 如果未分出或过滤了父元素则将子元素返回
        if (parent.isEmpty()) {
            return children;
        }
        // 2. 使用有序集合存储下一次变量的 ids
        List<Number> nextIds = new ArrayList<>(dataList.size());
        // 3. 遍历父元素 以及修改父元素内容 ( 不能使用 map 优化 , 使用 map 优化会导致无序 [ 除非系统对树形结构不要求有序 ] )
        List<T> collectParent = parent.stream().map(map).toList();
        for (T parentItem : collectParent) {
            List<T> itemChildren = parentItem.getChildren();
            // 3.1 当 parentItem.getChildren() == null 时 , 设置默认值 new ArrayList<>()
            if (itemChildren == null) {
                itemChildren = new ArrayList<>();
                parentItem.setChildren(itemChildren);
            }
            // 3.2 遍历子元素获取 child.parentId 与 parent.id 相等的值
            for (T child : children) {
                if (parentItem.getId().equals(child.getParentId())) {
                    // 3.3 添加子元素
                    itemChildren.add(child);
                    // 3.4 添加下一次遍历的 id
                    nextIds.add(child.getParentId());
                }
            }
        }
        buildTree(children, nextIds, map, filter);
        return parent;
    }

    /**
     * 根据 parentId 生成路径
     *
     * @param parentId    当前元素的 parentId
     * @param getTreePath 返回父元素 treePath
     * @return 当前需要插入的元素 树形路径
     */
    public static String generateTreePath(Number rootId, Number parentId, Function<Serializable, String> getTreePath) {
        StringBuilder treePath = new StringBuilder();
        if (rootId.equals(parentId)) {
            // 1. 如果当前节点是父节点直接返回
            treePath.append(parentId);
        } else {
            // 2. 调用者将当前元素的父元素查出来，方便后续拼接
            String dbTreePath = getTreePath.apply(parentId);
            // 3. 父元素的 treePath + "," + 父元素的id
            if (StringUtils.hasText(dbTreePath)) {
                treePath.append(dbTreePath).append(TREE_STRUCTURE_DELIMITER).append(parentId);
            }
        }
        return treePath.toString();
    }

}
