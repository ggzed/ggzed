package com.yf.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.converter.DictDataConverter;
import com.yf.mapper.system.SysDictDataMapper;
import com.yf.model.common.Option;
import com.yf.model.common.enums.EnableStatusEnum;
import com.yf.model.system.entity.SysDictData;
import com.yf.model.system.entity.SysDictType;
import com.yf.model.system.form.DictDataForm;
import com.yf.model.system.query.DictDataPageQuery;
import com.yf.model.vo.DictDataPageVO;
import com.yf.service.ISysDictDataService;
import com.yf.service.ISysDictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典数据表-SysDictDataIServiceImpl
 *
 * @author YiFei
 * @since 2024-04-23 18:52:09
 */
@Service("sysDictDataService")
@RequiredArgsConstructor
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {

    private final DictDataConverter dictDataConverter;
    private final ISysDictTypeService dictTypeService;

    /**
     * 字典下拉列表
     *
     * @param types 字典类型
     * @return 字典列表
     */
    @Override
    public Map<String, List<Option<String>>> listDictOptions(List<String> types) {
        // 1. 查询所有字典数据
        List<SysDictData> dictDataList = this.lambdaQuery()
                .select(SysDictData::getName, SysDictData::getValue, SysDictData::getDictType)
                .in(SysDictData::getDictType, types)
                .eq(SysDictData::getStatus, EnableStatusEnum.ENABLE)
                .orderByAsc(SysDictData::getSort)
                .list();
        // 2. 填充 Map<String,List<Option<Integer>>> key 为 type , value 为 Option
        return dictDataList.stream()
                .collect(Collectors.groupingBy(SysDictData::getDictType,
                        Collectors.mapping(dictDataConverter::entity2option, Collectors.toList())));

//        // 1. 查询字典类型的状态是否可用
//        if (dictTypeService.lambdaQuery()
//                .eq(SysDictType::getType, type)
//                .eq(SysDictType::getStatus, EnableStatusEnum.ENABLE)
//                .exists()) {
//            // 2. 查询字典数据状态是否可用
//            List<SysDictData> list = this.lambdaQuery()
//                    .select(SysDictData::getName, SysDictData::getValue)
//                    .eq(SysDictData::getDictType, type)
//                    .eq(SysDictData::getStatus, EnableStatusEnum.ENABLE)
//                    .orderByAsc(SysDictData::getSort)
//                    .list();
//            return dictDataConverter.list2options(list);
//        } else {
//            return Collections.emptyList();
//        }
    }

    /**
     * 查询字典数据
     *
     * @param dictTypeId  字典类型 Id
     * @param queryParams 分页参数
     * @return 分页数据
     */
    @Override
    public IPage<DictDataPageVO> getDictData(Integer dictTypeId, DictDataPageQuery queryParams) {
        // 1. 获取 dict_type 的 type
        String type = dictTypeService.lambdaQuery()
                .select(SysDictType::getType)
                .eq(SysDictType::getId, dictTypeId)
                .one()
                .getType();
        // 2. 根据 type 分页查询
        String name = queryParams.getName();
        Integer status = queryParams.getStatus();
        Integer defaulted = queryParams.getDefaulted();
        Page<SysDictData> page = this.lambdaQuery()
                .and(StringUtils.hasText(name), query -> query
                        .like(SysDictData::getName, name)
                        .or()
                        .like(SysDictData::getValue, name))
                .eq(SysDictData::getDictType, type)
                .eq(status != null, SysDictData::getStatus, status)
                .eq(defaulted != null, SysDictData::getDefaulted, defaulted)
                .page(queryParams.lambdaMpPage(SysDictData::getSort, true));
        // 3. 转换为 vo 后返回
        return dictDataConverter.page2pageVO(page);
    }

    /**
     * 获取字典数据表单数据
     *
     * @param dictDataId 字典数据表Id
     * @return 字典数据表单数据
     */
    @Override
    public DictDataForm getDictDataForm(Integer dictDataId) {
        // 1. 查询对应数据
        SysDictData dictData = this.lambdaQuery()
                .eq(SysDictData::getId, dictDataId)
                .one();
        // 2. entity 2 form
        return dictDataConverter.entity2form(dictData);
    }

    /**
     * 新增字典数据
     *
     * @param dictDataForm 字典数据表单
     * @return 存储后的Id
     */
    @Override
    public Integer saveDictData(DictDataForm dictDataForm) {
        // 1. form 转 entity
        SysDictData dictData = dictDataConverter.form2entity(dictDataForm);
        // 2. 获取对应 type 值
        String type = dictTypeService.lambdaQuery()
                .select(SysDictType::getType)
                .eq(SysDictType::getId, dictDataForm.getDictTypeId())
                .one()
                .getType();
        dictData.setDictType(type);
        // 3. 存储数据
        this.save(dictData);
        // 4. 返回id
        return dictData.getId();
    }

    /**
     * 删除字典数据
     *
     * @param dictDataIds ids
     * @return 是否删除成功
     */
    @Override
    public boolean deleteDictData(List<Integer> dictDataIds) {
        this.lambdaUpdate()
                .in(SysDictData::getId, dictDataIds)
                .remove();
        return true;
    }

    /**
     * 修改字典数据信息
     *
     * @param dictDataId   字典数据表Id
     * @param dictDataForm 字典数据表单数据
     * @return 是否修改成功
     */
    @Override
    public boolean updateDictData(Integer dictDataId, DictDataForm dictDataForm) {
        // 1. form 转 entity
        SysDictData dictData = dictDataConverter.form2entity(dictDataForm);
        // 2. 修改数据
        this.lambdaUpdate()
                .eq(SysDictData::getId, dictDataId)
                .update(dictData);
        return true;
    }

    /**
     * 修改字典数据状态
     *
     * @param dictDataId 字典数据表Id
     * @param status     状态
     * @return 是否字典数据状态修改成功
     */
    @Override
    public boolean updateDictDataStatus(Integer dictDataId, Boolean status) {
        return this.lambdaUpdate()
                .eq(SysDictData::getId, dictDataId)
                .set(status != null, SysDictData::getStatus, status)
                .update();
    }
}

