package com.yf.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.converter.DictTypeConverter;
import com.yf.exception.ServiceException;
import com.yf.mapper.system.SysDictDataMapper;
import com.yf.mapper.system.SysDictTypeMapper;
import com.yf.model.system.entity.SysDictData;
import com.yf.model.system.entity.SysDictType;
import com.yf.model.system.form.DictTypeForm;
import com.yf.model.system.query.DictTypePageQuery;
import com.yf.model.vo.DictTypePageVO;
import com.yf.result.ResultCode;
import com.yf.service.ISysDictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 字典类型表-SysDictTypeIServiceImpl
 *
 * @author YiFei
 * @since 2024-04-23 18:52:09
 */
@Service("sysDictTypeService")
@RequiredArgsConstructor
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {

    // 防止依赖循环
    private final DictTypeConverter dictTypeConverter;
    private final SysDictDataMapper dictDataMapper;

    /**
     * 查询字典类型
     *
     * @param queryParams 分页参数
     * @return 分页数据
     */
    @Override
    public IPage<DictTypePageVO> getDictType(DictTypePageQuery queryParams) {
        String name = queryParams.getName();
        String type = queryParams.getType();
        Integer status = queryParams.getStatus();
        // 1. 分页查询
        Page<SysDictType> page = this.lambdaQuery()
                .like(StringUtils.hasText(name), SysDictType::getName, name)
                .like(StringUtils.hasText(type), SysDictType::getName, type)
                .eq(status != null, SysDictType::getStatus, status)
                .page(queryParams.lambdaMpPage(SysDictType::getCreateTime, false));
        // 2. entity 转 vo
        return dictTypeConverter.page2pageVo(page);
    }

    /**
     * 获取字典类型表单数据
     *
     * @param dictTypeId 字典类型表Id
     * @return 字典类型表单数据
     */
    @Override
    public DictTypeForm getDictTypeForm(Integer dictTypeId) {
        // 1. 根据Id查询对应值
        SysDictType dictType = this.lambdaQuery()
                .eq(SysDictType::getId, dictTypeId)
                .one();
        // 2. entity 转换为 form
        return dictTypeConverter.entity2form(dictType);
    }

    /**
     * 新增字典类型
     *
     * @param dictTypeForm 字典类型表单
     * @return 存储后的Id
     */
    @Override
    public Integer saveDictType(DictTypeForm dictTypeForm) {
        // 1. 检查是否含有重复的 type
        this.isDuplicate(null, dictTypeForm);
        // 2. form 转换 entity
        SysDictType dictType = dictTypeConverter.form2entity(dictTypeForm);
        // 3. 存储对应数据
        this.save(dictType);
        // 4. 返回 id 值
        return dictType.getId();
    }

    /**
     * 删除字典类型
     *
     * @param dictTypeIds ids
     * @return 是否删除成功
     */
    @Override
    @Transactional
    public boolean deleteDictType(List<Integer> dictTypeIds) {
        // 1. 删除当前表数据
        // 1.1 获取当前表数据 （获取字表 type ）
        List<SysDictType> list = this.lambdaQuery()
                .select(SysDictType::getType)
                .in(SysDictType::getId, dictTypeIds)
                .list();
        // 1.2 删除对应数据
        this.lambdaUpdate()
                .in(SysDictType::getId, dictTypeIds)
                .remove();
        // 2. 删除字表 dict_data 数据
        List<String> dictTypes = list.stream().map(SysDictType::getType).toList();

        dictDataMapper.delete(
                Wrappers.<SysDictData>lambdaQuery()
                        .in(SysDictData::getDictType, dictTypes)
        );
        return true;
    }

    /**
     * 修改字典类型信息
     *
     * @param dictTypeId   字典类型表Id
     * @param dictTypeForm 字典类型表单数据
     * @return 是否修改成功
     */
    @Override
    public boolean updateDictType(Integer dictTypeId, DictTypeForm dictTypeForm) {
        // 1. 检查是否含有重复的 type
        this.isDuplicate(dictTypeId, dictTypeForm);
        // 2. form 转换 entity
        SysDictType dictType = dictTypeConverter.form2entity(dictTypeForm);
        // 3. 修改对应数据
        this.lambdaUpdate()
                .eq(SysDictType::getId, dictTypeId)
                .update(dictType);
        return true;
    }

    /**
     * 修改字典类型状态
     *
     * @param dictTypeId 字典类型表Id
     * @param status     状态
     * @return 是否字典类型状态修改成功
     */
    @Override
    public boolean updateDictTypeStatus(Integer dictTypeId, Boolean status) {
        return this.lambdaUpdate()
                .eq(SysDictType::getId, dictTypeId)
                .set(SysDictType::getStatus, status)
                .update();
    }

    /**
     * 检查 nx 修改/新增数据是否重复
     *
     * @param dictTypeId   id
     * @param dictTypeForm form
     */
    private void isDuplicate(Integer dictTypeId, DictTypeForm dictTypeForm) {
        boolean hasType = StringUtils.hasText(dictTypeForm.getType());
        // 1. 查询对应数据
        SysDictType oneSysDictType = this.lambdaQuery()
                .select(SysDictType::getId, SysDictType::getType)
                .eq(hasType, SysDictType::getType, dictTypeForm.getType())
                .one();
        if (oneSysDictType == null || oneSysDictType.getId().equals(dictTypeId)) {
            return;
        }
        // 2. 校验是否含有 type
        if (hasType && dictTypeForm.getType().equals(oneSysDictType.getType())) {
            throw new ServiceException(ResultCode.DICT_TYPE_DUPLICATE);
        }
    }
}

