package com.yf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.constants.SystemConstants;
import com.yf.converter.DeptConverter;
import com.yf.mapper.SysDeptMapper;
import com.yf.model.Option;
import com.yf.model.entity.SysDept;
import com.yf.model.form.DeptForm;
import com.yf.model.query.DeptPageQuery;
import com.yf.model.vo.DeptPageVo;
import com.yf.service.ISysDeptService;
import com.yf.utils.TreeNodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 部门表-SysDeptIServiceImpl
 *
 * @author YiFei
 * @since 2024-04-23 18:43:34
 */
@Service("sysDeptService")
@RequiredArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    private final DeptConverter deptConverter;

    /**
     * @return 部门下拉列表
     */
    @Override
    public List<Option<Integer>> listDeptOptions() {
        // 1. 查询部门数据
        List<SysDept> list = this.lambdaQuery()
                .select(SysDept::getId, SysDept::getParentId, SysDept::getName)
                .orderByAsc(SysDept::getSort)
                .list();
        // 2. list -> option
        List<Option<Integer>> options = deptConverter.list2options(list);
        // 3. 树形结构返回
        return TreeNodeUtil.buildTree(options, Collections.singletonList(0));
    }

    /**
     * 部门查询
     *
     * @param queryParams 查询参数
     * @return 部门数据
     */
    @Override
    public List<DeptPageVo> getDeptPage(DeptPageQuery queryParams) {
        String keywords = queryParams.getKeywords();
        Integer status = queryParams.getStatus();
        List<SysDept> list = this.lambdaQuery()
                .and(StringUtils.hasText(keywords), wrapper ->
                        wrapper.like(StringUtils.hasText(keywords), SysDept::getName, keywords)// 模糊查询 Name
                )
                .eq(status != null, SysDept::getStatus, status)
                .list();
        // 2. 构建树形结构
        // 2.1 获取到所有父元素
        Set<Integer> allIds = list.stream().map(SysDept::getId).collect(Collectors.toSet());
        Set<Integer> allParentIds = list.stream().map(SysDept::getParentId).collect(Collectors.toSet());
        Set<Integer> ids = allParentIds.stream().filter(id -> !allIds.contains(id)).collect(Collectors.toSet());
        // 3. entity2vo
        List<DeptPageVo> deptPageVos = deptConverter.list2vo(list);
        return TreeNodeUtil.buildTree(deptPageVos, ids);
    }

    /**
     * 部门表单数据
     *
     * @param deptId 部门id
     * @return 部门表单数据
     */
    @Override
    public DeptForm getDeptForm(Integer deptId) {
        // 1. 查询角色数据
        SysDept sysDept = this.lambdaQuery()
                .eq(SysDept::getId, deptId)
                .one();
        // 2. entity 转换为 form 返回
        return deptConverter.entity2form(sysDept);
    }


    /**
     * 删除部门及子部门
     *
     * @param deptIds 部门id集合
     * @return 是否删除成功
     */
    @Override
    public boolean deleteDept(List<Integer> deptIds) {
        // 注: 展示 mybatis-plus 的编写方式 ( 推荐 xml 编写 )
        return this.lambdaUpdate()
                .in(SysDept::getId, deptIds)
                .or(wrapper -> {
                    for (Integer deptId : deptIds) {
                        wrapper.or().apply("CONCAT(',',tree_path, ',') LIKE CONCAT('%,', {0}, ',%')", deptId);
                    }
                })
                .remove();
    }

    /**
     * 修改部门显示状态
     *
     * @param deptId 部门Id
     * @param status 是否隐藏
     * @return 是否修改成功
     */
    @Override
    public boolean updateDeptStatus(String deptId, Boolean status) {
        // 注: 展示 mybatis-plus 的编写方式 ( 推荐 xml 编写 )
        return this.lambdaUpdate()
                .eq(SysDept::getId, deptId)
                .or(wrapper -> wrapper.apply("CONCAT(',',tree_path, ',') LIKE CONCAT('%,', {0}, ',%')", deptId))
                .set(SysDept::getStatus, status)
                .update();
    }

    /**
     * 保存部门
     *
     * @param deptForm 部门表单
     * @return 部门ID
     */
    @Override
    public Integer saveDept(DeptForm deptForm) {
        // 1. 获取父部门 ID
        Integer parentId = deptForm.getParentId();
        // 2. 转换为 SysDept 实体
        SysDept entity = deptConverter.form2entity(deptForm);
        // 3. 设置 entity tree_path
        generatePaths(parentId, entity);
        // 4. 执行保存操作
        boolean saved = this.save(entity);
        return entity.getId();
    }

    /**
     * 修改部门
     *
     * @param deptId   部门ID
     * @param deptForm 部门表单
     * @return 是否修改成功
     */
    @Override
    @Transactional
    public boolean updateDept(Integer deptId, DeptForm deptForm) {
        // 1. 获取父部门 ID
        Integer parentId = deptForm.getParentId();
        // 2. 转换为 SysDept 实体
        SysDept entity = deptConverter.form2entity(deptForm);
        // 3. 查询部门确认是否修改parentId
        SysDept one = this.lambdaQuery()
                .select(SysDept::getTreePath, SysDept::getParentId)
                .eq(SysDept::getId, deptId)
                .one();
        if (parentId.equals(one.getParentId())) {
            // 4. 修改当前元素
            this.lambdaUpdate()
                    .eq(SysDept::getId, deptId)
                    .update(entity);
        } else {
            // 5. 修改当前元素的树形结构
            generatePaths(parentId, entity);
            this.lambdaUpdate()
                    .eq(SysDept::getId, deptId)
                    .update(entity);
            // 6. 修改子菜单的树形结构
            updateDeptChildrenTreePath(deptId);
        }
        return true;
    }

    /**
     * tree_path 路径
     *
     * @param parentId 父id
     * @param entity   未修改tree_path的entity
     * @return entity
     */
    private SysDept generatePaths(Integer parentId, SysDept entity) {
        // 1. 生成 tree_path
        String treePath = TreeNodeUtil.generateTreePath(SystemConstants.INTEGER_ROOT_ID, parentId,
                (currentParentId) -> this.lambdaQuery()
                        .eq(SysDept::getId, currentParentId)
                        .select(SysDept::getTreePath)
                        .one().getTreePath());
        // 2. 设置 tree_path 路径
        entity.setTreePath(treePath);
        return entity;
    }

    /**
     * 根据 deptId 修改子菜单 tree_path
     *
     * @param deptId 当前部门id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDeptChildrenTreePath(Integer deptId) {
        // 1. 查询部门信息
        List<SysDept> childrenBos = this.lambdaQuery()
                .select(SysDept::getId, SysDept::getParentId)
                .eq(SysDept::getId, deptId)
                .or(wrapper -> wrapper.apply("CONCAT(',',tree_path, ',') LIKE CONCAT('%,', {0}, ',%')", deptId))
                .list().stream()
                .filter(sysDept -> !sysDept.getId().equals(deptId))
                .toList();
        // 2.构建子菜单 tree_path 的Hash表
        Map<Integer, String> deptTreePathMap = buildDeptTreePathMap(childrenBos);
        // 3. 构建List<SysDept>
        List<SysDept> collect = childrenBos.stream()
                .map(childrenBo -> SysDept.builder()
                        .id(childrenBo.getId())
                        .treePath(deptTreePathMap.get(childrenBo.getParentId()))
                        .build()
                ).toList();
        // 4. 修改子元素tree_path
        this.saveOrUpdateBatch(collect);
    }

    /**
     * 构建菜单树形结构 map
     *
     * @return key-> parent_id , value -> newTreePath
     */
    private Map<Integer, String> buildDeptTreePathMap(List<SysDept> bos) {
        // 1. 整合所有父 Id 用户构建 tree_path
        Set<Integer> collect = bos.stream().map(SysDept::getParentId).collect(Collectors.toSet());
        // 2. 构建 tree_path 并且转成 Map结构
        return collect.stream()
                .collect(Collectors.toMap(
                                parentId -> parentId,                                 // 构建 Key
                                item -> TreeNodeUtil.generateTreePath(                // 构建 newTreePath
                                        SystemConstants.INTEGER_ROOT_ID,
                                        item,
                                        (parentId) -> this.lambdaQuery()              // 查询出tree_path返回
                                                .select(SysDept::getTreePath)
                                                .eq(SysDept::getId, parentId)
                                                .one().getTreePath()
                                )
                        )
                );
    }
}

