package com.yf;

import com.yf.model.system.bo.RouteBo;
import com.yf.utils.TreeNodeUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * 树形结构测试
 *
 * @author : YiFei
 * @since : 2024/11/13 20:37
 */
public class TreeNodeTest {

    private static final Logger log = LoggerFactory.getLogger(TreeNodeTest.class);

    @Test
    public void testBuildTreeWithCycleCheck() {
        // 创建 4 个 RouteBo 实例，构成深层循环引用
        RouteBo routeA = new RouteBo();
        routeA.setId(1);
        routeA.setParentId(2);  // routeA 的父节点指向 routeB

        RouteBo routeB = new RouteBo();
        routeB.setId(2);
        routeB.setParentId(3);  // routeB 的父节点指向 routeC

        RouteBo routeC = new RouteBo();
        routeC.setId(3);
        routeC.setParentId(4);  // routeC 的父节点指向 routeD

        RouteBo routeD = new RouteBo();
        routeD.setId(4);
        routeD.setParentId(1);  // routeD 的父节点指向 routeA，形成循环

        // 将它们放入列表中，模拟数据源
        List<RouteBo> dataList = new ArrayList<>();
        dataList.add(routeD);
        dataList.add(routeC);
        dataList.add(routeB);
        dataList.add(routeA);

        List<RouteBo> routeBos = TreeNodeUtil.buildTree(dataList, Collections.singletonList(2), false, false);
        log.info("routeBos: {}", routeBos);
    }

    @Test
    public void testCycleData() {
        // Sample route data
        RouteBo route1 = new RouteBo();
        route1.setId(1);
        route1.setParentId(0);
        RouteBo route2 = new RouteBo();
        route2.setId(2);
        route2.setParentId(3);
        RouteBo route3 = new RouteBo();
        route3.setId(3);
        route3.setParentId(2);

        List<RouteBo> dataList = Arrays.asList(route1, route2, route3);

//        List<RouteBo> tree1 = TreeNodeUtil.buildTree(dataList, Collections.singletonList(2), false, false);

//        List<RouteBo> tree2 = TreeNodeUtil.buildTree(dataList, Arrays.asList(0, 3), false, false);

//        List<RouteBo> tree3 = TreeNodeUtil.buildTree(dataList, List.of(0), false, true);

//        System.out.println(tree1); // 会忽略循环节点
//        System.out.println(tree2); // dataList 会出现循环引用 , 但是 tree 能够正常构建 , 并且会暴露父元素
//        System.out.println(tree3); // dataList 会出现循环引用 , 但是 tree 能够正常构建 , 并且忽略 route2 、 route3

        // dataList 会出现循环引用 , 但是 tree 能够正常构建 , 并且忽略 route2 、 route3
    }

    @Test
    public void testCheckCycleData() {
        // Sample route data
        RouteBo route1 = new RouteBo();
        route1.setId(1);
        route1.setParentId(0);
        RouteBo route2 = new RouteBo();
        route2.setId(2);
        route2.setParentId(3);
        RouteBo route3 = new RouteBo();
        route3.setId(3);
        route3.setParentId(4);
        RouteBo route4 = new RouteBo();
        route4.setId(4);
        route4.setParentId(2);

        List<RouteBo> dataList = Arrays.asList(route1, route2, route3, route4);

        List<RouteBo> tree = TreeNodeUtil.buildTree(dataList, List.of(0), true, false);
    }

}
