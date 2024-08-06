package com.yf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.model.Option;
import com.yf.model.entity.SysDept;
import com.yf.model.form.DeptForm;
import com.yf.model.query.DeptPageQuery;
import com.yf.model.vo.DeptPageVo;

import java.util.List;

/**
 * 部门表-SysDeptService
 *
 * @author YiFei
 * @since 2024-04-23 18:43:34
 */
public interface ISysDeptService extends IService<SysDept> {

    /**
     * @return 部门下拉列表
     */
    List<Option<Integer>> listDeptOptions();

    /**
     * 部门查询
     *
     * @param queryParams 查询参数
     * @return 部门数据
     */
    List<DeptPageVo> getDeptPage(DeptPageQuery queryParams);

    /**
     * 删除部门及子部门
     *
     * @param deptIds 部门id
     * @return 是否删除成功
     */
    boolean deleteDept(List<Integer> deptIds);

    /**
     * 部门表单数据
     *
     * @param deptId 部门id
     * @return 部门表单数据
     */
    DeptForm getDeptForm(Integer deptId);

    /**
     * 修改部门显示状态
     *
     * @param deptId 部门Id
     * @param status 是否隐藏
     * @return 是否修改成功
     */
    boolean updateDeptStatus(String deptId, Boolean status);

    /**
     * 保存部门
     *
     * @param deptForm 部门表单
     * @return 部门ID
     */
    Integer saveDept(DeptForm deptForm);

    /**
     * 修改部门
     *
     * @param deptId   部门ID
     * @param deptForm 部门表单
     * @return 是否修改成功
     */
    boolean updateDept(Integer deptId, DeptForm deptForm);
}

