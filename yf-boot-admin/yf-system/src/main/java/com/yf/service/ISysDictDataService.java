package com.yf.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.model.common.Option;
import com.yf.model.system.entity.SysDictData;
import com.yf.model.system.form.DictDataForm;
import com.yf.model.system.query.DictDataPageQuery;
import com.yf.model.vo.DictDataPageVO;

import java.util.List;

/**
 * 字典数据表-SysDictDataService
 *
 * @author YiFei
 * @since 2024-04-23 18:52:09
 */
public interface ISysDictDataService extends IService<SysDictData> {
    /**
     * 字典下拉列表
     *
     * @param type 字典类型
     * @return 字典列表
     */
    List<Option<Integer>> listDictOptions(String type);

    /**
     * 查询字典数据
     *
     * @param dictTypeId  字典类型 Id
     * @param queryParams 分页参数
     * @return 分页数据
     */
    IPage<DictDataPageVO> getDictData(Integer dictTypeId, DictDataPageQuery queryParams);

    /**
     * 获取字典数据表单数据
     *
     * @param dictDataId 字典数据表Id
     * @return 字典数据表单数据
     */
    DictDataForm getDictDataForm(Integer dictDataId);

    /**
     * 新增字典数据
     *
     * @param dictDataForm 字典数据表单
     * @return 存储后的Id
     */
    Integer saveDictData(DictDataForm dictDataForm);

    /**
     * 删除字典数据
     *
     * @param dictDataIds ids
     * @return 是否删除成功
     */
    boolean deleteDictData(List<Integer> dictDataIds);

    /**
     * 修改字典数据信息
     *
     * @param dictDataId   字典数据表Id
     * @param dictDataForm 字典数据表单数据
     * @return 是否修改成功
     */
    boolean updateDictData(Integer dictDataId, DictDataForm dictDataForm);

    /**
     * 修改字典数据状态
     *
     * @param dictDataId 字典数据表Id
     * @param status     状态
     * @return 是否字典数据状态修改成功
     */
    boolean updateDictDataStatus(Integer dictDataId, Boolean status);
}

