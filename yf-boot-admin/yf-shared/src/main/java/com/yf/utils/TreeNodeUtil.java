package com.yf.utils;


import com.yf.model.ITreeNode;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Consumer;
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
    @Deprecated
    private static final String PARENT_NAME = "parent";
    /**
     * 父子模块分组，子元素名称
     */
    @Deprecated
    private static final String CHILDREN_NAME = "children";
    /**
     * 树形结构拼接符号
     */
    private static final String TREE_STRUCTURE_DELIMITER = ",";


    /**
     * 扩展方法 : 构建树形结构 , 可不指定父元素 Id
     */
    public static <KEY, T extends ITreeNode<KEY, T>> List<T> buildTree(List<T> dataList) {
        Map<KEY, T> dataMap = dataList.stream().collect(Collectors.toMap(ITreeNode::getId, item -> item));
        return buildTree(dataList, null, dataMap, true, true);
    }

    /**
     * 扩展方法 : 构建树形结构 , 指定父元素 Id
     */
    public static <KEY, T extends ITreeNode<KEY, T>> List<T> buildTree(Collection<T> dataList, Collection<KEY> ids) {
        Map<KEY, T> dataMap = dataList.stream().collect(Collectors.toMap(ITreeNode::getId, item -> item));
        return buildTree(dataList, ids, dataMap, false, false);
    }

    /**
     * 扩展方法 : 通过 Consumer 对象处理数据
     */
    public static <KEY, T extends ITreeNode<KEY, T>> List<T> buildTree(List<T> dataList, Collection<KEY> ids, Consumer<T> consumer) {
        Map<KEY, T> dataMap = dataList.stream().peek(consumer).collect(Collectors.toMap(ITreeNode::getId, item -> item));
        return buildTree(dataList, ids, dataMap, true, true);
    }

    /**
     * 扩展方法 :
     * 1. 通过 MAP 构建树形结构并不影响原有结构 例如 : item -> new Item(item);
     * 2. 通过 MAP T 转换成 E 类型
     */
    public static <KEY, T, E extends ITreeNode<KEY, E>> List<E> buildTree(List<T> dataList, Collection<KEY> ids, Function<T, E> map) {
        // 注 : 不采用链式写法是为了减少循环次数
        Map<KEY, E> dataMap = new HashMap<>();
        List<E> typeEdataList = new ArrayList<>(dataList.size());

        for (T item : dataList) {
            E mappedItem = map.apply(item);
            typeEdataList.add(mappedItem);
            dataMap.put(mappedItem.getId(), mappedItem);  // 添加到映射中
        }
        return buildTree(typeEdataList, ids, dataMap, false, false);
    }

    /**
     * 扩展方法 : 调用者自定义 isCycleCheck 、isSaveFreeElements
     */
    public static <KEY, T extends ITreeNode<KEY, T>> List<T> buildTree(Collection<T> dataList, Collection<KEY> ids, Boolean isCycleCheck, Boolean isSaveFreeElements) {
        Map<KEY, T> dataMap = dataList.stream().collect(Collectors.toMap(ITreeNode::getId, item -> item));
        return buildTree(dataList, ids, dataMap, isCycleCheck, isSaveFreeElements);
    }

    /**
     * 构建树形结构
     *
     * @param dataList           数据集合
     * @param ids                父元素 Id 集合
     * @param dataMap            数据映射
     * @param isCycleCheck       是否检测循环引用
     * @param isSaveFreeElements 是否保存游离元素
     * @param <KEY>              主键类型
     * @param <T>                数据类型
     * @return 树形结构
     */
    public static <KEY, T extends ITreeNode<KEY, T>> List<T> buildTree(Collection<T> dataList, Collection<KEY> ids, Map<KEY, T> dataMap, Boolean isCycleCheck, Boolean isSaveFreeElements) {
        // 1. 非空校验
        if (CollectionUtils.isEmpty(ids)) {
            ids = Collections.emptyList();
        }

        // 2. 是否检测循环引用
        if (isCycleCheck) {
            return buildTreeWithCycleCheck(dataList, ids, dataMap, isSaveFreeElements);
        }

        // 3. 构建树形结构
        List<T> tree = isSaveFreeElements ? new ArrayList<>(dataList) : new ArrayList<>();
        for (T target : dataList) {
            if (!isSaveFreeElements && ids.contains(target.getParentId())) {
                // 3.1 处理父元素
                tree.add(target);
            } else {
                // 3.2 处理子元素
                T parent = dataMap.get(target.getParentId());
                setElementChildren(isSaveFreeElements, target, parent, tree);
            }

        }
        return tree;
    }

    /**
     * 扩展方法 : 构建树形结构并检查循环引用
     */
    private static <KEY, T extends ITreeNode<KEY, T>> List<T> buildTreeWithCycleCheck(Collection<T> dataList, Collection<KEY> ids, Map<KEY, T> dataMap, Boolean isSaveFreeElements) {
        // 1. 初始化并查集实例
        UnionFind unionFind = new UnionFind();

        // 2. 构建树形结构
        List<T> tree = isSaveFreeElements ? new ArrayList<>(dataList) : new ArrayList<>();

        // 3. 遍历所有节点，构建树并检查循环引用
        for (T target : dataList) {
            KEY parentId = target.getParentId();

            // 4. 检查循环引用：如果当前节点的父节点已经与当前节点连接过，说明有循环引用
            if (unionFind.isConnected(target.getId(), parentId)) {
                throw new IllegalStateException(unionFind.getCyclePath(target, dataMap));
            }

            // 5. 将当前节点与其父节点合并（防止循环）
            unionFind.union(target.getId(), parentId);

            if (!isSaveFreeElements && ids.contains(target.getParentId())) {
                // 5.1 处理父元素
                tree.add(target);
            } else {
                // 5.2 处理子元素
                T parent = dataMap.get(parentId);
                setElementChildren(isSaveFreeElements, target, parent, tree);
            }
        }
        return tree;
    }

    /**
     * 设置元素的子元素
     *
     * @param isSaveFreeElements 是否保存游离元素 ( 注 : 会自动忽略循环问题 )
     * @param target             当前元素
     * @param parent             父元素
     * @param tree               树形结构
     * @param <KEY>              主键类型
     * @param <T>                数据类型
     */
    private static <KEY, T extends ITreeNode<KEY, T>> void setElementChildren(Boolean isSaveFreeElements, T target, T parent, Collection<T> tree) {
        if (parent != null) {
            // 设置子元素
            List<T> children = parent.getChildren();
            if (children == null) {
                children = new ArrayList<>();
                parent.setChildren(children);
            }
            children.add(target);
            if (isSaveFreeElements) {
                tree.remove(target);
            }
        }
    }

    /**
     * 根据 parentId 生成路径
     *
     * @param parentId    当前元素的 parentId
     * @param getTreePath 返回父元素 treePath
     * @return 当前需要插入的元素 树形路径
     */
    public static <KEY extends Number> String generateTreePath(KEY rootId, KEY parentId, Function<KEY, String> getTreePath) {
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

    @Deprecated
    public static <KEY, T extends ITreeNode<KEY, T>> List<T> buildTreeDeprecated(List<T> dataList, Collection<KEY> ids) {
        return buildTreeDeprecated(dataList, ids, (data) -> data, (item) -> true);
    }

    @Deprecated
    public static <KEY, T extends ITreeNode<KEY, T>> List<T> buildTreeDeprecated(List<T> dataList, Collection<KEY> ids, Function<T, T> map) {
        return buildTreeDeprecated(dataList, ids, map, (item) -> true);
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
    @Deprecated
    public static <KEY, T extends ITreeNode<KEY, T>> List<T> buildTreeDeprecated(List<T> dataList, Collection<KEY> ids, Function<T, T> map, Predicate<T> filter) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        // 1. 将数据分为 父子结构
        Map<String, List<T>> nodeMap = dataList.stream().filter(filter).collect(Collectors.groupingBy(item -> ids.contains(item.getParentId()) ? PARENT_NAME : CHILDREN_NAME));

        List<T> parent = nodeMap.getOrDefault(PARENT_NAME, Collections.emptyList());
        List<T> children = nodeMap.getOrDefault(CHILDREN_NAME, Collections.emptyList());
        // 1.1 如果未分出或过滤了父元素则将子元素返回
        if (parent.isEmpty()) {
            return children;
        }
        // 2. 使用有序集合存储下一次变量的 ids
        List<KEY> nextIds = new ArrayList<>(dataList.size());
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
        buildTreeDeprecated(children, nextIds, map, filter);
        return parent;
    }

    // 并查集类，用于实现集合的合并和查找操作，支持检测循环引用
    static class UnionFind {

        // 存储每个元素的父节点，用于实现并查集的路径压缩
        private final Map<Object, Object> parent = new HashMap<>();

        /**
         * 查找给定元素的根节点，并使用路径压缩优化查找过程。
         *
         * @param p 需要查找的元素
         * @return 元素 p 的根节点
         */
        public Object find(Object p) {
            // 如果元素 p 不在并查集中，初始化它的父节点为自己
            parent.putIfAbsent(p, p);

            // 如果元素的父节点不是它本身，则递归查找根节点，并路径压缩
            if (!parent.get(p).equals(p)) {
                parent.put(p, find(parent.get(p)));  // 路径压缩
            }

            // 返回根节点
            return parent.get(p);
        }

        /**
         * 合并两个元素所在的集合。如果两个元素的根节点不同，则将它们合并到同一个集合。
         *
         * @param p 元素 p
         * @param q 元素 q
         */
        public void union(Object p, Object q) {
            // 查找元素 p 和元素 q 的根节点
            Object rootP = find(p);
            Object rootQ = find(q);

            // 如果 p 和 q 的根节点不同，进行合并操作
            if (!rootP.equals(rootQ)) {
                parent.put(rootP, rootQ);  // 将 p 的根节点指向 q 的根节点，合并两个集合
            }
        }

        /**
         * 判断两个元素是否属于同一个集合。即判断它们的根节点是否相同。
         *
         * @param p 元素 p
         * @param q 元素 q
         * @return 如果 p 和 q 属于同一个集合，则返回 true；否则返回 false
         */
        public boolean isConnected(Object p, Object q) {
            // 通过查找根节点判断两个元素是否在同一个集合中
            return find(p).equals(find(q));
        }

        /**
         * 获取检测到的循环路径，用于循环引用检测时输出路径。
         *
         * @param target  当前检测的目标节点
         * @param dataMap 存储所有节点的映射关系 (节点ID -> 节点)
         * @param <T>     泛型，继承自 ITreeNode 的类型
         * @param <KEY>   节点 ID 的类型
         * @return 返回一个字符串，表示检测到的循环路径
         */
        public <T extends ITreeNode<KEY, T>, KEY> String getCyclePath(T target, Map<KEY, T> dataMap) {
            StringBuilder builder = new StringBuilder("检测到循环引用! 循环路径: ");

            // 用于记录路径中已经遍历的节点，防止重复
            Set<KEY> cyclePath = new HashSet<>();

            // 当前节点的 ID
            KEY currentId = target.getId();

            // 遍历节点，直到遇到循环（即节点重复）
            do {
                cyclePath.add(currentId);  // 将当前节点加入路径集合

                // 如果当前节点是目标节点，则路径直接开始
                if (currentId.equals(target.getId())) {
                    builder.append(currentId);
                } else {
                    // 否则，按 " -> " 连接路径
                    builder.append(" -> ").append(currentId);
                }

                // 获取当前节点的父节点 ID
                currentId = dataMap.get(currentId).getParentId();

            } while (!cyclePath.contains(currentId));  // 如果路径中已包含当前节点，说明检测到循环

            // 在路径末尾加入循环开始节点，形成完整的循环路径
            return builder.append(" -> ").append(target.getId()).toString();
        }
    }

}
