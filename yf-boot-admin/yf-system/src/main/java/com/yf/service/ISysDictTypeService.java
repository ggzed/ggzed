package com.yf.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.model.entity.SysDictType;
import com.yf.model.form.DictTypeForm;
import com.yf.model.query.DictTypePageQuery;
import com.yf.model.vo.DictTypePageVO;

import java.util.List;

/**
 * 字典类型表-SysDictTypeService
 *
 * @author YiFei
 * @since 2024-04-23 18:52:09
 */
public interface ISysDictTypeService extends IService<SysDictType> {
    /**
     * 查询字典类型
     *
     * @param queryParams 分页参数
     * @return 分页数据
     */
    IPage<DictTypePageVO> getDictType(DictTypePageQuery queryParams);

    /**
     * 获取字典类型表单数据
     *
     * @param dictTypeId 字典类型表Id
     * @return 字典类型表单数据
     */
    DictTypeForm getDictTypeForm(Integer dictTypeId);

    /**
     * 新增字典类型
     *
     * @param dictTypeForm 字典类型表单
     * @return 存储后的Id
     */
    Integer saveDictType(DictTypeForm dictTypeForm);

    /**
     * 删除字典类型
     *
     * @param dictTypeIds ids
     * @return 是否删除成功
     */
    boolean deleteDictType(List<Integer> dictTypeIds);

    /**
     * 修改字典类型信息
     *
     * @param dictTypeId   字典类型表Id
     * @param dictTypeForm 字典类型表单数据
     * @return 是否修改成功
     */
    boolean updateDictType(Integer dictTypeId, DictTypeForm dictTypeForm);

    /**
     * 修改字典类型状态
     *
     * @param dictTypeId 字典类型表Id
     * @param status     状态
     * @return 是否字典类型状态修改成功
     */
    boolean updateDictTypeStatus(Integer dictTypeId, Boolean status);
}

