package com.yf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.converter.GenTableFieldsConverter;
import com.yf.mapper.generate.GenTableFieldsMapper;
import com.yf.model.generate.bo.DBTableFieldInfoBO;
import com.yf.model.generate.entity.GenTableFields;
import com.yf.model.generate.form.GenTableFieldsForm;
import com.yf.service.IGenTableFieldsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * GenTableFieldsIServiceImpl
 *
 * @author YiFei
 * @since 2024-06-14 16:53:13
 */
@Service("genTableFieldsService")
@RequiredArgsConstructor
public class GenTableFieldsServiceImpl extends ServiceImpl<GenTableFieldsMapper, GenTableFields> implements IGenTableFieldsService {

    private final GenTableFieldsConverter tableFieldsConverter;

    /**
     * 根据表名查询字段
     *
     * @param dbTableName 表名
     * @return 字段 BO
     */
    @Override
    public List<DBTableFieldInfoBO> getDBFields(String dbTableName) {
        return this.getBaseMapper().getDBFields(dbTableName);
    }

    /**
     * 修改生成表字段信息 ( 不能修改 tableId )
     *
     * @param forms 字段表单
     * @return 字段列表
     */
    @Transactional
    @Override
    public boolean updateGenTableFields(List<GenTableFieldsForm> forms) {
        this.updateBatchById(tableFieldsConverter.forms2Entities(forms));
        return true;
    }

    /**
     * @param tableId 生成表 ID
     * @return 字段列表
     */
    @Override
    public List<GenTableFieldsForm> getGenTableFieldsForm(Integer tableId) {
        return tableFieldsConverter.entities2Forms(this.lambdaQuery()
                .eq(GenTableFields::getTableId, tableId)
                .list());
    }
}

