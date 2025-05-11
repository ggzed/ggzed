package com.yf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.model.generate.bo.DBTableFieldInfoBO;
import com.yf.model.generate.entity.GenTableFields;
import com.yf.model.generate.form.GenTableFieldsForm;

import java.util.List;

/**
 * GenTableFieldsService
 *
 * @author YiFei
 * @since 2024-06-14 16:53:13
 */
public interface IGenTableFieldsService extends IService<GenTableFields> {

    /**
     * 根据表名查询字段
     *
     * @param dbTableName 表名
     * @return 字段 BO
     */
    List<DBTableFieldInfoBO> getDBFields(String dbTableName);

    /**
     * 修改生成表字段信息 ( 不能修改 tableId )
     *
     * @param forms 字段表单
     * @return 字段列表
     */
    boolean updateGenTableFields(List<GenTableFieldsForm> forms);

    /**
     * @param tableId 生成表 ID
     * @return 字段列表
     */
    List<GenTableFieldsForm> getGenTableFieldsForm(Integer tableId);
}

